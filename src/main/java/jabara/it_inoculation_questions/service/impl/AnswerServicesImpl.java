/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.entity.Answer_;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.entity.AnswersSave_;
import jabara.it_inoculation_questions.entity.Answers_;
import jabara.it_inoculation_questions.model.AnswerSummary;
import jabara.it_inoculation_questions.model.AnswersStatistics;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.QuestionType;
import jabara.it_inoculation_questions.model.Selection;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.jpa.entity.EntityBase_;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * @author jabaraster
 */
public class AnswerServicesImpl implements IAnswersService {

    private static final String        QUOT = "\"";    //$NON-NLS-1$
    private static final String        SEP  = ",";     //$NON-NLS-1$
    private static final int           ON   = 1;
    private static final int           OFF  = 0;

    private final EntityManagerFactory emf;

    @Inject
    IQuestionService                   questionService;

    /**
     * @param pEmf {@link EntityManagerFactory}オブジェクト.
     */
    @Inject
    public AnswerServicesImpl(final EntityManagerFactory pEmf) {
        ArgUtil.checkNull(pEmf, "pEmf"); //$NON-NLS-1$
        this.emf = pEmf;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#countAllAnswers()
     */
    @Override
    public long countAllAnswers() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<Answers> root = query.from(Answers.class);

        query.multiselect(builder.count(root));

        return em.createQuery(query).getSingleResult().longValue();
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#decide(jabara.it_inoculation_questions.entity.AnswersSave)
     */
    @Override
    public Answers decide(final AnswersSave pAnswersSave) {
        ArgUtil.checkNull(pAnswersSave, "pAnswersSave"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();

        em.remove(em.merge(pAnswersSave));

        final Answers answers = new Answers();
        for (final Answer answer : pAnswersSave) {
            answers.addAnswer(answer.getQuestionIndex(), answer.getValues());
        }

        em.persist(answers);

        return answers;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#getAllAnswers()
     */
    @Override
    public List<Answers> getAllAnswers() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
        final Root<Answers> root = query.from(Answers.class);
        query.orderBy(builder.desc(root.get(EntityBase_.created)));
        return em.createQuery(query).getResultList();
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#getAnswersById(long)
     */
    @Override
    public Answers getAnswersById(final long pId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
        final Root<Answers> root = query.from(Answers.class);

        query.distinct(true);
        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pId)));

        root.fetch(Answers_.answers);

        try {
            final Answers ret = em.createQuery(query).getSingleResult();
            for (final Answer answer : ret) {
                for (final AnswerValue value : answer.getValues()) {
                    value.getOptionTexts();
                }
            }
            return ret;

        } catch (final NoResultException e) {
            throw NotFound.GLOBAL;
        }
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#getAnswersStatistics()
     */
    @Override
    public List<AnswersStatistics> getAnswersStatistics() {
        final List<Question> questions = this.questionService.getQuestions();

        final List<AnswersStatistics> ret = new ArrayList<AnswersStatistics>();

        for (int i = 0; i < questions.size(); i++) {
            final Map<String, ValueAndCount> answers = getAnswersByQuestionIndex(i);
            final Question question = questions.get(i);
            final AnswersStatisticsImpl statistics = new AnswersStatisticsImpl(question.getMessage());

            if (question.getType() == QuestionType.SELECT) {
                addSelectQuestionAnswerSummary(answers, question, statistics);
            } else {
                addTextQuestionAnswerSummary(answers, statistics);
            }

            ret.add(statistics);
        }

        return ret;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#getSavedByKey(java.lang.String, int)
     */
    @Override
    public AnswersSave getSavedByKey(final String pAnswersKey, final int pQuestionsCount) {
        ArgUtil.checkNullOrEmpty(pAnswersKey, "pAnswersKey"); //$NON-NLS-1$
        try {
            return getByKeyCore(pAnswersKey);
        } catch (final NotFound e) {
            return insertAnswers(pAnswersKey, pQuestionsCount);
        }
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#makeAnswersCsv()
     */
    @SuppressWarnings("resource")
    @Override
    public File makeAnswersCsv() {
        OutputStream out = null;
        BufferedWriter writer = null;
        try {
            final File tempFile = File.createTempFile(this.getClass().getName(), ".csv"); //$NON-NLS-1$

            out = new FileOutputStream(tempFile);
            writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("utf-8"))); //$NON-NLS-1$

            final List<Question> questions = this.questionService.getQuestions();

            writer.write(buildHeaderLine(questions));
            writer.newLine();

            for (final Answers answers : getAllAnswers()) {
                writer.write(buildCsvLine(questions, answers));
                writer.newLine();
            }
            writer.flush();
            return tempFile;

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        } finally {
            IoUtil.close(writer);
            IoUtil.close(out);
        }
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#update(jabara.it_inoculation_questions.entity.Answer)
     */
    @Override
    public void update(final Answer pAnswer) {
        ArgUtil.checkNull(pAnswer, "pAnswer"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();

        final Answer merged = getAnswerById(pAnswer.getId().longValue());
        merged.setQuestionIndex(pAnswer.getQuestionIndex());

        final List<AnswerValue> values = merged.getValues();
        for (final AnswerValue value : values) {
            em.remove(value);
        }
        values.clear();
        for (final AnswerValue value : pAnswer.getValues()) {
            if (value.getId() == null) {
                em.persist(value);
                values.add(value);
            } else {
                values.add(em.merge(value));
            }
        }
    }

    private Answer getAnswerById(final long pAnswerId) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Answer> query = builder.createQuery(Answer.class);
        final Root<Answer> root = query.from(Answer.class);

        query.distinct(true);
        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pAnswerId)));

        root.fetch(Answer_.values, JoinType.LEFT);

        return em.createQuery(query).getSingleResult();
    }

    private Map<String, ValueAndCount> getAnswersByQuestionIndex(final int pQuestionIndex) {
        return Collections.emptyMap();
        // final EntityManager em = getEntityManager();
        // final CriteriaBuilder builder = em.getCriteriaBuilder();
        // final CriteriaQuery<ValueAndCount> query = builder.createQuery(ValueAndCount.class);
        // final Root<Answer> root = query.from(Answer.class);
        //
        // query.select(builder.construct(ValueAndCount.class, root.get(Answer_.value), builder.count(root.get(Answer_.value))));
        // query.groupBy(root.get(Answer_.value));
        //
        // query.where( //
        // builder.equal(root.get(Answer_.questionIndex), Integer.valueOf(pQuestionIndex)) //
        // , builder.isMember(root, query.from(Answers.class).get(Answers_.answers)) //
        // );
        //
        // final List<ValueAndCount> list = em.createQuery(query).getResultList();
        // final Map<String, ValueAndCount> ret = new HashMap<String, AnswerServicesImpl.ValueAndCount>();
        // for (final ValueAndCount vc : list) {
        // ret.put(vc.getValue(), vc);
        // }
        // return ret;
    }

    private AnswersSave getByKeyCore(final String pAnswersKey) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<AnswersSave> query = builder.createQuery(AnswersSave.class);
        final Root<AnswersSave> root = query.from(AnswersSave.class);

        query.where(builder.equal(root.get(AnswersSave_.key), pAnswersKey));
        query.distinct(true);

        root.fetch(AnswersSave_.answers, JoinType.LEFT);

        try {
            final AnswersSave ret = em.createQuery(query).getSingleResult();
            for (final Answer a : ret) {
                for (@SuppressWarnings("unused")
                final AnswerValue v : a) {
                    // 処理なし
                }
            }
            return ret;
        } catch (final NoResultException e) {
            throw NotFound.GLOBAL;
        }
    }

    private EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    private AnswersSave insertAnswers(final String pAnswersKey, final int pQuestionsCount) {
        final AnswersSave ret = new AnswersSave();
        ret.setKey(pAnswersKey);
        for (int i = 0; i < pQuestionsCount; i++) {
            ret.newAnswer();
        }
        insertCore(ret);
        return ret;
    }

    private void insertCore(final AnswersSave pAnswers) {
        getEntityManager().persist(pAnswers);
    }

    private static void addSelectQuestionAnswerSummary( //
            final Map<String, ValueAndCount> pAnswers //
            , final Question pQuestion //
            , final AnswersStatisticsImpl pStatistics) {

        for (final Selection selection : pQuestion.getSelections()) {
            final ValueAndCount vc = pAnswers.get(selection.getValue());
            final int answerCount = vc == null ? 0 : (int) vc.getCount();
            final AnswerSummaryImpl summary = new AnswerSummaryImpl(answerCount, selection.getLabel());
            pStatistics.addAnswerSummary(summary);
        }
    }

    private static void addTextQuestionAnswerSummary(final Map<String, ValueAndCount> pAnswers, final AnswersStatisticsImpl pStatistics) {
        for (final Map.Entry<String, ValueAndCount> entry : pAnswers.entrySet()) {
            final String value = entry.getValue().getValue();
            if (value != null) {
                final AnswerSummaryImpl summary = new AnswerSummaryImpl(1, value);
                pStatistics.addAnswerSummary(summary);
            }
        }
    }

    private static void appendOtherIfExists(final StringBuilder pSb, final Question pQuestion) {
        final Selection otherSelection = pQuestion.getOtherSelection();
        if (otherSelection != null) {
            appendStringToken(pSb, otherSelection.getLabel());
        }
        appendSeparator(pSb);
    }

    private static void appendSeparator(final StringBuilder pSb) {
        pSb.append(SEP);
    }

    private static void appendStringToken(final StringBuilder pSb, final String pToken) {
        pSb.append(QUOT);
        pSb.append(pToken);
        pSb.append(QUOT);
    }

    private static String buildCsvLine(final List<Question> pQuestion, final Answers pAnswers) {
        final StringBuilder sb = new StringBuilder();
        sb.append(pAnswers.getId().longValue());
        sb.append(SEP);

        for (final Answer answer : pAnswers) {
            final Question q = pQuestion.get(answer.getQuestionIndex());
            switch (q.getType()) {
            case SELECT:
            case MULTI_SELECT:
                buildCsvLineForSelect(sb, q, answer);
                break;
            case TEXT:
            case TEXTAREA:
                buildCsvLineForText(sb, answer);
                break;
            case SELECT_WITH_TEXT:
                buildCsvLineForSelectWithText(sb, q, answer);
                break;
            default:
                throw new IllegalStateException();
            }
        }

        sb.delete(sb.length() - 1, sb.length());
        return new String(sb);
    }

    private static void buildCsvLineForSelect(final StringBuilder pSb, final Question pQuestion, final Answer pAnswer) {
        // 選択されている項目には1を、選択されていない項目には0を設定する.
        for (final Selection selection : pQuestion.getSelections()) {
            boolean exists = false;
            for (final AnswerValue value : pAnswer) {
                if (selection.getValue().equals(value.getValue())) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                pSb.append(ON);
            } else {
                pSb.append(OFF);
            }
            appendSeparator(pSb);
        }
        // その他テキストを出力する.
        final Selection otherSelection = pQuestion.getOtherSelection();
        if (otherSelection != null) {
            String s = Empty.STRING;
            for (final AnswerValue value : pAnswer) {
                if (!value.getValue().equals(otherSelection.getValue())) {
                    continue;
                }
                s = value.getOptionText();
            }
            appendStringToken(pSb, s);
            appendSeparator(pSb);
        }
    }

    private static void buildCsvLineForSelectWithText(final StringBuilder pSb, final Question pQuestion, final Answer pAnswer) {
        // TODO Auto-generated method stub

    }

    private static void buildCsvLineForText(final StringBuilder pSb, final Answer pAnswer) {
        final List<AnswerValue> values = pAnswer.getValues();
        if (values.isEmpty()) {
            appendStringToken(pSb, Empty.STRING);
        } else {
            appendStringToken(pSb, values.get(0).getValue());
        }
        appendSeparator(pSb);
    }

    private static String buildHeaderLine(final List<Question> pQuestions) {
        final StringBuilder sb = new StringBuilder();
        appendStringToken(sb, "ID"); //$NON-NLS-1$
        appendSeparator(sb);
        for (final Question q : pQuestions) {
            final List<Selection> selections = q.getSelections();
            switch (q.getType()) {
            case SELECT:
            case MULTI_SELECT:
                appendStringToken(sb, q.getMessage() + selections.get(0).getLabel());
                appendSeparator(sb);
                for (int i = 1; i < selections.size(); i++) {
                    appendStringToken(sb, selections.get(i).getLabel());
                    appendSeparator(sb);
                }
                appendOtherIfExists(sb, q);
                break;
            case TEXT:
            case TEXTAREA:
                appendStringToken(sb, q.getMessage());
                appendSeparator(sb);
                break;
            case SELECT_WITH_TEXT:
                // TODO
                break;
            default:
                throw new IllegalStateException();
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        return new String(sb);
    }

    /**
     * @author jabaraster
     */
    public static class AnswersStatisticsImpl implements AnswersStatistics {
        private static final long         serialVersionUID = 3942976868013085336L;

        private final List<AnswerSummary> list             = new ArrayList<AnswerSummary>();
        private final String              questionMessage;

        /**
         * @param pQuestionMessage 選択肢文.
         */
        public AnswersStatisticsImpl(final String pQuestionMessage) {
            this.questionMessage = pQuestionMessage;
        }

        /**
         * @param pSummary
         */
        public void addAnswerSummary(final AnswerSummary pSummary) {
            ArgUtil.checkNull(pSummary, "pSummary"); //$NON-NLS-1$
            this.list.add(pSummary);
        }

        /**
         * @see jabara.it_inoculation_questions.model.AnswersStatistics#getAnswerSummaries()
         */
        @Override
        public List<AnswerSummary> getAnswerSummaries() {
            return new ArrayList<AnswerSummary>(this.list);
        }

        /**
         * @see jabara.it_inoculation_questions.model.AnswersStatistics#getAnswerSummaryCount()
         */
        @Override
        public int getAnswerSummaryCount() {
            return this.list.size();
        }

        /**
         * @see jabara.it_inoculation_questions.model.AnswersStatistics#getQuestionMessage()
         */
        @Override
        public String getQuestionMessage() {
            return this.questionMessage;
        }

    }

    /**
     * @author jabaraster
     */
    public static class AnswerSummaryImpl implements AnswerSummary {
        private static final long serialVersionUID = -263957673298582958L;

        private final int         answerCount;
        private final String      selectionMessage;

        /**
         * @param pAnswerCount
         * @param pSelectionMessage
         */
        public AnswerSummaryImpl(final int pAnswerCount, final String pSelectionMessage) {
            this.answerCount = pAnswerCount;
            this.selectionMessage = pSelectionMessage;
        }

        /**
         * @see jabara.it_inoculation_questions.model.AnswerSummary#getAnswerCount()
         */
        @Override
        public int getAnswerCount() {
            return this.answerCount;
        }

        /**
         * @see jabara.it_inoculation_questions.model.AnswerSummary#getSelectionMessage()
         */
        @Override
        public String getSelectionMessage() {
            return this.selectionMessage;
        }

    }

    /**
     * @author jabaraster
     */
    public static class ValueAndCount {
        private final String value;
        private final long   count;

        /**
         * @param pValue
         * @param pCount
         */
        public ValueAndCount(final String pValue, final long pCount) {
            this.value = pValue;
            this.count = pCount;
        }

        /**
         * @return countを返す.
         */
        public long getCount() {
            return this.count;
        }

        /**
         * @return valueを返す.
         */
        public String getValue() {
            return this.value;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            return "ValueAndCount [value=" + this.value + ", count=" + this.count + "]";
        }
    }

}

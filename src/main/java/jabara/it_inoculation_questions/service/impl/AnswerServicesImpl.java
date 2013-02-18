/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.Answer;
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

import java.util.ArrayList;
import java.util.HashMap;
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
     * @see jabara.it_inoculation_questions.service.IAnswersService#decide(jabara.it_inoculation_questions.entity.AnswersSave)
     */
    @Override
    public Answers decide(final AnswersSave pAnswersSave) {
        ArgUtil.checkNull(pAnswersSave, "pAnswersSave"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();

        em.remove(em.merge(pAnswersSave));

        final Answers answers = new Answers();
        for (final Answer answer : pAnswersSave) {
            answers.addAnswer(answer.getQuestionIndex(), answer.getValue());
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
     * @see jabara.it_inoculation_questions.service.IAnswersService#getById(long)
     */
    @Override
    public Answers getById(final long pId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Answers> query = builder.createQuery(Answers.class);
        final Root<Answers> root = query.from(Answers.class);

        query.distinct(true);
        query.where(builder.equal(root.get(EntityBase_.id), Long.valueOf(pId)));

        root.fetch(Answers_.answers, JoinType.LEFT);

        try {
            return em.createQuery(query).getSingleResult();
        } catch (final NoResultException e) {
            throw NotFound.GLOBAL;
        }
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
     * @see jabara.it_inoculation_questions.service.IAnswersService#update(jabara.it_inoculation_questions.entity.Answer)
     */
    @Override
    public void update(final Answer pAnswer) {
        ArgUtil.checkNull(pAnswer, "pAnswer"); //$NON-NLS-1$
        final Answer merged = getEntityManager().merge(pAnswer);
        merged.setQuestionIndex(pAnswer.getQuestionIndex());
        merged.setValue(pAnswer.getValue());
    }

    private Map<String, ValueAndCount> getAnswersByQuestionIndex(final int pQuestionIndex) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ValueAndCount> query = builder.createQuery(ValueAndCount.class);
        final Root<Answer> root = query.from(Answer.class);

        query.select(builder.construct(ValueAndCount.class, root.get(Answer_.value), builder.count(root.get(Answer_.value))));
        query.groupBy(root.get(Answer_.value));

        query.where(builder.equal(root.get(Answer_.questionIndex), Integer.valueOf(pQuestionIndex)));

        final List<ValueAndCount> list = em.createQuery(query).getResultList();
        final Map<String, ValueAndCount> ret = new HashMap<String, AnswerServicesImpl.ValueAndCount>();
        for (final ValueAndCount vc : list) {
            ret.put(vc.getValue(), vc);
        }
        return ret;
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
            return em.createQuery(query).getSingleResult();
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
            final AnswerSummaryImpl summary = new AnswerSummaryImpl(answerCount, selection.getMessage());
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

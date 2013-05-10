/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.Empty;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.QuestionConfiguration;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.QuestionType;
import jabara.it_inoculation_questions.model.Selection;
import jabara.it_inoculation_questions.model.TextQuestionFound;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.it_inoculation_questions.util.QuestionUtil;
import jabara.jpa.JpaDaoBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author jabaraster
 */
@Singleton
public class QuestionServiceImpl extends JpaDaoBase implements IQuestionService {
    private static final long                     serialVersionUID = 2019997759657311819L;

    private final AtomicReference<String>         qaNameHolder     = new AtomicReference<String>(Empty.STRING);
    private final AtomicReference<List<Question>> qaHolder         = new AtomicReference<List<Question>>(Collections.<Question> emptyList());

    /**
     * @param pEntityManagerFactory
     */
    @Inject
    public QuestionServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#findQuestionMessage(int)
     */
    @Override
    public String findQuestionMessage(final int pQuestionIndex) throws NotFound {
        if (pQuestionIndex < 0 || pQuestionIndex >= questions().size()) {
            throw NotFound.GLOBAL;
        }
        return questions().get(pQuestionIndex).getMessage();
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#findSelectionMessage(int, java.lang.String)
     */
    @Override
    public String findSelectionMessage(final int pQuestionIndex, final String pValue) throws NotFound, TextQuestionFound {
        if (pQuestionIndex < 0 || pQuestionIndex >= questions().size()) {
            throw NotFound.GLOBAL;
        }

        final Question question = questions().get(pQuestionIndex);
        if (question.getType() != QuestionType.SELECT) {
            throw TextQuestionFound.GLOBAL;
        }

        for (final Selection selection : question.getSelections()) {
            final String value = selection.getValue();
            if (value == null) {
                throw NotFound.GLOBAL;
            }
            if (value.equals(pValue)) {
                return selection.getMessage();
            }
        }
        throw NotFound.GLOBAL;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#getQaName()
     */
    @Override
    public String getQaName() {
        return this.qaNameHolder.get();
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#getQuestions()
     */
    @Override
    public List<Question> getQuestions() {
        return new ArrayList<Question>(this.qaHolder.get());
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#isQuestionsRegistered()
     */
    @Override
    public boolean isQuestionsRegistered() {
        return isQuestionsRegisteredCore();
    }

    /**
     * DBに設問が登録済みであればそれをロードしてキャッシュする.
     */
    @PostConstruct
    public void postConstruct() {
        final List<QuestionConfiguration> list = getAll();
        if (list.isEmpty()) {
            return;
        }

        final QuestionConfiguration config = list.get(0);
        setToHolders(config);
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#registerQuestion(java.lang.String, java.io.InputStream)
     */
    @Override
    public synchronized void registerQuestion(final String pQaName, final InputStream pQaXmlData) {
        if (isQuestionsRegisteredCore()) {
            return;
        }
        try {
            final QuestionConfiguration config = new QuestionConfiguration();
            config.setQaName(pQaName);
            config.setConfigurationText(toString(pQaXmlData));

            getEntityManager().persist(config);

            setToHolders(config);

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private List<QuestionConfiguration> getAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<QuestionConfiguration> query = builder.createQuery(QuestionConfiguration.class);
        final Root<QuestionConfiguration> root = query.from(QuestionConfiguration.class);
        final List<QuestionConfiguration> list = em.createQuery(query.select(root)).getResultList();
        return list;
    }

    private boolean isQuestionsRegisteredCore() {
        return !questions().isEmpty();
    }

    private List<Question> questions() {
        return this.qaHolder.get();
    }

    private void setToHolders(final QuestionConfiguration pQuestionConfiguration) {
        final List<Question> questions = QuestionUtil.parseQuestions(pQuestionConfiguration.getConfigurationText());
        this.qaNameHolder.set(pQuestionConfiguration.getQaName());
        this.qaHolder.set(questions);
    }

    private static String toString(final InputStream pQaXmlData) throws IOException {
        final ByteArrayOutputStream mem = new ByteArrayOutputStream();
        final byte[] buf = new byte[4096];
        for (int d = pQaXmlData.read(buf); d != -1; d = pQaXmlData.read(buf)) {
            mem.write(buf, 0, d);
        }
        return new String(mem.toByteArray(), Charset.forName("utf-8")); //$NON-NLS-1$
    }
}

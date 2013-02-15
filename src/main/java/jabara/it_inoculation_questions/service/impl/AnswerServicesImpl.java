/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.entity.AnswersSave_;
import jabara.it_inoculation_questions.entity.Answers_;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.jpa.entity.EntityBase_;

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

}

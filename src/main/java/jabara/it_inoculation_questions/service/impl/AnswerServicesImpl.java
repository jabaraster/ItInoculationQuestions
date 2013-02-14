/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.ArgUtil;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.jpa.entity.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * @author jabaraster
 */
public class AnswerServicesImpl implements IAnswersService {

    private final EntityManagerFactory emf;

    /**
     * @param pEmf
     */
    @Inject
    public AnswerServicesImpl(final EntityManagerFactory pEmf) {
        ArgUtil.checkNull(pEmf, "pEmf"); //$NON-NLS-1$
        this.emf = pEmf;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#insertOrUpdate(jabara.it_inoculation_questions.entity.Answers)
     */
    @Override
    public void insertOrUpdate(final Answers pAnswers) {
        ArgUtil.checkNull(pAnswers, "pAnswers"); //$NON-NLS-1$

        if (isPersisted(pAnswers)) {
            updateCore(pAnswers);
        } else {
            insertCore(pAnswers);
        }
    }

    /**
     * @see jabara.it_inoculation_questions.service.IAnswersService#update(jabara.it_inoculation_questions.entity.Answer)
     */
    @Override
    public void update(final Answer pAnswer) {
        ArgUtil.checkNull(pAnswer, "pAnswer"); //$NON-NLS-1$
        final Answer merged = this.emf.createEntityManager().merge(pAnswer);
        merged.setIndex(pAnswer.getIndex());
        merged.setValue(pAnswer.getValue());
    }

    private void insertCore(final Answers pAnswers) {
        this.emf.createEntityManager().persist(pAnswers);
    }

    private void updateCore(final Answers pAnswers) {
        this.emf.createEntityManager().merge(pAnswers);
    }

    private static boolean isPersisted(final EntityBase<?> pEntity) {
        return pEntity.getId() != null;
    }

}

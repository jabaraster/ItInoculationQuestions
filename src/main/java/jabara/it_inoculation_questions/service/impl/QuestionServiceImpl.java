/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.ArgUtil;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Questions;
import jabara.it_inoculation_questions.service.IQuestionService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXB;

/**
 * @author jabaraster
 */
public class QuestionServiceImpl implements IQuestionService {

    private static final String         QUESTIONS_FILE_PATH = "/questions.xml"; //$NON-NLS-1$

    private static final List<Question> QUESTIONS           = loadQuestions();

    private final EntityManagerFactory  emf;

    /**
     * @param pEmf
     */
    @Inject
    public QuestionServiceImpl(final EntityManagerFactory pEmf) {
        ArgUtil.checkNull(pEmf, "pEmf"); //$NON-NLS-1$
        this.emf = pEmf;
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#getQuestions()
     */
    @Override
    public List<Question> getQuestions() {
        return new ArrayList<Question>(QUESTIONS);
    }

    private static List<Question> loadQuestions() {
        return JAXB.unmarshal(QuestionServiceImpl.class.getResource(QUESTIONS_FILE_PATH), Questions.class).getQuestions();
    }
}

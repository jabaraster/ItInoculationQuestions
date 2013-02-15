/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.it_inoculation_questions.util.QuestionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jabaraster
 */
public class QuestionServiceImpl implements IQuestionService {

    private static final String         QUESTIONS_FILE_PATH = "/questions.xml"; //$NON-NLS-1$

    private static final List<Question> QUESTIONS           = loadQuestions();

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#getQuestions()
     */
    @Override
    public List<Question> getQuestions() {
        return new ArrayList<Question>(QUESTIONS);
    }

    private static List<Question> loadQuestions() {
        return QuestionUtil.loadQuestions(QuestionServiceImpl.class.getResource(QUESTIONS_FILE_PATH));
    }
}

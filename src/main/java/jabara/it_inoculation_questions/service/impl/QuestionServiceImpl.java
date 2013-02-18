/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import jabara.general.NotFound;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.QuestionType;
import jabara.it_inoculation_questions.model.Selection;
import jabara.it_inoculation_questions.model.TextQuestionFound;
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
     * @see jabara.it_inoculation_questions.service.IQuestionService#findQuestionMessage(int)
     */
    @Override
    public String findQuestionMessage(final int pQuestionIndex) throws NotFound {
        if (pQuestionIndex < 0 || pQuestionIndex >= QUESTIONS.size()) {
            throw NotFound.GLOBAL;
        }
        return QUESTIONS.get(pQuestionIndex).getMessage();
    }

    /**
     * @see jabara.it_inoculation_questions.service.IQuestionService#findSelectionMessage(int, java.lang.String)
     */
    @Override
    public String findSelectionMessage(final int pQuestionIndex, final String pValue) throws NotFound, TextQuestionFound {
        if (pQuestionIndex < 0 || pQuestionIndex >= QUESTIONS.size()) {
            throw NotFound.GLOBAL;
        }

        final Question question = QUESTIONS.get(pQuestionIndex);
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

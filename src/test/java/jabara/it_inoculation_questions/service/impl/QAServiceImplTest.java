package jabara.it_inoculation_questions.service.impl;

import jabara.it_inoculation_questions.model.DI;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.service.IQuestionService;

import org.junit.Test;

/**
 * @author jabaraster
 */
public class QAServiceImplTest {

    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    public void testGetQuestions() {
        for (final Question q : DI.get(IQuestionService.class).getQuestions()) {
            System.out.println(q);
        }
    }

}

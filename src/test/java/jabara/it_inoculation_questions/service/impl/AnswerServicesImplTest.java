/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.model.DI;
import jabara.it_inoculation_questions.service.IAnswersService;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class AnswerServicesImplTest {

    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    @Ignore
    public void _getAnswersStatistics() {
        DI.get(IAnswersService.class).getAnswersStatistics();
    }

    /**
     * 
     */
    @SuppressWarnings("static-method")
    @Test
    @Ignore
    public void _getByKey() {
        final String key = "hoge"; //$NON-NLS-1$
        final int questionsCount = 10;

        final AnswersSave answer = DI.get(IAnswersService.class).getSavedByKey(key, questionsCount);

        assertEquals(key, answer.getKey());
        assertNotNull(answer.getId());
        assertEquals(questionsCount, answer.getAnswersCount());
    }

    /**
     * 
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void _update() {
        final IAnswersService sut = DI.get(IAnswersService.class);
        final AnswersSave save = sut.getSavedByKey("key", 10);
        final Answer answer = save.getAnswer(0);

        answer.getValues().add(new AnswerValue("99", "Option Text"));
        sut.update(answer);
        jabara.Debug.write(answer.getValues().get(0).getId());

        answer.getValues().get(0).setOptionText("Option Text 2");
        sut.update(answer);
        jabara.Debug.write(answer.getValues().get(0).getId());
    }

}

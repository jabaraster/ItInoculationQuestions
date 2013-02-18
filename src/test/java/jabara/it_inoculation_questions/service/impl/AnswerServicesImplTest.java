/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

}

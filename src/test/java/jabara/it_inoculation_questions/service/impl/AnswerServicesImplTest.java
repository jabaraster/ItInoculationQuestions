/**
 * 
 */
package jabara.it_inoculation_questions.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import jabara.general.IoUtil;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.model.DI;
import jabara.it_inoculation_questions.service.IAnswersService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.wicket.util.io.IOUtils;
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
     * @throws IOException -
     */
    @SuppressWarnings({ "static-method", "nls", "resource" })
    @Test
    public void _makeAnswersCsv() throws IOException {
        final File file = DI.get(IAnswersService.class).makeAnswersCsv();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")));
        try {
            final String s = IOUtils.toString(reader);
            System.out.println(s);
        } finally {
            IoUtil.close(reader);
            file.delete();
        }
    }

    /**
     * 
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    @Ignore
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

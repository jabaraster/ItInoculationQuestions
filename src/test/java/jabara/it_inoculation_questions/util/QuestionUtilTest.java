package jabara.it_inoculation_questions.util;

import jabara.it_inoculation_questions.model.InvalidQuestionSettingException;

import org.junit.Test;

/**
 * @author jabaraster
 */
public class QuestionUtilTest {

    /**
     * @throws Exception -
     */
    @SuppressWarnings("static-method")
    @Test(expected = InvalidQuestionSettingException.class)
    public void _E01_loadQuestions_select_error() throws Exception {
        try {
            QuestionUtil.loadQuestions(QuestionUtilTest.class.getResource("error_questions.xml")); //$NON-NLS-1$
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

}

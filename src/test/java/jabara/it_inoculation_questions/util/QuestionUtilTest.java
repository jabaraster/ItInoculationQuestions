package jabara.it_inoculation_questions.util;

import static org.junit.Assert.assertEquals;
import jabara.general.Empty;
import jabara.it_inoculation_questions.model.InvalidQuestionSettingException;
import jabara.it_inoculation_questions.model.TextAnswerColumn;

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
    public void _loadQuestions_select_error() throws Exception {
        try {
            QuestionUtil.loadQuestions(QuestionUtilTest.class.getResource("error_questions.xml")); //$NON-NLS-1$
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public void _parseTextAnswerColumn() {
        assertEquals(new TextAnswerColumn(Empty.STRING, Empty.STRING), QuestionUtil.parseTextAnswerColumn(null));
        assertEquals(new TextAnswerColumn(Empty.STRING, Empty.STRING), QuestionUtil.parseTextAnswerColumn(Empty.STRING));
        assertEquals(new TextAnswerColumn("a", "b"), QuestionUtil.parseTextAnswerColumn("a${value}b"));
        assertEquals(new TextAnswerColumn("a", Empty.STRING), QuestionUtil.parseTextAnswerColumn("a${value}"));
        assertEquals(new TextAnswerColumn(Empty.STRING, "b"), QuestionUtil.parseTextAnswerColumn("${value}b"));
        assertEquals(new TextAnswerColumn(Empty.STRING, Empty.STRING), QuestionUtil.parseTextAnswerColumn("${value}"));
    }

    /**
     * @throws Exception -
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public void _parseTextAnswerColumn_parse_error() throws Exception {
        try {
            QuestionUtil.parseTextAnswerColumn("ab");
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}

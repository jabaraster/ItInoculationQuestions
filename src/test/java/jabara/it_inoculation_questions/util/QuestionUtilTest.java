package jabara.it_inoculation_questions.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import jabara.general.Empty;
import jabara.it_inoculation_questions.model.InvalidQuestionSettingException;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.QuestionType;
import jabara.it_inoculation_questions.model.Selection;
import jabara.it_inoculation_questions.model.TextAnswerColumn;

import java.util.List;

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
    @SuppressWarnings({ "boxing", "static-method" })
    @Test
    public void _optionTextのある設問のパースをテスト() {
        final List<Question> questions = QuestionUtil.loadQuestions(QuestionUtilTest.class.getResource("questions_with_optionText.xml")); //$NON-NLS-1$

        final Selection selection = questions.get(0).getSelections().get(0);
        final List<String> optionTexts = selection.getOptionTexts();
        final int actual = optionTexts.size();
        assertThat(actual, is(3));
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

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "nls", "boxing" })
    @Test
    public void _正常ケース() {
        final List<Question> questions = QuestionUtil.loadQuestions(QuestionUtilTest.class.getResource("valid_questions.xml")); //$NON-NLS-1$

        Question q;

        q = questions.get(0);
        assertThat(q.getType(), is(QuestionType.SELECT));
        assertThat(q.getMessage(), is("お客様についてお聞かせ下さい。"));
        assertThat(q.getSelections().size(), is(5));
        assertThat(q.getSelections().get(0).getLabel(), is("学校関係者様：法人経営者"));

        q = questions.get(1);
        assertThat(q.getType(), is(QuestionType.MULTI_SELECT));

        q = questions.get(2);
        assertThat(q.getType(), is(QuestionType.TEXT));

        q = questions.get(3);
        assertThat(q.getType(), is(QuestionType.SELECT_WITH_TEXT));

        q = questions.get(4);
        assertThat(q.getType(), is(QuestionType.TEXTAREA));
    }
}

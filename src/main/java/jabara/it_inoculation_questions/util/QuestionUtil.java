/**
 * 
 */
package jabara.it_inoculation_questions.util;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.it_inoculation_questions.model.InvalidQuestionSettingException;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Questions;
import jabara.it_inoculation_questions.model.TextAnswerColumn;

import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

/**
 * @author jabaraster
 */
public final class QuestionUtil {

    private QuestionUtil() {
        // 処理なし
    }

    /**
     * XML形式のアンケート設定ファイルを読み込みます.
     * 
     * @param pXmlLocation XMLファイルの位置.
     * @return 読み込み結果.
     */
    public static List<Question> loadQuestions(final URL pXmlLocation) {
        final List<Question> ret = JAXB.unmarshal(pXmlLocation, Questions.class).getQuestions();
        checkQuestions(ret);
        return ret;
    }

    /**
     * @param pXml
     * @return -
     */
    public static List<Question> parseQuestions(final String pXml) {
        ArgUtil.checkNullOrEmpty(pXml, "pXml"); //$NON-NLS-1$
        final List<Question> ret = JAXB.unmarshal(new StringReader(pXml), Questions.class).getQuestions();
        checkQuestions(ret);
        return ret;
    }

    /**
     * @param pDefinition ${value}を含む文字列.
     * @return 入力欄前後の文字列.
     */
    public static TextAnswerColumn parseTextAnswerColumn(final String pDefinition) {
        if (pDefinition == null || pDefinition.length() == 0) {
            return new TextAnswerColumn(Empty.STRING, Empty.STRING);
        }

        final Matcher matcher = Pattern.compile("(.*)\\$\\{value\\}(.*)").matcher(pDefinition); //$NON-NLS-1$
        if (!matcher.find()) {
            throw new IllegalArgumentException("文字列に${value}を含める必要があります."); //$NON-NLS-1$
        }
        return new TextAnswerColumn(matcher.group(1), matcher.group(2));
    }

    private static void checkQuestion(final Question pQuestion) throws CheckError {
        switch (pQuestion.getType()) {
        case SELECT:
            checkSelectionCountIsGeOne(pQuestion);
            break;
        case MULTI_SELECT:
            checkSelectionCountIsGeOne(pQuestion);
            break;
        case TEXT:
            checkSelectionCountIsZeroOrOne(pQuestion);
            break;
        case TEXTAREA:
            checkSelectionCountIsZeroOrOne(pQuestion);
            break;
        case SELECT_WITH_TEXT:
            checkSelectionCountIsGeOne(pQuestion);
            break;
        default:
            throw new IllegalStateException();
        }
    }

    private static void checkQuestions(final List<Question> pQuestions) {
        final InvalidQuestionSettingException errors = new InvalidQuestionSettingException();
        for (final Question q : pQuestions) {
            try {
                checkQuestion(q);
            } catch (final CheckError e) {
                errors.addError(q, e.getMessage());
            }
        }
        if (errors.getErrorCount() > 0) {
            throw errors;
        }
    }

    private static void checkSelectionCountIsGeOne(final Question pQuestion) throws CheckError {
        if (pQuestion.getSelections().size() < 2) {
            throw new CheckError("タイプが " + pQuestion.getType() + " の設問には少なくとも２つの選択肢が必要です."); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void checkSelectionCountIsZeroOrOne(final Question pQuestion) throws CheckError {
        if (pQuestion.getSelections().size() > 1) {
            throw new CheckError("タイプが " + pQuestion.getType() + " の設問には２つ以上の選択肢は指定出来ません."); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

    private static class CheckError extends Exception {
        private static final long serialVersionUID = 665023247411535771L;

        CheckError(final String pMessage) {
            super(pMessage);
        }
    }

}

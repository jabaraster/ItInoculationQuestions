/**
 * 
 */
package jabara.it_inoculation_questions.util;

import jabara.it_inoculation_questions.model.InvalidQuestionSettingException;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Questions;

import java.net.URL;
import java.util.List;

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

    private static void checkQuestion(final Question pQuestion) throws CheckError {
        switch (pQuestion.getType()) {
        case SELECT:
            checkSelectionCountIsGeOne(pQuestion);
            break;
        case TEXT:
            checkSelectionCountIsZeroOrOne(pQuestion);
            break;
        case TEXTAREA:
            checkSelectionCountIsZeroOrOne(pQuestion);
            break;
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

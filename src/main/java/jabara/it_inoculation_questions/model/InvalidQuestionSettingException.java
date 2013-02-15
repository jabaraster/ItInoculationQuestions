/**
 * 
 */
package jabara.it_inoculation_questions.model;

import jabara.general.ArgUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jabaraster
 */
public class InvalidQuestionSettingException extends RuntimeException {

    private static final long         serialVersionUID = -6671418537899187576L;

    private final List<QuestionError> errors           = new ArrayList<InvalidQuestionSettingException.QuestionError>();

    /**
     * 
     */
    public InvalidQuestionSettingException() {
        // 処理なし
    }

    /**
     * @param pQuestion エラーのあった設問.
     * @param pErrorMessage エラーメッセージ.
     */
    public void addError(final Question pQuestion, final String pErrorMessage) {
        ArgUtil.checkNull(pQuestion, "pQuestion"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pErrorMessage, "pErrorMessage"); //$NON-NLS-1$
        this.errors.add(new QuestionError(pQuestion, pErrorMessage));
    }

    /**
     * @return エラーの数.
     */
    public int getErrorCount() {
        return this.errors.size();
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        for (final QuestionError qe : this.errors) {
            sb.append("設問 '").append(qe.getErrorQuestion().getMessage()).append("' に設定ミスがあります. ").append(qe.getErrorMessage()); //$NON-NLS-1$//$NON-NLS-2$
            sb.append("\n"); //$NON-NLS-1$
        }
        return new String(sb);
    }

    /**
     * @author jabaraster
     */
    public static class QuestionError implements Serializable {
        private static final long serialVersionUID = 8799224810639414648L;

        private final Question    errorQuestion;
        private final String      errorMessage;

        /**
         * @param pErrorQuestion エラーが起きた設問.
         * @param pErrorMessage エラーメッセージ.
         */
        public QuestionError(final Question pErrorQuestion, final String pErrorMessage) {
            this.errorQuestion = pErrorQuestion;
            this.errorMessage = pErrorMessage;
        }

        /**
         * @return the errorMessage
         */
        public String getErrorMessage() {
            return this.errorMessage;
        }

        /**
         * @return the errorQuestion
         */
        public Question getErrorQuestion() {
            return this.errorQuestion;
        }

    }
}

/**
 * 
 */
package jabara.it_inoculation_questions;

/**
 * 
 * @author jabaraster
 */
public final class ItInoculationQuestionsEnv {

    /**
     * 
     */
    public static final String APPLICATION_NAME                       = "ItInoculationQuestions";                            //$NON-NLS-1$

    /**
     * 
     */
    public static final String APPLICATION_NAME_JAVA_CONSTANTS_FORMAT = "IT_INOCULATION_QUESTIONS";                          //$NON-NLS-1$

    /**
     * 
     */
    public static final String APPLICATION_NAME_IN_JAPANESE           = "IT予防接種アンケート";                                       //$NON-NLS-1$

    /**
     * 
     */
    public static final String ENV_PASSWORD                           = APPLICATION_NAME_JAVA_CONSTANTS_FORMAT + "_PASSWORD"; //$NON-NLS-1$

    private ItInoculationQuestionsEnv() {
        // 処理なし
    }

    /**
     * @return ログインパスワード.
     */
    public static String getPassword() {
        final String p = System.getenv(ENV_PASSWORD);
        return p == null ? "password" : p; //$NON-NLS-1$
    }
}

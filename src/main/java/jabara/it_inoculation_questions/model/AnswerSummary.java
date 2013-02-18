/**
 * 
 */
package jabara.it_inoculation_questions.model;

import java.io.Serializable;

/**
 * @author jabaraster
 */
public interface AnswerSummary extends Serializable {

    /**
     * @return 回答数.
     */
    int getAnswerCount();

    /**
     * @return 選択肢文.
     */
    String getSelectionMessage();
}

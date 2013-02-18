/**
 * 
 */
package jabara.it_inoculation_questions.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jabaraster
 */
public interface AnswersStatistics extends Serializable {

    /**
     * @return 回答サマリー.
     */
    List<AnswerSummary> getAnswerSummaries();

    /**
     * @return 選択肢数.
     */
    int getAnswerSummaryCount();

    /**
     * @return 設問.
     */
    String getQuestionMessage();
}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;

import java.util.List;

import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerMultiSelectWithTextPanel extends InputPanel {
    private static final long serialVersionUID = 3569774264912981889L;

    /**
     * @param pId
     * @param pQuestion
     * @param pAnswerValuesModel
     * @param pAnswerValueChangeListener
     */
    public AnswerMultiSelectWithTextPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final IAjaxListener pAnswerValueChangeListener) {
        super(pId, pAnswerValueChangeListener);
    }

}

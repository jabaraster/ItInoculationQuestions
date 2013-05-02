/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;

import java.util.List;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerTextFieldPanel extends TextComponentPanel<TextField<List<String>>> {
    private static final long serialVersionUID = -3447325264244103488L;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValueModel 回答を格納するモデル.
     */
    public AnswerTextFieldPanel(final String pId, final Question pQuestion, final IModel<List<String>> pAnswerValueModel) {
        super(pId, pQuestion, pAnswerValueModel);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.TextComponentPanel#createAnswerText()
     */
    @Override
    protected TextField<List<String>> createAnswerText() {
        return new TextField<List<String>>("answerText", this.answerValueModel); //$NON-NLS-1$
    }
}

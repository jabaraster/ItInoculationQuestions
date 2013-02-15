/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerTextFieldPanel extends TextComponentPanel<TextField<String>> {
    private static final long serialVersionUID = -3447325264244103488L;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValueModel 回答を格納するモデル.
     */
    public AnswerTextFieldPanel(final String pId, final Question pQuestion, final IModel<String> pAnswerValueModel) {
        super(pId, pQuestion, pAnswerValueModel);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.TextComponentPanel#createAnswerText()
     */
    @Override
    protected TextField<String> createAnswerText() {
        return new TextField<String>("answerText", this.answerValueModel); //$NON-NLS-1$
    }

}

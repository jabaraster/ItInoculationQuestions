/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerTextAreaPanel extends InputPanel {
    private static final long    serialVersionUID = -3447325264244103488L;

    private final Question       question;
    private final IModel<String> answerValueModel;

    private TextArea<String>     answerText;

    /**
     * @param pId
     * @param pQuestion
     * @param pAnswerValueModel
     */
    public AnswerTextAreaPanel(final String pId, final Question pQuestion, final IModel<String> pAnswerValueModel) {
        super(pId);
        this.question = pQuestion;
        this.answerValueModel = pAnswerValueModel;
        this.add(getAnswerText());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
     */
    @Override
    public FormComponent<?> getInputComponent() {
        return getAnswerText();
    }

    private TextArea<String> getAnswerText() {
        if (this.answerText == null) {
            this.answerText = new TextArea<String>("answerText", this.answerValueModel); //$NON-NLS-1$

        }
        return this.answerText;
    }

}

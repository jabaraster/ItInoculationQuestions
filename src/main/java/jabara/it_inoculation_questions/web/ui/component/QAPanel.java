/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class QAPanel extends InputPanel {
    private static final long               serialVersionUID = -1685283113094374805L;

    private final Question                  question;
    private final IModel<List<AnswerValue>> answerValuesModel;
    private final int                       index;

    private InputPanel                      answerInputPanel;

    private Label                           message;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValuesModel 回答を格納するモデル.
     * @param pIndex 何番目の設問かを示す値. 0始まり.
     * @param pListener -
     */
    public QAPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final int pIndex //
            , final IAjaxListener pListener) {
        super(pId, pListener);

        this.question = pQuestion;
        this.answerValuesModel = pAnswerValuesModel;
        this.index = pIndex;

        this.add(getMessage());
        this.add(getAnswerInputPanel());
    }

    // /**
    // * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
    // */
    // @Override
    // public FormComponent<?> getInputComponent() {
    // return this.answerInputPanel.getInputComponent();
    // }

    private InputPanel getAnswerInputPanel() {
        if (this.answerInputPanel == null) {
            final String id = "answer"; //$NON-NLS-1$

            switch (this.question.getType()) {
            case SELECT:
                this.answerInputPanel = new AnswerSelectPanel(id, this.question, this.answerValuesModel, this.answerValueChangeListener);
                break;
            case MULTI_SELECT:
                this.answerInputPanel = new AnswerMultiSelectPanel(id, this.question, this.answerValuesModel, this.answerValueChangeListener);
                break;
            case TEXT:
                this.answerInputPanel = new AnswerTextFieldPanel(id, this.question, this.answerValuesModel, this.answerValueChangeListener);
                break;
            case TEXTAREA:
                this.answerInputPanel = new AnswerTextAreaPanel(id, this.question, this.answerValuesModel, this.answerValueChangeListener);
                break;
            default:
                throw new IllegalStateException(this.question.getType() + " is unsupported."); //$NON-NLS-1$
            }
        }
        return this.answerInputPanel;
    }

    @SuppressWarnings("nls")
    private Label getMessage() {
        if (this.message == null) {
            this.message = new Label("message", new AbstractReadOnlyModel<String>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public String getObject() {
                    return "Q" + (QAPanel.this.index + 1) + ") " + QAPanel.this.question.getMessage();
                }
            });
        }
        return this.message;
    }

}

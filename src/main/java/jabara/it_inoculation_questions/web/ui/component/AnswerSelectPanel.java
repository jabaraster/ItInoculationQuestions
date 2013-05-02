/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerSelectPanel extends InputPanel {
    private static final long          serialVersionUID = 6209600276674562508L;

    private final Question             question;
    private final IModel<List<String>> answerValueModel;

    private DropDownChoice<Selection>  selection;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValueModel 回答を格納するモデル.
     */
    public AnswerSelectPanel(final String pId, final Question pQuestion, final IModel<List<String>> pAnswerValueModel) {
        super(pId);
        this.question = pQuestion;
        this.answerValueModel = pAnswerValueModel;
        this.add(getSelection());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
     */
    @Override
    public FormComponent<?> getInputComponent() {
        return getSelection();
    }

    @SuppressWarnings("synthetic-access")
    private DropDownChoice<Selection> getSelection() {
        if (this.selection == null) {
            this.selection = new DropDownChoice<Selection>( //
                    "selection" // //$NON-NLS-1$
                    , new AnswerModel() //
                    , this.question.getSelections() //
                    , new SelectionChoiceRenderer() //
            );
            this.selection.setRequired(this.question.isRequired());
        }
        return this.selection;
    }

    private class AnswerModel implements IModel<Selection> {
        private static final long serialVersionUID = -4290415428966921826L;

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public Selection getObject() {
            for (final Selection s : AnswerSelectPanel.this.question.getSelections()) {
                for (final String v : AnswerSelectPanel.this.answerValueModel.getObject()) {
                    if (s.getValue().equals(v)) {
                        return s;
                    }
                }
            }
            return null;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final Selection pObject) {
            final List<String> values = AnswerSelectPanel.this.answerValueModel.getObject();
            values.clear();
            values.add(pObject.getValue());
        }
    }
}

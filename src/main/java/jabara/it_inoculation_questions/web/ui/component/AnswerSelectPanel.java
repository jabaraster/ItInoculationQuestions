/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerSelectPanel extends InputPanel {
    private static final long         serialVersionUID = 6209600276674562508L;

    private final Question            question;
    private final IModel<String>      answerValueModel;

    private DropDownChoice<Selection> selection;

    /**
     * @param pId
     * @param pQuestion
     * @param pAnswerValueModel
     */
    public AnswerSelectPanel(final String pId, final Question pQuestion, final IModel<String> pAnswerValueModel) {
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
                if (s.getValue().equals(AnswerSelectPanel.this.answerValueModel.getObject())) {
                    return s;
                }
            }
            return null;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final Selection pObject) {
            AnswerSelectPanel.this.answerValueModel.setObject(pObject == null ? null : pObject.getValue());
        }
    }

    private static class SelectionChoiceRenderer implements IChoiceRenderer<Selection> {
        private static final long serialVersionUID = 2100614335551258343L;

        @Override
        public Object getDisplayValue(final Selection pObject) {
            return pObject.getMessage();
        }

        @Override
        public String getIdValue(final Selection pObject, @SuppressWarnings("unused") final int pIndex) {
            return pObject.getValue();
        }

    }
}

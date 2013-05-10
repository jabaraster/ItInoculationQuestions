/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.general.Empty;
import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerSelectPanel extends InputPanel {
    private static final long               serialVersionUID = 6209600276674562508L;

    private final Question                  question;
    private final IModel<List<AnswerValue>> answerValuesModel;

    private DropDownChoice<Selection>       selection;
    private TextField<String>               other;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValuesModel 回答を格納するモデル.
     * @param pListener -
     */
    public AnswerSelectPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final IAjaxListener pListener) {
        super(pId, pListener);
        this.question = pQuestion;
        this.answerValuesModel = pAnswerValuesModel;
        this.add(getSelection());
        this.add(getOther());
    }

    @SuppressWarnings({ "synthetic-access", "serial" })
    private TextField<String> getOther() {
        if (this.other == null) {
            this.other = new TextField<String>("other", new AnswerTextModel()) { //$NON-NLS-1$
                @Override
                public boolean isVisible() {
                    return isOtherSelected();
                }
            };

            this.other.add(AttributeModifier.append("maxlength", Integer.valueOf(AnswerValue.MAX_CHAR_COUNT_OPTION_TEXT))); //$NON-NLS-1$

            this.other.setOutputMarkupPlaceholderTag(true);
            this.other.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(final AjaxRequestTarget pTarget) {
                    on_other_onUpdate(pTarget);
                }
            });
        }
        return this.other;
    }

    @SuppressWarnings({ "synthetic-access", "serial" })
    private DropDownChoice<Selection> getSelection() {
        if (this.selection == null) {
            this.selection = new DropDownChoice<Selection>( //
                    "selection" // //$NON-NLS-1$
                    , new AnswerModel() //
                    , this.question.getSelections() //
                    , new SelectionChoiceRenderer() //
            );
            this.selection.setRequired(this.question.isRequired());
            this.selection.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(final AjaxRequestTarget pTarget) {
                    on_selection_onUpdate(pTarget);
                }
            });
        }
        return this.selection;
    }

    private boolean isOtherSelected() {
        final Selection selectedValue = getSelection().getModelObject();
        if (selectedValue == null) {
            return false;
        }
        return selectedValue.isOther();
    }

    private void on_other_onUpdate(final AjaxRequestTarget pTarget) {
        // final List<AnswerValue> values = this.answerValuesModel.getObject();
        // if (values.isEmpty()) {
        // return;
        // }
        //
        // final String optionText = getOther().getModelObject();
        // values.get(0).setOptionText(optionText);
        fireAnswerValueChanged(pTarget);
    }

    private void on_selection_onUpdate(final AjaxRequestTarget pTarget) {
        pTarget.add(getOther());
        if (getOther().isVisible()) {
            pTarget.focusComponent(getOther());
        }
        fireAnswerValueChanged(pTarget);
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
                for (final AnswerValue v : AnswerSelectPanel.this.answerValuesModel.getObject()) {
                    if (s.getValue().equals(v.getValue())) {
                        return s;
                    }
                }
            }
            return null;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final Selection pObject) {
            final List<AnswerValue> values = new ArrayList<AnswerValue>();
            final String optionText = pObject.isOther() ? getOther().getModelObject() : null;
            values.add(new AnswerValue(pObject.getValue(), optionText));
            AnswerSelectPanel.this.answerValuesModel.setObject(values);
        }
    }

    private class AnswerTextModel implements IModel<String> {
        private static final long serialVersionUID = -8296551924907778630L;

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public String getObject() {
            final List<AnswerValue> l = AnswerSelectPanel.this.answerValuesModel.getObject();
            return l.isEmpty() ? Empty.STRING : l.get(0).getOptionText();
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final String pObject) {
            final Selection selectedValue = getSelection().getModelObject();
            if (selectedValue == null) {
                return;
            }
            AnswerSelectPanel.this.answerValuesModel.setObject(Arrays.asList(new AnswerValue(selectedValue.getValue(), pObject)));
        }
    }
}

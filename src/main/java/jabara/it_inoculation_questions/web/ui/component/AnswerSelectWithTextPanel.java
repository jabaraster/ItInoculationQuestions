/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerSelectWithTextPanel extends InputPanel {
    private static final long               serialVersionUID = 3569774264912981889L;

    private final Question                  question;
    private final IModel<List<AnswerValue>> answerValuesModel;

    private WebMarkupContainer              selectionsContainer;
    private ListView<Selection>             selections;
    private RadioGroup<?>                   radioGroup;

    /**
     * @param pId
     * @param pQuestion
     * @param pAnswerValuesModel
     * @param pAnswerValueChangeListener
     */
    public AnswerSelectWithTextPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final IAjaxListener pAnswerValueChangeListener) {

        super(pId, pAnswerValueChangeListener);

        this.question = pQuestion;
        this.answerValuesModel = pAnswerValuesModel;

        this.add(getRadioGroup());
    }

    @SuppressWarnings({ "serial", "synthetic-access" })
    private RadioGroup<?> getRadioGroup() {
        if (this.radioGroup == null) {
            this.radioGroup = new RadioGroup<String>("radioGroup", new AnswerRadioGroupModel()); //$NON-NLS-1$
            this.radioGroup.add(getSelectionsContainer());
            this.radioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                @Override
                protected void onUpdate(final AjaxRequestTarget pTarget) {
                    fireAnswerValueChanged(pTarget);
                    pTarget.add(getSelectionsContainer());
                }
            });
        }
        return this.radioGroup;
    }

    @SuppressWarnings("serial")
    private ListView<Selection> getSelections() {
        if (this.selections == null) {
            this.selections = new ListView<Selection>("selections", this.question.getSelections()) { //$NON-NLS-1$
                @SuppressWarnings({ "synthetic-access", "nls" })
                @Override
                protected void populateItem(final ListItem<Selection> pItem) {
                    final Selection selection = pItem.getModelObject();
                    final List<AnswerValue> values = AnswerSelectWithTextPanel.this.answerValuesModel.getObject();
                    final AnswerValueModel m = new AnswerValueModel(selection, values);
                    pItem.add(new RadioWithTextPanel("input", selection, m, new IAjaxListener() {
                        @Override
                        public void handle(final AjaxRequestTarget pTarget) {
                            fireAnswerValueChanged(pTarget);
                        }
                    }));
                }
            };
        }
        return this.selections;
    }

    private WebMarkupContainer getSelectionsContainer() {
        if (this.selectionsContainer == null) {
            this.selectionsContainer = new WebMarkupContainer("selectionsContainer"); //$NON-NLS-1$
            this.selectionsContainer.setOutputMarkupId(true);
            this.selectionsContainer.add(getSelections());
        }
        return this.selectionsContainer;
    }

    private class AnswerRadioGroupModel implements IModel<String> {
        private static final long serialVersionUID = -2897172287127048828L;

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public String getObject() {
            final List<AnswerValue> values = AnswerSelectWithTextPanel.this.answerValuesModel.getObject();
            return values.isEmpty() ? null : values.get(0).getValue();
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final String pObject) {
            AnswerSelectWithTextPanel.this.answerValuesModel.setObject(Arrays.asList(new AnswerValue(pObject)));
        }
    }

    private class AnswerValueModel implements IModel<AnswerValue> {
        private static final long serialVersionUID = 4341464283861200173L;

        private final AnswerValue value;

        AnswerValueModel(final Selection pSelection, final List<AnswerValue> pValues) {
            if (pValues.isEmpty()) {
                this.value = new AnswerValue(pSelection.getValue());
            } else if (pSelection.getValue().equals(pValues.get(0).getValue())) {
                this.value = pValues.get(0);
            } else {
                this.value = new AnswerValue(pSelection.getValue());
            }
        }

        @Override
        public void detach() {
            // 処理なし
        }

        @Override
        public AnswerValue getObject() {
            return this.value;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final AnswerValue pObject) {
            AnswerSelectWithTextPanel.this.answerValuesModel.setObject(Arrays.asList(pObject));
        }
    }

}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.general.ArgUtil;
import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Selection;
import jabara.it_inoculation_questions.model.TextAnswerColumn;
import jabara.it_inoculation_questions.util.QuestionUtil;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class RadioWithTextPanel extends Panel {
    private static final long         serialVersionUID = -234517856324929115L;

    private final Selection           selection;
    private final IModel<AnswerValue> answerValueModel;
    private final IAjaxListener       onChange;

    private Radio<?>                  radio;
    private FormComponentLabel        radioLabel;
    private ListView<String>          optionText;

    /**
     * @param pId -
     * @param pSelection -
     * @param pAnswerValueModel -
     * @param pOnChange -
     */
    public RadioWithTextPanel( //
            final String pId //
            , final Selection pSelection //
            , final IModel<AnswerValue> pAnswerValueModel //
            , final IAjaxListener pOnChange //
    ) {
        super(pId);

        ArgUtil.checkNull(pSelection, "pSelection"); //$NON-NLS-1$
        ArgUtil.checkNull(pAnswerValueModel, "pAnswerValueModel"); //$NON-NLS-1$
        ArgUtil.checkNull(pOnChange, "pOnChange"); //$NON-NLS-1$
        this.selection = pSelection;
        this.answerValueModel = pAnswerValueModel;
        this.onChange = pOnChange;

        this.add(getRadio());
        this.add(getRadioLabel());
        this.add(getOptionText());
    }

    @SuppressWarnings("serial")
    private ListView<?> getOptionText() {
        if (this.optionText == null) {
            this.optionText = new ListView<String>("optionText", this.selection.getOptionTexts()) { //$NON-NLS-1$
                @Override
                public boolean isVisible() {
                    return true;
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void populateItem(final ListItem<String> pItem) {

                    final List<String> l = RadioWithTextPanel.this.answerValueModel.getObject().getOptionTexts();
                    final IModel<String> m = new ListModel<String>(pItem.getIndex(), l);
                    final TextField<String> text = new TextField<String>("text", m, String.class); //$NON-NLS-1$
                    pItem.add(text);
                    text.add(new OnChangeAjaxBehavior() {
                        @Override
                        protected void onUpdate(final AjaxRequestTarget pTarget) {
                            RadioWithTextPanel.this.onChange.handle(pTarget);
                        }
                    });

                    final TextAnswerColumn c = QuestionUtil.parseTextAnswerColumn(pItem.getModelObject());
                    pItem.add(new Label("valuePrefix", c.getValuePrefix())); //$NON-NLS-1$
                    pItem.add(new Label("valueSuffix", c.getValueSuffix())); //$NON-NLS-1$
                }
            };
            this.optionText.setOutputMarkupPlaceholderTag(true);
        }
        return this.optionText;
    }

    @SuppressWarnings("synthetic-access")
    private Radio<?> getRadio() {
        if (this.radio == null) {
            this.radio = new Radio<String>("radio", new RadioValueModel()); //$NON-NLS-1$
        }
        return this.radio;
    }

    private FormComponentLabel getRadioLabel() {
        if (this.radioLabel == null) {
            this.radioLabel = new FormComponentLabel("radioLabel", getRadio()); //$NON-NLS-1$
            this.radioLabel.add(new Label("label", this.selection.getLabel())); //$NON-NLS-1$
        }
        return this.radioLabel;
    }

    private static class ListModel<E> implements IModel<E> {
        private static final long serialVersionUID = -1433673186031321627L;

        private final int         index;
        private final List<E>     list;

        ListModel(final int pIndex, final List<E> pList) {
            this.index = pIndex;
            this.list = pList;
        }

        @Override
        public void detach() {
            // 処理なし
        }

        @Override
        public E getObject() {
            expandListIfNecessity();
            return this.list.get(this.index);
        }

        @Override
        public void setObject(final E pObject) {
            expandListIfNecessity();
            this.list.set(this.index, pObject);
        }

        private void expandListIfNecessity() {
            for (int i = this.list.size(); i <= this.index; i++) {
                this.list.add(null);
            }
        }
    }

    private class RadioValueModel implements IModel<String> {
        private static final long serialVersionUID = 1128870593532478208L;

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public String getObject() {
            final String value = RadioWithTextPanel.this.answerValueModel.getObject().getValue();
            return value;
        }

        @Override
        public void setObject(@SuppressWarnings("unused") final String pObject) {
            throw new IllegalStateException();
        }
    }
}

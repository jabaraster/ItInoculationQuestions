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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class AnswerMultiSelectPanel extends InputPanel {
    private static final long               serialVersionUID = 8239144123893439073L;

    private final Question                  question;
    private final IModel<List<AnswerValue>> answerValuesModel;

    private ListMultipleChoice<Selection>   selections;
    private TextField<String>               other;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValuesModel 回答を格納するモデル.
     * @param pAnswerValueChangeListener -
     */
    public AnswerMultiSelectPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final IAjaxListener pAnswerValueChangeListener) {
        super(pId, pAnswerValueChangeListener);
        this.question = pQuestion;
        this.answerValuesModel = pAnswerValuesModel;
        this.add(getSelections());
        this.add(getOther());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(CssHeaderItem.forReference( //
                new CssResourceReference(AnswerMultiSelectPanel.class, AnswerMultiSelectPanel.class.getSimpleName() + ".css"))); //$NON-NLS-1$
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
    private ListMultipleChoice<Selection> getSelections() {
        if (this.selections == null) {
            this.selections = new ListMultipleChoice<Selection>( //
                    "selections" // //$NON-NLS-1$
                    , new AnswerModel() //
                    , this.question.getSelections() //
                    , new SelectionChoiceRenderer() //
            );
            this.selections.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(final AjaxRequestTarget pTarget) {
                    pTarget.add(getOther());
                    fireAnswerValueChanged(pTarget);
                }
            });
        }
        return this.selections;
    }

    private boolean isOtherSelected() {
        final Collection<Selection> selectedValues = getSelections().getModelObject();
        for (final Selection selection : selectedValues) {
            if (selection.isOther()) {
                return true;
            }
        }
        return false;
    }

    private void on_other_onUpdate(final AjaxRequestTarget pTarget) {
        fireAnswerValueChanged(pTarget);
    }

    private class AnswerModel implements IModel<List<Selection>> {
        private static final long serialVersionUID = -8054598275270485940L;

        @Override
        public void detach() {
            // 処理なし
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public List<Selection> getObject() {
            final List<Selection> ret = new ArrayList<Selection>();
            for (final Selection s : AnswerMultiSelectPanel.this.question.getSelections()) {
                for (final AnswerValue v : AnswerMultiSelectPanel.this.answerValuesModel.getObject()) {
                    if (s.getValue().equals(v.getValue())) {
                        ret.add(s);
                    }
                }
            }
            return ret;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final List<Selection> pObject) {
            final List<AnswerValue> answers = AnswerMultiSelectPanel.this.answerValuesModel.getObject();
            answers.clear();
            for (final Selection s : pObject) {
                answers.add(new AnswerValue(s.getValue()));
            }
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
            final List<AnswerValue> l = AnswerMultiSelectPanel.this.answerValuesModel.getObject();
            return l.isEmpty() ? Empty.STRING : l.get(0).getOptionText();
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final String pObject) {
            final Iterator<Selection> selectedValue = getSelections().getModelObject().iterator();
            if (!selectedValue.hasNext()) {
                return;
            }
            final AnswerValue answerValue = new AnswerValue(selectedValue.next().getValue(), pObject);
            AnswerMultiSelectPanel.this.answerValuesModel.setObject(Arrays.asList(answerValue));
        }
    }
}

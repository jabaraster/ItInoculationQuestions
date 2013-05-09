/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerMultiSelectPanel extends InputPanel {
    private static final long               serialVersionUID = 8239144123893439073L;

    private final Question                  question;
    private final IModel<List<AnswerValue>> answerValuesModel;

    private ListMultipleChoice<Selection>   selections;

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
    }

    // /**
    // * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
    // */
    // @Override
    // public FormComponent<?> getInputComponent() {
    // return getSelections();
    // }

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
                    fireAnswerValueChanged(pTarget);
                }
            });
        }
        return this.selections;
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
}

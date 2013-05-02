/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.Selection;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswerMultiSelectPanel extends InputPanel {
    private static final long             serialVersionUID = 8239144123893439073L;

    private final Question                question;
    private final IModel<List<String>>    answerValueModel;

    private ListMultipleChoice<Selection> selections;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValueModel 回答を格納するモデル.
     */
    public AnswerMultiSelectPanel(final String pId, final Question pQuestion, final IModel<List<String>> pAnswerValueModel) {
        super(pId);
        this.question = pQuestion;
        this.answerValueModel = pAnswerValueModel;
        this.add(getSelections());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
     */
    @Override
    public FormComponent<?> getInputComponent() {
        return getSelections();
    }

    @SuppressWarnings("synthetic-access")
    private ListMultipleChoice<Selection> getSelections() {
        if (this.selections == null) {
            this.selections = new ListMultipleChoice<Selection>( //
                    "selections" // //$NON-NLS-1$
                    , new AnswerModel() //
                    , this.question.getSelections() //
                    , new SelectionChoiceRenderer() //
            );
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
                if (s.getValue().equals(AnswerMultiSelectPanel.this.answerValueModel.getObject())) {
                    ret.add(s);
                }
            }
            return ret;
        }

        @SuppressWarnings("synthetic-access")
        @Override
        public void setObject(final List<Selection> pObject) {
            final List<String> answers = AnswerMultiSelectPanel.this.answerValueModel.getObject();
            answers.clear();
            for (final Selection s : pObject) {
                answers.add(s.getValue());
            }
        }

    }
}

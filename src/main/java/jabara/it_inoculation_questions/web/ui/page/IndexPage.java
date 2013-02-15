/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.it_inoculation_questions.web.ui.component.DescriptionPanel;
import jabara.it_inoculation_questions.web.ui.component.QAPanel;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class IndexPage extends ItInoculationQuestionsWebPageBase {

    @Inject
    IQuestionService             questionService;
    @Inject
    IAnswersService              answersService;

    private final List<Question> questionsValue;
    private final Answers        answersValue = new Answers();

    private ListView<Question>   questions;

    /**
     * 
     */
    public IndexPage() {
        this.questionsValue = this.questionService.getQuestions();
        for (@SuppressWarnings("unused")
        final Question q : this.questionsValue) {
            this.answersValue.newAnswer();
        }
        this.add(getQuestions());

        // TODO ここでINSERTするのが適切か・・・
        this.answersService.insertOrUpdate(this.answersValue);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse, this.getClass());
    }

    @Override
    protected Panel createHeaderPanel(final String pHeaderPanelId) {
        return new DescriptionPanel(pHeaderPanelId);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "IT予防接種アンケート"; //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings("nls")
    private ListView<Question> getQuestions() {
        if (this.questions == null) {
            this.questions = new ListView<Question>("questions", this.questionsValue) {
                @Override
                protected void populateItem(final ListItem<Question> pItem) {
                    @SuppressWarnings("synthetic-access")
                    final Answer answer = IndexPage.this.answersValue.getAnswer(pItem.getIndex());

                    final IModel<String> m = new IModel<String>() {
                        @Override
                        public void detach() {
                            // 処理なし
                        }

                        @Override
                        public String getObject() {
                            return answer.getValue();
                        }

                        @Override
                        public void setObject(final String pObject) {
                            answer.setValue(pObject);
                        }
                    };
                    final QAPanel qaPanel = new QAPanel("question", pItem.getModelObject(), m, pItem.getIndex());
                    final Form<?> form = new Form<Object>("questionForm");
                    form.add(qaPanel);
                    pItem.add(form);

                    qaPanel.getInputComponent().add(new OnChangeAjaxBehavior() {
                        @Override
                        protected void onUpdate(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
                            IndexPage.this.answersService.update(answer);
                        }
                    });
                }
            };
        }
        return this.questions;
    }
}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.service.IAnswersService;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswersPage extends AuthenticatedWebPageBase {
    private static final long   serialVersionUID = 1873666627586886600L;

    @Inject
    IAnswersService             answersService;

    private final List<Answers> answersListValue;

    private ListView<Answers>   answersList;

    /**
     * 
     */
    public AnswersPage() {
        this.answersListValue = this.answersService.getAllAnswers();
        this.add(getAnswersList());
        setStatelessHint(true);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        this.addPageCssReference(pResponse);
        pResponse.render(JavaScriptHeaderItem.forUrl("http://code.jquery.com/jquery-1.9.1.min.js")); //$NON-NLS-1$
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pHeaderPanelId) {
        return new EmptyPanel(pHeaderPanelId);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 8983883004843308112L;

            @Override
            public String getObject() {
                return "回答一覧"; //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<Answers> getAnswersList() {
        if (this.answersList == null) {
            this.answersList = new ListView<Answers>("answersList", this.answersListValue) {
                @Override
                protected void populateItem(final ListItem<Answers> pItem) {
                    final Answers answers = pItem.getModelObject();
                    final Link<Object> goDetail = new StatelessLink<Object>("goDetail") {
                        @Override
                        public void onClick() {
                            setResponsePage(AnswerPage.class, AnswerPage.createParameters(answers.getId().longValue()));
                        }
                    };
                    pItem.add(goDetail);

                    pItem.add(new Label("id", String.valueOf(answers.getId()))); //$NON-NLS-1$
                }
            };
        }
        return this.answersList;
    }
}
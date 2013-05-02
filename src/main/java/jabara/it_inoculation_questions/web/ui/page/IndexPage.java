/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.ItInoculationQuestionsEnv;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.it_inoculation_questions.web.ui.component.DescriptionPanel;
import jabara.it_inoculation_questions.web.ui.component.QAPanel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class IndexPage extends ItInoculationQuestionsWebPageBase {

    private static final String  VISITED_COOKIE_NAME = ItInoculationQuestionsEnv.APPLICATION_NAME + ".visited"; //$NON-NLS-1$

    @Inject
    IQuestionService             questionService;
    @Inject
    IAnswersService              answersService;

    private final List<Question> questionsValue;
    private final AnswersSave    answersValue;

    private ListView<Question>   questions;
    private Form<?>              deciderForm;
    private Button               decider;

    /**
     * 
     */
    public IndexPage() {
        this.questionsValue = this.questionService.getQuestions();

        final String answersKey = manageVisitedCookie();
        this.answersValue = this.answersService.getSavedByKey(answersKey, this.questionsValue.size());

        this.add(getQuestions());
        this.add(getDeciderForm());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse, this.getClass());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#createHeaderPanel(java.lang.String)
     */
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
                return "設問"; //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings("nls")
    private Button getDecider() {
        if (this.decider == null) {
            this.decider = new Button("decider") {
                @Override
                public void onError() {
                    jabara.Debug.write();
                }

                @SuppressWarnings("synthetic-access")
                @Override
                public void onSubmit() {
                    IndexPage.this.answersService.decide(IndexPage.this.answersValue);

                    final PageParameters param = ThankYouPage.createParameterForAnswerIdDisplay(IndexPage.this.answersValue.getId().longValue());
                    setResponsePage(ThankYouPage.class, param);
                }
            };
        }
        return this.decider;
    }

    private Form<?> getDeciderForm() {
        if (this.deciderForm == null) {
            this.deciderForm = new Form<Object>("deciderForm"); //$NON-NLS-1$
            this.deciderForm.add(getDecider());
        }
        return this.deciderForm;
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

    private static String addVisitedCookie(final String pVisitedCookieName) {
        final String key = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession().getId();
        final Cookie visitedCookie = new Cookie(pVisitedCookieName, key);

        final long maxAge = TimeUnit.SECONDS.convert(3, TimeUnit.DAYS);
        if (maxAge > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }
        visitedCookie.setMaxAge((int) maxAge);
        ((WebResponse) RequestCycle.get().getOriginalResponse()).addCookie(visitedCookie);
        return key;
    }

    private static String manageVisitedCookie() {

        final WebRequest request = (WebRequest) RequestCycle.get().getRequest();
        final Cookie cookie = request.getCookie(VISITED_COOKIE_NAME);
        if (cookie == null) { // 初めての訪問なら
            return addVisitedCookie(VISITED_COOKIE_NAME);
        }
        return cookie.getValue();
    }

}

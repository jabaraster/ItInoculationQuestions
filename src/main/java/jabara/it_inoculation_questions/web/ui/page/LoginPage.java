/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.model.FailAuthentication;
import jabara.it_inoculation_questions.web.ui.ItInoculationQuestionsSession;
import jabara.it_inoculation_questions.web.ui.JavaScriptUtil;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * @author jabaraster
 */
public class LoginPage extends ItInoculationQuestionsWebPageBase {
    private static final long serialVersionUID = -8987437226988765476L;

    private StatelessForm<?>  form;
    private FeedbackPanel     feedback;
    private PasswordTextField password;
    private Button            submitter;

    /**
     * @param pParameters パラメータ情報.
     */
    public LoginPage(final PageParameters pParameters) {
        super(pParameters);
        this.add(getForm());
        setStatelessHint(true);
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse, this.getClass());
        pResponse.render(OnDomReadyHeaderItem.forScript(JavaScriptUtil.getFocusScript(getPassword())));
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return new EmptyPanel(pId);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#getTitleLabelModel()
     */
    @SuppressWarnings("serial")
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                return "ログイン"; //$NON-NLS-1$
            }
        };
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
            this.feedback.setOutputMarkupId(true);
        }
        return this.feedback;
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<Object>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getPassword());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", new Model<String>()); //$NON-NLS-1$
            this.password.setOutputMarkupId(true);
            this.password.setRequired(true);
        }
        return this.password;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") {
                @SuppressWarnings("synthetic-access")
                @Override
                public void onSubmit() {
                    onSubmitterClick();
                }
            };
        }
        return this.submitter;
    }

    private void onSubmitterClick() {
        final ItInoculationQuestionsSession session = ItInoculationQuestionsSession.get();
        try {
            session.authenticate(getPassword().getModelObject());

            final StringValue redirectPath = getPageParameters().get("u"); //$NON-NLS-1$
            if (!redirectPath.isEmpty()) {
                this.setResponsePage(new RedirectPage(redirectPath.toString()));
            } else {
                this.setResponsePage(IndexPage.class);
            }
        } catch (final FailAuthentication e) {
            error(getString("failAuthentication")); //$NON-NLS-1$
            this.setResponsePage(this);
        }
    }

    /**
     * @return このページにリダイレクトするためのURLパラメータを返す.
     */
    public static PageParameters createRequesParameter() {
        final Request request = RequestCycle.get().getRequest();
        final PageParameters parameters = new PageParameters();
        final String requestPath = request.getContextPath() + request.getFilterPath() + "/" + request.getUrl().getPath(); //$NON-NLS-1$
        parameters.add("u", requestPath); //$NON-NLS-1$
        return parameters;
    }
}

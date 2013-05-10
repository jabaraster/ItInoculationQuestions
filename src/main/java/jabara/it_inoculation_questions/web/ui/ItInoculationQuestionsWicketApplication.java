/**
 * 
 */
package jabara.it_inoculation_questions.web.ui;

import jabara.it_inoculation_questions.model.DI;
import jabara.it_inoculation_questions.service.IQuestionService;
import jabara.it_inoculation_questions.web.ui.page.AnswerPage;
import jabara.it_inoculation_questions.web.ui.page.AnswersPage;
import jabara.it_inoculation_questions.web.ui.page.AnswersStatisticsPage;
import jabara.it_inoculation_questions.web.ui.page.AuthenticatedWebPageBase;
import jabara.it_inoculation_questions.web.ui.page.IndexPage;
import jabara.it_inoculation_questions.web.ui.page.LoginPage;
import jabara.it_inoculation_questions.web.ui.page.QuestionConfigurationUploadPage;
import jabara.it_inoculation_questions.web.ui.page.ThankYouPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;

/**
 * 
 * @author jabaraster
 */
public class ItInoculationQuestionsWicketApplication extends WebApplication {

    private static final String ENC = "UTF-8"; //$NON-NLS-1$

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        final IQuestionService questionService = DI.get(IQuestionService.class);
        if (questionService.isQuestionsRegistered()) {
            return IndexPage.class;
        }
        return QuestionConfigurationUploadPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(final Request pRequest, @SuppressWarnings("unused") final Response pResponse) {
        return new ItInoculationQuestionsSession(pRequest);
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountPages();
        initializeEncoding();
        initializeInjection();
        initializeAccessControl();
    }

    private void initializeAccessControl() {
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {

            @SuppressWarnings("unused")
            @Override
            public boolean isActionAuthorized(final Component pComponent, final Action pAction) {
                return true;
            }

            @Override
            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(final Class<T> pComponentClass) {
                // Pageに載っているUI部品は常に表示OKにする.
                // どうせPage自体を表示NGにすれば部品も表示されないので、これでいい.
                if (!WebPage.class.isAssignableFrom(pComponentClass)) {
                    return true;
                }

                final ItInoculationQuestionsSession session = ItInoculationQuestionsSession.get();

                // 認証済みなのにログインページを表示しようとした場合、メインページにリダイレクトさせる.
                if (LoginPage.class.equals(pComponentClass)) {
                    if (!session.isSessionInvalidated() && session.isAuthenticated()) {
                        throw new RestartResponseAtInterceptPageException(AnswersPage.class);
                    }
                    return true;
                }

                // 認証不要なページ（ログインページとか）は表示する.
                if (!AuthenticatedWebPageBase.class.isAssignableFrom(pComponentClass)) {
                    return true;
                }

                // 処理がここに至るのは認証が必要なページが表示されようとしているとき.
                if (session.isAuthenticated()) {
                    return true;
                }
                throw new RestartResponseAtInterceptPageException(LoginPage.class, LoginPage.createRequesParameter());
            }
        });
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        // GuiceによるDI機能をWicketに対して有効にする仕込み.
        // これによりDaoBaseクラスを継承したクラスのpublicメソッドはトランザクションが自動ではられるようになる.
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, DI.getGuiceInjector()));
    }

    private void mountPages() {
        mountPage("login", LoginPage.class); //$NON-NLS-1$
        mountPage("thankyou", ThankYouPage.class); //$NON-NLS-1$
        mountPage("answers/${id}", AnswerPage.class); //$NON-NLS-1$
        mountPage("answers", AnswersPage.class); //$NON-NLS-1$
        mountPage("answersStatistics", AnswersStatisticsPage.class); //$NON-NLS-1$
    }
}

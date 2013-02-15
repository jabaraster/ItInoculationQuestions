/**
 * 
 */
package jabara.it_inoculation_questions.web.ui;

import jabara.it_inoculation_questions.model.DI;
import jabara.it_inoculation_questions.web.ui.page.AnswerPage;
import jabara.it_inoculation_questions.web.ui.page.IndexPage;
import jabara.it_inoculation_questions.web.ui.page.ThankYouPage;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

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
        return IndexPage.class;
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
        mountPage("thankyou", ThankYouPage.class); //$NON-NLS-1$
        mountPage("answer", AnswerPage.class); //$NON-NLS-1$
    }
}

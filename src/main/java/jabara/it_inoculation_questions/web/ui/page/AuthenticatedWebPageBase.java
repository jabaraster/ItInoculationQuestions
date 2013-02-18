/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.web.ui.component.AdministratorLinksPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public abstract class AuthenticatedWebPageBase extends ItInoculationQuestionsWebPageBase {
    private static final long serialVersionUID = -940818010018665769L;

    /**
     * 
     */
    public AuthenticatedWebPageBase() {
        super();
    }

    /**
     * @param pParameters URLパラメータ.
     */
    public AuthenticatedWebPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pHeaderPanelId) {
        return new AdministratorLinksPanel(pHeaderPanelId);
    }
}

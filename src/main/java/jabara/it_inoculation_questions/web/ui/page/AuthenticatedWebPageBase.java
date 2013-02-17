/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

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
}

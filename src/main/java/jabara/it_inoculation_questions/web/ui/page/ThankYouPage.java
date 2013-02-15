/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class ThankYouPage extends ItInoculationQuestionsWebPageBase {

    private Link<Object> goHome;

    /**
     * 
     */
    public ThankYouPage() {
        this.add(getGoHome());
        setStatelessHint(true);
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
            @Override
            public String getObject() {
                return "ご協力ありがとうございました"; //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings("nls")
    private Link<Object> getGoHome() {
        if (this.goHome == null) {
            this.goHome = new StatelessLink<Object>("goHome") {
                @Override
                public void onClick() {
                    this.setResponsePage(Application.get().getHomePage());
                }
            };
        }
        return this.goHome;
    }
}

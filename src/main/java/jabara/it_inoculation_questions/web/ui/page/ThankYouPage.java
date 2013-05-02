/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.general.Empty;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class ThankYouPage extends ItInoculationQuestionsWebPageBase {

    private static final String PARAMETER_NAME_ANSWER_ID = "answerId"; //$NON-NLS-1$

    private Label               answerId;
    private Link<Object>        goHome;

    /**
     * @param pParameters リクエストパラメータ.
     */
    public ThankYouPage(final PageParameters pParameters) {
        super(pParameters);
        this.add(getAnswerId());
        this.add(getGoHome());
        setStatelessHint(true);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse);
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

    private Label getAnswerId() {
        if (this.answerId == null) {
            this.answerId = new Label("answerId", getAnswerIdValue()); //$NON-NLS-1$
        }
        return this.answerId;
    }

    private String getAnswerIdValue() {
        final StringValue value = getPageParameters().get(PARAMETER_NAME_ANSWER_ID);
        return value == null ? Empty.STRING : value.toString();
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

    /**
     * @param pAnswerId -
     * @return -
     */
    public static PageParameters createParameterForAnswerIdDisplay(final long pAnswerId) {
        final PageParameters ret = new PageParameters();
        ret.add(PARAMETER_NAME_ANSWER_ID, Long.valueOf(pAnswerId));
        return ret;
    }
}

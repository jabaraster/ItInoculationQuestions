/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.web.ui.page.AnswersPage;
import jabara.it_inoculation_questions.web.ui.page.AnswersStatisticsPage;
import jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public class AdministratorLinksPanel extends Panel {
    private static final long serialVersionUID = -1159621008035914980L;

    private Link<?>           goStatistics;
    private Link<?>           goList;

    /**
     * @param pId パネルのwicket:id.
     */
    public AdministratorLinksPanel(final String pId) {
        super(pId);
        this.add(getGoList());
        this.add(getGoStatistics());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        ItInoculationQuestionsWebPageBase.addPageCssReference(pResponse, this.getClass());
    }

    @SuppressWarnings("nls")
    private Link<?> getGoList() {
        if (this.goList == null) {
            this.goList = new BookmarkablePageLink<Object>("goList", AnswersPage.class);
        }
        return this.goList;
    }

    @SuppressWarnings("nls")
    private Link<?> getGoStatistics() {
        if (this.goStatistics == null) {
            this.goStatistics = new BookmarkablePageLink<Object>("goStatistics", AnswersStatisticsPage.class);
        }
        return this.goStatistics;
    }
}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.service.IAnswersService;
import jabara.it_inoculation_questions.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.FileResourceStream;

/**
 * @author jabaraster
 */
public class AnswersPage extends AuthenticatedWebPageBase {
    private static final long   serialVersionUID = 1873666627586886600L;

    @Inject
    IAnswersService             answersService;

    private final List<Answers> answersListValue;

    private Link<?>             csvDownloader;
    private Link<?>             refresher;
    private ListView<Answers>   answersList;

    /**
     * 
     */
    public AnswersPage() {
        this.answersListValue = this.answersService.getAllAnswers();
        this.add(getAnswersList());
        this.add(getCsvDownloader());
        this.add(getRefresher());
        setStatelessHint(true);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        this.addPageCssReference(pResponse);
        ItInoculationQuestionsWebPageBase.addJQueryReference(pResponse);
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

                private final DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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

                    final Date d = DateUtil.convertAsiaTokyo(answers.getCreated());
                    pItem.add(new Label("created", this.formatter.format(d)));
                }
            };
        }
        return this.answersList;
    }

    @SuppressWarnings({ "serial", "nls" })
    private Link<?> getCsvDownloader() {
        if (this.csvDownloader == null) {
            this.csvDownloader = new Link<Object>("csvDownloader") {
                @Override
                public void onClick() {
                    final File csv = AnswersPage.this.answersService.makeAnswersCsv();
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(new FileResourceStream(csv) {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            csv.delete();
                        }

                        @Override
                        public String getContentType() {
                            return "text/csv; charset=" + AnswersPage.this.answersService.getCsvFileEncoding().name();
                        }
                    }));
                }
            };
        }
        return this.csvDownloader;
    }

    private Link<?> getRefresher() {
        if (this.refresher == null) {
            this.refresher = new BookmarkablePageLink<Object>("refresher", this.getClass()); //$NON-NLS-1$
        }
        return this.refresher;
    }
}

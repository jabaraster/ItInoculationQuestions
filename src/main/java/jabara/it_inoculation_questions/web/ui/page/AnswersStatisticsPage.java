/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.it_inoculation_questions.model.AnswerSummary;
import jabara.it_inoculation_questions.model.AnswersStatistics;
import jabara.it_inoculation_questions.service.IAnswersService;

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class AnswersStatisticsPage extends AuthenticatedWebPageBase {
    private static final long             serialVersionUID = 8964913831542966551L;

    @Inject
    IAnswersService                       answersService;

    private final List<AnswersStatistics> answersStatisticsValue;

    private Link<?>                       refresher;
    private ListView<AnswersStatistics>   answersStatistics;

    /**
     * 
     */
    public AnswersStatisticsPage() {
        this.answersStatisticsValue = this.answersService.getAnswersStatistics();
        this.add(getAnswersStatistics());
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
            private static final long serialVersionUID = 6472148559551680280L;

            @Override
            public String getObject() {
                return "回答統計情報"; //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<AnswersStatistics> getAnswersStatistics() {
        if (this.answersStatistics == null) {
            this.answersStatistics = new ListView<AnswersStatistics>("answersStatistics", this.answersStatisticsValue) {
                @Override
                protected void populateItem(final ListItem<AnswersStatistics> pItem) {
                    if (pItem.getModelObject().getAnswerSummaries().isEmpty()) {
                        populateEmptyAnswerItem(pItem);
                    } else {
                        populateAnswerItem(pItem);
                    }
                }

                @SuppressWarnings("synthetic-access")
                private void populateAnswerItem(final ListItem<AnswersStatistics> pItem) {
                    final AnswersStatistics answers = pItem.getModelObject();
                    final List<AnswerSummary> summaries = answers.getAnswerSummaries();
                    final AttributeModifier rowspan = AttributeModifier.replace("rowspan", Integer.valueOf(answers.getAnswerSummaries().size()));
                    pItem.add(createRowSpanLabel("index", "Q" + String.valueOf(pItem.getIndex() + 1), rowspan));
                    pItem.add(createRowSpanLabel("questionMessage", answers.getQuestionMessage(), rowspan));
                    pItem.add(new Label("selectionMessage", summaries.get(0).getSelectionMessage())); //$NON-NLS-1$
                    pItem.add(new Label("answerCount", Integer.valueOf(summaries.get(0).getAnswerCount()))); //$NON-NLS-1$
                    pItem.add(createAnswerSummary("answerSummary", answers));
                }

                @SuppressWarnings("synthetic-access")
                private void populateEmptyAnswerItem(final ListItem<AnswersStatistics> pItem) {
                    final AnswersStatistics answers = pItem.getModelObject();
                    final AttributeModifier rowspan = AttributeModifier.remove("rowspan");
                    pItem.add(createRowSpanLabel("index", "Q" + String.valueOf(pItem.getIndex() + 1), rowspan));
                    pItem.add(createRowSpanLabel("questionMessage", answers.getQuestionMessage(), rowspan));
                    pItem.add(new Label("selectionMessage", "-")); //$NON-NLS-1$
                    pItem.add(new Label("answerCount", "-")); //$NON-NLS-1$
                    pItem.add(createEmptyList("answerSummary"));
                }
            };
        }
        return this.answersStatistics;
    }

    private Link<?> getRefresher() {
        if (this.refresher == null) {
            this.refresher = new BookmarkablePageLink<Object>("refresher", this.getClass()); //$NON-NLS-1$
        }
        return this.refresher;
    }

    @SuppressWarnings({ "serial" })
    private static ListView<AnswerSummary> createAnswerSummary(final String pId, final AnswersStatistics pAnswers) {
        final List<AnswerSummary> list = pAnswers.getAnswerSummaries().subList(1, pAnswers.getAnswerSummaryCount());
        return new ListView<AnswerSummary>(pId, list) {
            @Override
            protected void populateItem(final ListItem<AnswerSummary> pItem) {
                final AnswerSummary summary = pItem.getModelObject();
                pItem.add(new Label("selectionMessage", summary.getSelectionMessage())); //$NON-NLS-1$
                pItem.add(new Label("answerCount", Integer.valueOf(summary.getAnswerCount()))); //$NON-NLS-1$
            }
        };
    }

    @SuppressWarnings("serial")
    private static Component createEmptyList(final String pId) {
        return new Loop(pId, 0) {
            @Override
            protected void populateItem(@SuppressWarnings("unused") final LoopItem pItem) {
                // 処理なし
            }
        };
    }

    private static Label createRowSpanLabel(final String pId, final String pLabel, final AttributeModifier pRowSpan) {
        final Label ret = new Label(pId, pLabel);
        ret.add(pRowSpan);
        return ret;
    }

}

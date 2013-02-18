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
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
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

    private ListView<AnswersStatistics>   answersStatistics;

    /**
     * 
     */
    public AnswersStatisticsPage() {
        this.answersStatisticsValue = this.answersService.getAnswersStatistics();
        this.add(getAnswersStatistics());
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
                @SuppressWarnings("synthetic-access")
                @Override
                protected void populateItem(final ListItem<AnswersStatistics> pItem) {
                    final AnswersStatistics answers = pItem.getModelObject();
                    final List<AnswerSummary> summaries = answers.getAnswerSummaries();
                    if (summaries.isEmpty()) {
                        throw new IllegalStateException();
                    }

                    final AttributeModifier rowspan = AttributeModifier.replace("rowspan", Integer.valueOf(answers.getAnswerSummaries().size()));

                    final Label index = new Label("index", "Q" + String.valueOf(pItem.getIndex() + 1));
                    index.add(rowspan);
                    pItem.add(index);

                    final Label questionMessage = new Label("questionMessage", answers.getQuestionMessage()); //$NON-NLS-1$
                    questionMessage.add(rowspan); //$NON-NLS-1$
                    pItem.add(questionMessage);

                    pItem.add(new Label("selectionMessage", summaries.get(0).getSelectionMessage())); //$NON-NLS-1$
                    pItem.add(new Label("answerCount", Integer.valueOf(summaries.get(0).getAnswerCount()))); //$NON-NLS-1$

                    pItem.add(createAnswerSummary(answers));
                }
            };
        }
        return this.answersStatistics;
    }

    @SuppressWarnings({ "serial", "nls" })
    private static ListView<AnswerSummary> createAnswerSummary(final AnswersStatistics pAnswers) {
        final List<AnswerSummary> list = pAnswers.getAnswerSummaries().subList(1, pAnswers.getAnswerSummaryCount());
        return new ListView<AnswerSummary>("answerSummary", list) {
            @Override
            protected void populateItem(final ListItem<AnswerSummary> pItem) {
                final AnswerSummary summary = pItem.getModelObject();
                pItem.add(new Label("selectionMessage", summary.getSelectionMessage())); //$NON-NLS-1$
                pItem.add(new Label("answerCount", Integer.valueOf(summary.getAnswerCount()))); //$NON-NLS-1$
            }
        };
    }

}

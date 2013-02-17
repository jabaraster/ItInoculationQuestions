/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.service.IAnswersService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * @author jabaraster
 */
public class AnswerPage extends AuthenticatedWebPageBase {
    private static final long serialVersionUID = 5632334148076438396L;

    @Inject
    IAnswersService           answersService;

    private final Answers     answersValue;

    private TextField<Long>   idText;
    private ListView<Answer>  answers;
    private Link<?>           goList;

    /**
     * @param pParameters URLパラメータ.
     */
    public AnswerPage(final PageParameters pParameters) {
        super(pParameters);
        final StringValue answerId = pParameters.get("id"); //$NON-NLS-1$
        this.answersValue = findAnswers(answerId);
        this.add(getIdText());
        this.add(getAnswers());
        this.add(getGoList());
        setStatelessHint(true);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse);
        pResponse.render(JavaScriptHeaderItem.forUrl("http://code.jquery.com/jquery-1.9.1.min.js")); //$NON-NLS-1$
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
    @SuppressWarnings({ "nls", "serial" })
    @Override
    protected IModel<String> getTitleLabelModel() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "回答の確認";
            }
        };
    }

    private Answers findAnswers(final StringValue pAnswersId) {
        if (pAnswersId == null) {
            return null;
        }
        try {
            return this.answersService.getById(Long.parseLong(pAnswersId.toString()));
        } catch (final NumberFormatException e) {
            return null;
        } catch (final NotFound e) {
            return null;
        }
    }

    @SuppressWarnings({ "serial", "nls" })
    private ListView<Answer> getAnswers() {
        if (this.answers == null) {

            final List<Answer> list = toAnswerList(this.answersValue);

            this.answers = new ListView<Answer>("answers", list) {
                @Override
                protected void populateItem(final ListItem<Answer> pItem) {
                    pItem.setModel(new CompoundPropertyModel<Answer>(pItem.getModelObject()));
                    pItem.add(new Label("id"));
                    pItem.add(new Label("questionIndex"));
                    pItem.add(new Label("value"));
                }
            };
        }
        return this.answers;
    }

    @SuppressWarnings({ "nls", "serial" })
    private Link<?> getGoList() {
        if (this.goList == null) {
            this.goList = new StatelessLink<Object>("goList") {
                @Override
                public void onClick() {
                    setResponsePage(AnswersPage.class);
                }
            };
        }
        return this.goList;
    }

    @SuppressWarnings({ "serial", "nls" })
    private TextField<Long> getIdText() {
        if (this.idText == null) {
            this.idText = new TextField<Long>("idText", new AbstractReadOnlyModel<Long>() {
                @SuppressWarnings("synthetic-access")
                @Override
                public Long getObject() {
                    return AnswerPage.this.answersValue == null ? null : AnswerPage.this.answersValue.getId();
                }
            });
        }
        return this.idText;
    }

    /**
     * @param pId 回答のID値.
     * @return URLパラメータ.
     */
    public static PageParameters createParameters(final long pId) {
        final PageParameters ret = new PageParameters();
        ret.set("id", Long.valueOf(pId)); //$NON-NLS-1$
        return ret;
    }

    private static List<Answer> toAnswerList(final Answers pAnswers) {
        if (pAnswers == null) {
            return Collections.emptyList();
        }
        final List<Answer> ret = new ArrayList<Answer>();
        for (final Answer a : pAnswers) {
            ret.add(a);
        }
        return ret;
    }

}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.general.Empty;
import jabara.it_inoculation_questions.model.FailAuthentication;
import jabara.it_inoculation_questions.service.IQuestionService;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author jabaraster
 */
public class QuestionConfigurationUploadPage extends AuthenticatedWebPageBase {
    private static final long serialVersionUID = 5733953788580167257L;

    @Inject
    IQuestionService          questionService;

    private Form<?>           form;
    private TextField<String> qaName;
    private FeedbackPanel     feedbackForQaName;
    private FileUploadField   qaXml;
    private FeedbackPanel     feedbackForQaXml;
    private PasswordTextField password;
    private FeedbackPanel     feedbackForPassword;
    private Button            submitter;

    /**
     * 
     */
    public QuestionConfigurationUploadPage() {
        this.add(getForm());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pHeaderPanelId) {
        return new EmptyPanel(pHeaderPanelId);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#getHeadlineModel()
     */
    @SuppressWarnings("serial")
    @Override
    protected IModel<String> getHeadlineModel() {
        return new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return "アンケート情報設定"; //$NON-NLS-1$
            }
        };
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.page.ItInoculationQuestionsWebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Model.of("設問XMLファイルアップロード"); //$NON-NLS-1$
    }

    private FeedbackPanel getFeebackForQaName() {
        if (this.feedbackForQaName == null) {
            this.feedbackForQaName = new ComponentFeedbackPanel("feedbackForQaName", getQaName()); //$NON-NLS-1$
        }
        return this.feedbackForQaName;
    }

    private FeedbackPanel getFeedbackForPassword() {
        if (this.feedbackForPassword == null) {
            this.feedbackForPassword = new ComponentFeedbackPanel("feedbackForPassword", getPassword()); //$NON-NLS-1$
        }
        return this.feedbackForPassword;
    }

    private FeedbackPanel getFeedbackForQaXml() {
        if (this.feedbackForQaXml == null) {
            this.feedbackForQaXml = new ComponentFeedbackPanel("feedbackForQaXml", getQaXml()); //$NON-NLS-1$
        }
        return this.feedbackForQaXml;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<Object>("form"); //$NON-NLS-1$
            this.form.add(getQaName());
            this.form.add(getFeebackForQaName());
            this.form.add(getQaXml());
            this.form.add(getFeedbackForQaXml());
            this.form.add(getPassword());
            this.form.add(getFeedbackForPassword());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", Model.of(Empty.STRING)); //$NON-NLS-1$
            this.password.setRequired(true);
        }
        return this.password;
    }

    private TextField<String> getQaName() {
        if (this.qaName == null) {
            this.qaName = new TextField<String>("qaName", Model.of(Empty.STRING), String.class); //$NON-NLS-1$
            this.qaName.setRequired(true);
        }
        return this.qaName;
    }

    private FileUploadField getQaXml() {
        if (this.qaXml == null) {
            this.qaXml = new FileUploadField("qaXml"); //$NON-NLS-1$
            this.qaXml.setRequired(true);
        }
        return this.qaXml;
    }

    @SuppressWarnings("serial")
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                public void onSubmit() {
                    on_submitter_onSubmit();
                }
            };
        }
        return this.submitter;
    }

    private void on_submitter_onSubmit() {
        final FileUpload uploadFile = getQaXml().getFileUpload();
        try {
            this.questionService.registerQuestion(getPassword().getModelObject(), getQaName().getModelObject(), uploadFile.getInputStream());
            setResponsePage(IndexPage.class);
        } catch (final IOException e) {
            error(e.getMessage());
            e.printStackTrace();
        } catch (final FailAuthentication e) {
            getPassword().error("パスワードが不正です。"); //$NON-NLS-1$
        } finally {
            uploadFile.closeStreams();
        }
    }
}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.page;

import jabara.general.ArgUtil;
import jabara.it_inoculation_questions.ItInoculationQuestionsEnv;
import jabara.it_inoculation_questions.web.ui.JavaScriptUtil;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * 
 * @author jabaraster
 */
public abstract class ItInoculationQuestionsWebPageBase extends WebPage {
    private static final long serialVersionUID = -4825633194551616360L;

    private Label             titleLabel;
    private Panel             headerPanel;

    /**
     * 
     */
    public ItInoculationQuestionsWebPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters パラメータ情報.
     */
    public ItInoculationQuestionsWebPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getTitleLabel());
        this.add(getHeaderPanel());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        renderCommonHead(pResponse);
    }

    /**
     * ページに固有のスタイルを記述したスタイルシートをheadタグに追加します. <br>
     * 「ページに固有のページに固有のスタイルを記述したスタイルシート」とは、ページクラス名と同名のcssファイルのことを指します. <br>
     * 
     * @param pResponse ヘッダ描画用オブジェクト.
     */
    protected void addPageCssReference(final IHeaderResponse pResponse) {
        addPageCssReference(pResponse, this.getClass());
    }

    /**
     * @param pHeaderPanelId headerタグに含めるPanelのwicket:id.
     * @return headerタグに含めるPanel.
     */
    protected abstract Panel createHeaderPanel(String pHeaderPanelId);

    /**
     * @return headerタグに含めるPanel.
     */
    protected Panel getHeaderPanel() {
        if (this.headerPanel == null) {
            this.headerPanel = createHeaderPanel("headerPanel"); //$NON-NLS-1$
        }
        return this.headerPanel;
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

    @SuppressWarnings({ "nls", "serial" })
    private Label getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new Label("titleLabel", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return getTitleLabelModel().getObject() + " - " + ItInoculationQuestionsEnv.APPLICATION_NAME_IN_JAPANESE;
                }
            });
        }
        return this.titleLabel;
    }

    /**
     * jQuery1.9.1を読み込むタグをヘッダに書き込みます.
     * 
     * @param pResponse ヘッダ書き込み用オブジェクト.
     */
    public static void addJQueryReference(final IHeaderResponse pResponse) {
        pResponse.render(JavaScriptHeaderItem.forUrl("http://code.jquery.com/jquery-1.9.1.min.js")); //$NON-NLS-1$
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType CSSファイルの基準となるページクラス.
     */
    public static void addPageCssReference(final IHeaderResponse pResponse, final Class<? extends MarkupContainer> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(pPageType, pPageType.getSimpleName() + ".css"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(ItInoculationQuestionsWebPageBase.class, "fonts/icomoon/style.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(ItInoculationQuestionsWebPageBase.class,
                "bootstrap/css/bootstrap.min.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(ItInoculationQuestionsWebPageBase.class, "ItInoculationQuestions.css"))); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(ItInoculationQuestionsWebPageBase.class,
                JavaScriptUtil.COMMON_JS_FILE_PATH)));
    }
}

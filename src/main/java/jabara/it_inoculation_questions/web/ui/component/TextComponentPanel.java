/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.TextAnswerColumn;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * @param <C> テキスト入力欄の型.
 * @author jabaraster
 */
public abstract class TextComponentPanel<C extends AbstractTextComponent<String>> extends InputPanel {
    private static final long      serialVersionUID = -7819960106628263705L;

    /**
     * 
     */
    protected final Question       question;
    /**
     * 
     */
    protected final IModel<String> answerValueModel;

    private final TextAnswerColumn answerColumn;

    private C                      answerText;
    private Label                  valuePrefix;
    private Label                  valueSuffix;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValueModel 回答を格納するモデル.
     */
    public TextComponentPanel(final String pId, final Question pQuestion, final IModel<String> pAnswerValueModel) {
        super(pId);
        this.question = pQuestion;
        this.answerValueModel = pAnswerValueModel;

        this.answerColumn = pQuestion.getAnswerColumnForText();

        this.add(getAnswerText());
        this.add(getValuePrefix());
        this.add(getValueSuffix());
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.InputPanel#getInputComponent()
     */
    @Override
    public FormComponent<?> getInputComponent() {
        return getAnswerText();
    }

    /**
     * @return テキスト入力欄.
     */
    protected abstract C createAnswerText();

    /**
     * @return テキスト入力欄.
     */
    protected AbstractTextComponent<String> getAnswerText() {
        if (this.answerText == null) {
            this.answerText = createAnswerText();
            this.answerText.add(AttributeModifier.append("maxlength", Integer.valueOf(this.question.getMaxChar()))); //$NON-NLS-1$
        }
        return this.answerText;
    }

    private Label getValuePrefix() {
        if (this.valuePrefix == null) {
            this.valuePrefix = new Label("valuePrefix", this.answerColumn.getValuePrefix()); //$NON-NLS-1$
        }
        return this.valuePrefix;
    }

    private Label getValueSuffix() {
        if (this.valueSuffix == null) {
            this.valueSuffix = new Label("valueSuffix", this.answerColumn.getValueSuffix()); //$NON-NLS-1$
        }
        return this.valueSuffix;
    }

}

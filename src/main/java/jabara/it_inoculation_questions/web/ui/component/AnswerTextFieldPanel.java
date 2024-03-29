/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.entity.AnswerValue;
import jabara.it_inoculation_questions.model.Question;

import java.util.List;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * @author jabaraster
 */
public class AnswerTextFieldPanel extends TextComponentPanel<TextField<List<AnswerValue>>> {
    private static final long serialVersionUID = -3447325264244103488L;

    /**
     * @param pId パネルのwicket:id.
     * @param pQuestion 設問.
     * @param pAnswerValuesModel 回答を格納するモデル.
     * @param pAnswerValueChangeListener -
     */
    public AnswerTextFieldPanel( //
            final String pId //
            , final Question pQuestion //
            , final IModel<List<AnswerValue>> pAnswerValuesModel //
            , final IAjaxListener pAnswerValueChangeListener) {
        super(pId, pQuestion, pAnswerValuesModel, pAnswerValueChangeListener);
    }

    /**
     * @see jabara.it_inoculation_questions.web.ui.component.TextComponentPanel#createAnswerText()
     */
    @SuppressWarnings("serial")
    @Override
    protected TextField<List<AnswerValue>> createAnswerText() {
        final TextField<List<AnswerValue>> ret = new TextField<List<AnswerValue>>("answerText", this.answerValuesModel) { //$NON-NLS-1$
            @SuppressWarnings("unchecked")
            @Override
            public <C> IConverter<C> getConverter(final Class<C> pType) {
                if (List.class.isAssignableFrom(pType)) {
                    return (IConverter<C>) new ListConverter();
                }
                return super.getConverter(pType);
            }
        };
        ret.setType(List.class);
        return ret;
    }
}

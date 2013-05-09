/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.general.Empty;
import jabara.it_inoculation_questions.entity.AnswerValue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

/**
 * @author jabaraster
 */
public class ListConverter implements IConverter<List<AnswerValue>> {
    private static final long serialVersionUID = 7856839258723539847L;

    /**
     * @see org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String, java.util.Locale)
     */
    @Override
    public List<AnswerValue> convertToObject(final String pValue, @SuppressWarnings("unused") final Locale pLocale) {
        return Arrays.asList(new AnswerValue(pValue));
    }

    /**
     * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
     */
    @Override
    public String convertToString(final List<AnswerValue> pValue, @SuppressWarnings("unused") final Locale pLocale) {
        return pValue.isEmpty() ? Empty.STRING : pValue.get(0).getValue();
    }
}

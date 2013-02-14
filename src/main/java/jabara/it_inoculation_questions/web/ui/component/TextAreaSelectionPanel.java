/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Selection;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
public class TextAreaSelectionPanel extends Panel {
    private static final long serialVersionUID = 5600397750347900021L;

    /**
     * @param pId
     * @param pModel
     */
    public TextAreaSelectionPanel(final String pId, final IModel<Selection> pModel) {
        super(pId);
        this.add(new TextArea<Selection>("text", pModel)); //$NON-NLS-1$
    }
}

/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public abstract class InputPanel extends Panel {
    private static final long serialVersionUID = -2247244758107513835L;

    /**
     * @param pId
     */
    public InputPanel(final String pId) {
        super(pId);
    }

    /**
     * @return 入力コンポーネント.
     */
    public abstract FormComponent<?> getInputComponent();
}

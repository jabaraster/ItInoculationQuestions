/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public abstract class InputPanel extends Panel {
    private static final long     serialVersionUID = -2247244758107513835L;

    /**
     * 
     */
    protected final IAjaxListener answerValueChangeListener;

    /**
     * @param pId パネルのwicket:id.
     * @param pAnswerValueChangeListener -
     */
    public InputPanel(final String pId, final IAjaxListener pAnswerValueChangeListener) {
        super(pId);
        this.answerValueChangeListener = pAnswerValueChangeListener;
    }

    /**
     * @param pTarget -
     */
    protected final void fireAnswerValueChanged(final AjaxRequestTarget pTarget) {
        if (this.answerValueChangeListener != null) {
            this.answerValueChangeListener.handle(pTarget);
        }
    }
}

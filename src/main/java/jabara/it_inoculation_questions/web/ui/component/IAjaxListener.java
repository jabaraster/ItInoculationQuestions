/**
 * 
 */
package jabara.it_inoculation_questions.web.ui.component;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author jabaraster
 */
public interface IAjaxListener extends Serializable {

    /**
     * @param pTarget -
     */
    void handle(AjaxRequestTarget pTarget);
}

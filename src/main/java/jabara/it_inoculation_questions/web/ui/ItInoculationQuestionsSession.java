/**
 * 
 */
package jabara.it_inoculation_questions.web.ui;

import jabara.it_inoculation_questions.ItInoculationQuestionsEnv;
import jabara.it_inoculation_questions.model.FailAuthentication;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * @author jabaraster
 */
public class ItInoculationQuestionsSession extends WebSession {
    private static final long   serialVersionUID = 6930131592306007296L;

    private final AtomicBoolean authenticated    = new AtomicBoolean(false);

    /**
     * @param pRequest
     */
    public ItInoculationQuestionsSession(final Request pRequest) {
        super(pRequest);
    }

    /**
     * @param pPassword
     * @throws FailAuthentication
     */
    public void authenticate(final String pPassword) throws FailAuthentication {
        if (!ItInoculationQuestionsEnv.getPassword().equals(pPassword)) {
            throw new FailAuthentication();
        }
        this.authenticated.set(true);
    }

    /**
     * @return 認証済みならtrue.
     */
    public boolean isAuthenticated() {
        return this.authenticated.get();
    }

    /**
     * @return セッション.
     */
    public static ItInoculationQuestionsSession get() {
        return (ItInoculationQuestionsSession) Session.get();
    }
}

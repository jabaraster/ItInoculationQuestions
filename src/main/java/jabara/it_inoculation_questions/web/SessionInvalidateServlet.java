/**
 * 
 */
package jabara.it_inoculation_questions.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
@WebServlet(urlPatterns = { "/sessionInvalidate" })
public class SessionInvalidateServlet extends HttpServlet {
    private static final long serialVersionUID = 5360693042689763414L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws IOException, ServletException {
        pRequest.getSession().invalidate();
        pRequest.getRequestDispatcher("/").forward(pRequest, pResponse); //$NON-NLS-1$
    }
}

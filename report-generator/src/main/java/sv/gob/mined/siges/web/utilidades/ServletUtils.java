/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.gob.mined.siges.web.excepciones.BusinessError;
import sv.gob.mined.siges.web.excepciones.BusinessException;

/**
 *
 * @author Sofis Solutions
 */
public class ServletUtils {

    private static final Logger LOGGER = Logger.getLogger(ServletUtils.class.getName());

    public static void printMessage(String title, String message, HttpServletResponse response) {
        try {
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            String docType = "<!DOCTYPE html>\n";
            out.println(docType
                    + "<html>\n" + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>" + title + "</title></head>\n" + "<body>");
            out.println("<h2>" + message + "</h2></body>\n</html>");
            out.close();
        } catch (Exception ex) {
             LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static void printMessage(String title, BusinessException exception, HttpServletResponse response) {
        try {
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            String docType = "<!DOCTYPE html>\n";
            out.println(docType
                    + "<html>\n" + "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>" + title + "</title></head>\n" + "<body>");
            for (BusinessError be : exception.getErrores()) {
                out.println("<h2>" + be.getMensaje() + "</h2>");
            }
            out.println("</body>\n</html>");
            out.close();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }
}

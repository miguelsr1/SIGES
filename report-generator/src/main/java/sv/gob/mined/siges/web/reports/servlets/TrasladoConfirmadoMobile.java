/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jose4j.jwt.JwtClaims;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.TrasladoConfirmadoGenerator;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "trasladoConfirmadoMobileServlet ", urlPatterns = "/ma/trasladoConfirmado")
public class TrasladoConfirmadoMobile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TrasladoConfirmadoMobile.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private TrasladoConfirmadoGenerator reportGenerator;

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ClassicEngineBoot.getInstance().start();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {

            String token = getBearerToken(request);

            JwtClaims claims = JWTUtils.validarToken(token, "/publicKey.pem", "SIGES", "SIGES");

            if (!claims.getStringListClaimValue("groups").contains(ConstantesOperaciones.GENERAR_TRASLADO_CONFIRMADO)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            sessionBean.setToken(token);

            Long trasladoId = null;

            if (request.getParameter("trasladoId") != null) {
                trasladoId = Long.parseLong(request.getParameter("trasladoId"));
            }

            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(trasladoId), response.getOutputStream());

        } catch (BusinessException be) {
            response.setStatus(422);
            ServletUtils.printMessage("Traslado confirmado", be, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }
}

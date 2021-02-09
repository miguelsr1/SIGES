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
import sv.gob.mined.siges.web.reports.generator.ConfirmacionMatriculaSedeGenerator;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "confirmacionMatriculaSedeMobileServlet ", urlPatterns = "/ma/confirmacionMatriculaSede")
public class ConfirmacionMatriculaSedeMobile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ConfirmacionMatriculaSedeMobile.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ConfirmacionMatriculaSedeGenerator reportGenerator;

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
            if (!claims.getStringListClaimValue("groups").contains(ConstantesOperaciones.GENERAR_CONFIRMACION_MATRICULA_SEDE)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            sessionBean.setToken(token);


            Long sedePk = null;
            Integer anio = null;
            if (request.getParameter("sedId") != null) {
                sedePk = Long.parseLong(request.getParameter("sedId"));
            }
            if (request.getParameter("a") != null) {
                anio = Integer.parseInt(request.getParameter("a"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(sedePk, anio), response.getOutputStream());
            
        } catch (BusinessException be) {
            response.setStatus(422);
            ServletUtils.printMessage("Confirmacion Matricula Sede", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

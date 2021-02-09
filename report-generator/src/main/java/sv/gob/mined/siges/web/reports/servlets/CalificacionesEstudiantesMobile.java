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
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.CalificacionesEstudiantesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "calificacionesEstudiantesMobileServlet ", urlPatterns = "/ma/calificacionesEstudiantes")
public class CalificacionesEstudiantesMobile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CalificacionesEstudiantesMobile.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CalificacionesEstudiantesGenerator reportGenerator;

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
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            String token = getBearerToken(request);

            JwtClaims claims =JWTUtils.validarToken(token, "/publicKey.pem", "SIGES", "SIGES");

            if (!claims.getStringListClaimValue("groups").contains(ConstantesOperaciones.GENERAR_CALIFICACION_ESTUDIANTE)){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            sessionBean.setToken(token);
            
            Long seccionPk = null;
            Long estudiantePk = null;
            Long componentePlanEstudioPk = null;
            if (request.getParameter("seccionId") != null) {
                seccionPk = Long.parseLong(request.getParameter("seccionId"));
            }
            if (request.getParameter("estudianteId") != null) {
                estudiantePk = Long.parseLong(request.getParameter("estudianteId"));
            }
            if (request.getParameter("componentePlanEstudioId") != null) {
                componentePlanEstudioPk = Long.parseLong(request.getParameter("componentePlanEstudioId"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(seccionPk, estudiantePk, componentePlanEstudioPk), response.getOutputStream());
        } catch (InvalidJwtException ie){
            LOGGER.log(Level.SEVERE, "User address: " + JSFUtils.getRemoteAddress(request) + " - " +  ie.getMessage(), ie);
            response.setStatus(422);
            ServletUtils.printMessage("Permisos insuficientes", ConstantesOperaciones.GENERAR_CALIFICACION_ESTUDIANTE, response);
        } catch (BusinessException be) {
            response.setStatus(422);
            ServletUtils.printMessage("Calificaciones estudiante", be, response);
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

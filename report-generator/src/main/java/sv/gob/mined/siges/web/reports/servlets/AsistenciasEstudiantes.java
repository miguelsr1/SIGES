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
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.AsistenciasEstudiantesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "asistenciasEstudiantesServlet ", urlPatterns = "/wa/asistenciasEstudiantes")
public class AsistenciasEstudiantes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(AsistenciasEstudiantes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AsistenciasEstudiantesGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_ASISTENCIAS_ESTUDIANTE)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_ASISTENCIAS_ESTUDIANTE)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ServletUtils.printMessage("Asistencias estudiante", "Permisos insuficientes", response);
                return;   
            }
            
            Long seccionPk = null;
            Long estudiantePk = null;
            Integer mes = null;
            if (request.getParameter("seccionId") != null) {
                seccionPk = Long.parseLong(request.getParameter("seccionId"));
            }
            if (request.getParameter("estudianteId") != null) {
                estudiantePk = Long.parseLong(request.getParameter("estudianteId"));
            }
            if (request.getParameter("mes") != null) {
                mes = Integer.parseInt(request.getParameter("mes"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(seccionPk, estudiantePk, mes), response.getOutputStream());

        } catch (BusinessException be) {
            response.setStatus(422);
            ServletUtils.printMessage("Calificaciones estudiante", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

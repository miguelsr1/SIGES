/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.io.IOException;
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
import sv.gob.mined.siges.web.reports.generator.CalificacionesHistoricoGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "calificacionHistoricoServlet ", urlPatterns = "/wa/calificacionHistorico")
public class CalificacionesEstudianteHistorico extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CalificacionesEstudianteHistorico.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CalificacionesHistoricoGenerator reportGenerator;
        
        
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ClassicEngineBoot.getInstance().start();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_CALIFICACION_ESTUDIANTE)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }


            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_CALIFICACIONES_HISTORICO)) {
                ServletUtils.printMessage("Calificaciones estudiante", "Permisos insuficientes", response);
                return;   
            }
            
            Integer anio = null;
            Long estudiantePk = null;
            if (request.getParameter("anio") != null) {
                anio = Integer.parseInt(request.getParameter("anio"));
            }
            if (request.getParameter("estudianteId") != null) {
                estudiantePk = Long.parseLong(request.getParameter("estudianteId"));
            }
            
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(anio, estudiantePk), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Calificaciones estudiante", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}

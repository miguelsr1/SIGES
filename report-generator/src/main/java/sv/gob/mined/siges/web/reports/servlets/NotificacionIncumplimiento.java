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
import org.apache.commons.lang3.StringUtils;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.NotificacionIncumplimientoGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "notificacionIncumplimientoServlet ", urlPatterns = "/wa/notificacionIncumplimiento")
public class NotificacionIncumplimiento extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(NotificacionIncumplimiento.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NotificacionIncumplimientoGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_NOTIFICACION_INCUMPLIMIENTO)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_NOTIFICACION_INCUMPLIMIENTO)) {
                ServletUtils.printMessage("Notificación de incumplimiento", "Permisos insuficientes", response);
                return;   
            }
     
            Long notificacionId = null;
            Long unidadAD = null;
            Long unidadAF = null;
            TipoUnidad tipoUnidad = null;
            
            if (request.getParameter("not") != null && StringUtils.isNotBlank(request.getParameter("not").trim())) {
                notificacionId = Long.parseLong(request.getParameter("not").trim());
            }
            if (request.getParameter("uaf") != null && StringUtils.isNotBlank(request.getParameter("uaf").trim())) {
                unidadAF = Long.parseLong(request.getParameter("uaf").trim());
            }

            if (request.getParameter("uad") != null && StringUtils.isNotBlank(request.getParameter("uad").trim())) {
                unidadAD = Long.parseLong(request.getParameter("uad").trim());
                tipoUnidad = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            } else  if (request.getParameter("sed") != null && StringUtils.isNotBlank(request.getParameter("sed").trim())) {
                unidadAD = Long.parseLong(request.getParameter("sed").trim());
                tipoUnidad = TipoUnidad.CENTRO_ESCOLAR;
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(tipoUnidad,unidadAF, unidadAD,  notificacionId), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Notificación de incumplimiento.", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
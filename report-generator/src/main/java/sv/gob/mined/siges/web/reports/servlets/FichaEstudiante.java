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
import sv.gob.mined.siges.web.reports.generator.FichaEstudianteGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "fichaEstudianteServlet ", urlPatterns = "/wa/fichaEstudiante")
public class FichaEstudiante extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(FichaEstudiante.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private FichaEstudianteGenerator reportGenerator;
        
        
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
            //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            //response.setHeader("Pragma", "no-cache");
            //response.setDateHeader("Expires", 0);
            
            long currentTime = System.currentTimeMillis();
            long twoMinutes = 2*60*1000L; // Milisegundos
            response.setDateHeader("Expires", currentTime + twoMinutes);
            

           if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_FICHA_ESTUDIANTE)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_FICHA_ESTUDIANTE)) {
                ServletUtils.printMessage("Ficha estudiante", "Permisos insuficientes", response);
                return;   
            }
           
            Long estId = null;
            if (request.getParameter("id") != null) {
                estId = Long.parseLong(request.getParameter("id"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(estId), response.getOutputStream());
            
        } catch (BusinessException be) {
            response.setStatus(422);
            ServletUtils.printMessage("Ficha estudiante.", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
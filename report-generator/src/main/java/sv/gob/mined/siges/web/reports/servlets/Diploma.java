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
import sv.gob.mined.siges.web.reports.generator.DiplomaGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "diplomaServlet ", urlPatterns = "/wa/diploma")
public class Diploma extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(Diploma.class.getName());

    @Inject
    private SessionBean sessionBean;

    
    @Inject
    private DiplomaGenerator reportGenerator;

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
            
                        
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_DIPLOMA)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_DIPLOMA)) {
                ServletUtils.printMessage("Título estudiante", "Permisos insuficientes", response);
                return;
            }
            
            
            String diplomaPk = request.getParameter("diploma");
            String estudiantePk = request.getParameter("estudiante");
           

            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(diplomaPk,estudiantePk), response.getOutputStream());
        } catch (BusinessException be) {
            ServletUtils.printMessage("Titulo estudiante", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
   
}

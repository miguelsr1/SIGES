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
import sv.gob.mined.siges.web.reports.generator.ConvenioCECEGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "convenioCECEServlet ", urlPatterns = "/wa/convenioCECE")
public class ConvenioCECE extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ConvenioCECE.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ConvenioCECEGenerator reportGenerator;
        
        
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

           if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_PLANTILLA_CONVENIO_CECE)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_PLANTILLA_CONVENIO_CECE)) {
                ServletUtils.printMessage("Convenio CDE", "Permisos insuficientes", response);
                return;   
            }
           
            Long sedeId = null;
            if (request.getParameter("id") != null) {
                sedeId = Long.parseLong(request.getParameter("id"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(sedeId), response.getOutputStream());
            
        } catch (BusinessException be) {
            ServletUtils.printMessage("Convenio CDE.", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
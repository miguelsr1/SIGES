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
import sv.gob.mined.siges.web.reports.generator.ReciboTransferenciaGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "reciboTransferenciaServlet ", urlPatterns = "/wa/reciboTransferencia")
public class ReciboTransferenciaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ReciboTransferenciaServlet.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ReciboTransferenciaGenerator reportGenerator;
        
        
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
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {

        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

           if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_PLANTILLA_RECIBO_TRANSFERENCIA)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_PLANTILLA_RECIBO_TRANSFERENCIA)) {
                ServletUtils.printMessage("Recibo de transferencia", "Permisos insuficientes", response);
                return;   
            }
           
            Long transferencia = null;
            if (request.getParameter("id") != null) {
                transferencia = Long.parseLong(request.getParameter("id"));
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(transferencia), response.getOutputStream());
            
        } catch (BusinessException be) {
            ServletUtils.printMessage("Recibo de transferencia.", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
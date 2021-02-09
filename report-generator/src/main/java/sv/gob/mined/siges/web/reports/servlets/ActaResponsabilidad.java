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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.reports.generator.ActaResponsabilidadGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "actaResponsabilidadServlet ", urlPatterns = "/wa/actaResponsabilidad")
public class ActaResponsabilidad extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ActaResponsabilidad.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ActaResponsabilidadGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_RESPONSABILIDAD)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_RESPONSABILIDAD)) {
                ServletUtils.printMessage("Acta de responsabilidad", "Permisos insuficientes", response);
                return;   
            }
     
            Long unidadAF = null;
            Long unidadAD = null;
            Long empleadoId = null;

            if (request.getParameter("uaf") != null && StringUtils.isNotBlank(request.getParameter("uaf").trim())) {
                unidadAF = Long.parseLong(request.getParameter("uaf").trim());
            }
            if (request.getParameter("uad") != null && StringUtils.isNotBlank(request.getParameter("uad").trim())) {
                unidadAD = Long.parseLong(request.getParameter("uad").trim());
            }
            
            if (request.getParameter("empId") != null && StringUtils.isNotBlank(request.getParameter("empId").trim())) {
                empleadoId = Long.parseLong(request.getParameter("empId").trim());
            }
            // Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(unidadAF, unidadAD, empleadoId), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Acta de responsabilidad", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
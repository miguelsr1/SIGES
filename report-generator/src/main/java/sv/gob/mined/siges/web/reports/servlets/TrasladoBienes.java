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
import sv.gob.mined.siges.web.reports.generator.TrasladoBienesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "trasladosBienesServlet ", urlPatterns = "/wa/trasladoBienes")
public class TrasladoBienes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(TrasladoBienes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private TrasladoBienesGenerator reportGenerator;
        
        
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
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_TRASLADOS)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_TRASLADOS)) {
                ServletUtils.printMessage("Traslados de bienes", "Permisos insuficientes", response);
                return;   
            }
            Long unidasAF = null;
            Long idTraslado = null;
            if (request.getParameter("uaf") != null && StringUtils.isNoneBlank(request.getParameter("uaf").trim())) {
                unidasAF = Long.parseLong(request.getParameter("uaf").trim());
            }
            if (request.getParameter("traId") != null && StringUtils.isNoneBlank(request.getParameter("traId").trim())) {
                idTraslado = Long.parseLong(request.getParameter("traId").trim());
            }    
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(unidasAF, idTraslado), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Traslado de Bienes", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
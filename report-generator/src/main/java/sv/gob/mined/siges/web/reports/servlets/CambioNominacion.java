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
import sv.gob.mined.siges.web.reports.generator.CambioNominacionGenerator;
import sv.gob.mined.siges.web.simple.SgCambioNominacion;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "CambioNominacionServlet ", urlPatterns = "/wa/cambioNominacion")
public class CambioNominacion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CambioNominacion.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CambioNominacionGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CAMBIO_NOMINACION)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CAMBIO_NOMINACION)) {
                ServletUtils.printMessage(CambioNominacion.class.getName(), "Permisos insuficientes", response);
                return;   
            }
     
            SgCambioNominacion cn = new SgCambioNominacion();
            if (request.getParameter("nombreAprobado") != null && StringUtils.isNotBlank(request.getParameter("nombreAprobado").trim())) {
                cn.setNombreAprobado(request.getParameter("nombreAprobado").trim());
            }     
            if (request.getParameter("numero") != null && StringUtils.isNotBlank(request.getParameter("numero").trim())) {
                cn.setNumero(request.getParameter("numero").trim());
            }
            if (request.getParameter("nombreCompleto") != null && StringUtils.isNotBlank(request.getParameter("nombreCompleto").trim())) {
                cn.setNombreCompleto(request.getParameter("nombreCompleto").trim());
            }
            if (request.getParameter("sedPk") != null && StringUtils.isNotBlank(request.getParameter("sedPk").trim())) {
                cn.setSedePk(Long.parseLong(request.getParameter("sedPk").trim()));
            }                                  
            
            //  Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(cn),response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage(CambioNominacion.class.getName(), be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
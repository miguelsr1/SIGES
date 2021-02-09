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
import sv.gob.mined.siges.web.reports.generator.CambioDomicilioGenerator;
import sv.gob.mined.siges.web.simple.SgCambioDomicilio;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "CambioDomicilioServlet ", urlPatterns = "/wa/cambioDomicilio")
public class CambioDomicilio extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CambioDomicilio.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CambioDomicilioGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CAMBIO_DOMICILIO)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CAMBIO_DOMICILIO)) {
                ServletUtils.printMessage(CambioDomicilio.class.getName(), "Permisos insuficientes", response);
                return;   
            }
     
            SgCambioDomicilio cd = new SgCambioDomicilio();
            if (request.getParameter("numero") != null && StringUtils.isNotBlank(request.getParameter("numero").trim())) {
                cd.setNumero(request.getParameter("numero").trim());
            }
            if (request.getParameter("nombreCompleto") != null && StringUtils.isNotBlank(request.getParameter("nombreCompleto").trim())) {
                cd.setNombreCompleto(request.getParameter("nombreCompleto").trim());
            }
            if (request.getParameter("sedPk") != null && StringUtils.isNotBlank(request.getParameter("sedPk").trim())) {
                cd.setSedePk(Long.parseLong(request.getParameter("sedPk").trim()));
            }
            if (request.getParameter("zonaPk") != null && StringUtils.isNotBlank(request.getParameter("zonaPk").trim())) {
                cd.setZonaPk(Long.parseLong(request.getParameter("zonaPk").trim()));
            }
            if (request.getParameter("departamentoPk") != null && StringUtils.isNotBlank(request.getParameter("departamentoPk").trim())) {
                cd.setDepartamentoNuevoPk(Long.parseLong(request.getParameter("departamentoPk").trim()));
            }
            if (request.getParameter("municipioPk") != null && StringUtils.isNotBlank(request.getParameter("municipioPk").trim())) {
                cd.setMunicipioNuevoPk(Long.parseLong(request.getParameter("municipioPk").trim()));
            }
            if (request.getParameter("cantonPk") != null && StringUtils.isNotBlank(request.getParameter("cantonPk").trim())) {
                cd.setCantonNuevoPk(Long.parseLong(request.getParameter("cantonPk").trim()));
            }
            if (request.getParameter("caserio") != null && StringUtils.isNotBlank(request.getParameter("caserio").trim())) {
                cd.setCaserioNuevo(request.getParameter("caserio").trim());
            }
            if (request.getParameter("direccion") != null && StringUtils.isNotBlank(request.getParameter("direccion").trim())) {
                cd.setDireccionNuevo(request.getParameter("direccion").trim());
            }
            if (request.getParameter("telefono") != null && StringUtils.isNotBlank(request.getParameter("telefono").trim())) {
                cd.setTelefonoNuevo(request.getParameter("telefono").trim());
            }            
                        
            //  Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(cd),response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage(CambioDomicilio.class.getName(), be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
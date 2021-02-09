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
import sv.gob.mined.siges.web.reports.generator.AmpliacionServicioGenerator;
import sv.gob.mined.siges.web.simple.SgAmpliacionServicio;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "AmpliarcionServicioServlet ", urlPatterns = "/wa/ampliacionServicio")
public class AmpliacionServicio extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(AmpliacionServicio.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AmpliacionServicioGenerator reportGenerator;
        
        
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

            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_AMPLIACION_SERVICIO)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }
            
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_AMPLIACION_SERVICIO)) {
                ServletUtils.printMessage(AmpliacionServicio.class.getName(), "Permisos insuficientes", response);
                return;   
            }
     
            SgAmpliacionServicio ampliacion = new SgAmpliacionServicio();
            
            //String servicios_educativos:@@servicios_educativos_a_solicitar,
            
            ampliacion.setDepartamento(request.getParameter("departamento"));
           
            if (request.getParameter("sedPk") != null) {                
                ampliacion.setSedId(Long.parseLong(request.getParameter("sedPk")));           
            }
            if (request.getParameter("numero") != null && StringUtils.isNotBlank(request.getParameter("numero").trim())) {
                ampliacion.setNumero(request.getParameter("numero").trim());
            }
            if (request.getParameter("nombre_completo") != null && StringUtils.isNotBlank(request.getParameter("nombre_completo").trim())) {
                ampliacion.setNombreCompleto(request.getParameter("nombre_completo").trim());
            }            
            if (request.getParameter("correo_electronico") != null && StringUtils.isNotBlank(request.getParameter("correo_electronico").trim())) {
                ampliacion.setCorreoElectronico(request.getParameter("correo_electronico").trim());
            }
            if (request.getParameter("telefono_de_contacto") != null && StringUtils.isNotBlank(request.getParameter("telefono_de_contacto").trim())) {
                ampliacion.setTelefonoContacto(request.getParameter("telefono_de_contacto").trim());
            }            
            
            //  Conversion to PDF and rendering.
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(ampliacion),response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage(AmpliacionServicio.class.getName(), be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.time.LocalDate;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;
import sv.gob.mined.siges.web.reports.generator.ActaDescargoGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "actaDescargoBienes ", urlPatterns = "wa/actaDescargoBienes")
public class ActaDescargo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(ActaDescargo.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ActaDescargoGenerator reportGenerator;
        
        
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
        LocalDate fechaAcuerdo = null;
        String numeroAcuerdo = null;
        String seAutoriza = null;
        try {
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_DESCARGO)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_DESCARGO)) {
                ServletUtils.printMessage("Acta de descargo de bienes", "Permisos insuficientes", response);
                return;   
            }
            
            FiltroDescargosDetalle filtro = new FiltroDescargosDetalle();
            // ID EN TABLAS
            if (request.getParameter("uad") != null && StringUtils.isNoneBlank(request.getParameter("uad").trim())) {
                filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                filtro.setUnidadAdministrativaId(Long.parseLong(request.getParameter("uad").trim()));
            } else if (request.getParameter("sed") != null && StringUtils.isNoneBlank(request.getParameter("sed").trim())) {
                filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                filtro.setUnidadAdministrativaId(Long.parseLong(request.getParameter("sed").trim()));
            }
            
            if (request.getParameter("desId") != null && StringUtils.isNoneBlank(request.getParameter("desId").trim())) {
                filtro.setDescargoId(Long.parseLong(request.getParameter("desId").trim()));
            }
            
            if (request.getParameter("uaf") != null && StringUtils.isNoneBlank(request.getParameter("uaf").trim())) {
                filtro.setUnidadActivoFijoId(Long.parseLong(request.getParameter("uaf").trim()));
            }        

            /**
            if (request.getParameter("fAcuerdo") != null && StringUtils.isNoneBlank(request.getParameter("fAcuerdo").trim())) {
                fechaAcuerdo = LocalDate.parse(request.getParameter("fAcuerdo").trim());
            }
            
            if (request.getParameter("nAcuerdo") != null && StringUtils.isNoneBlank(request.getParameter("nAcuerdo").trim())) {
                numeroAcuerdo = request.getParameter("nAcuerdo").trim();
            }
            
            if (request.getParameter("sAutoriza") != null && StringUtils.isNoneBlank(request.getParameter("sAutoriza").trim())) {
                seAutoriza = request.getParameter("sAutoriza").trim();
            }
            **/
            
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(filtro), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Acta de descargo", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
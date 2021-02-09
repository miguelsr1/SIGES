/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.servlets;

import java.math.BigDecimal;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.reports.generator.DepreciacionSubcuentasContablesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "depSubCuentasContablesBienesServlet ", urlPatterns = "/wa/bienessubcontables")
public class DepreciacionSubcuentasContables extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CargosBienes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DepreciacionSubcuentasContablesGenerator reportGenerator;
        
        
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
            
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_BIENES_SUBCUENTAS_CONTABLES)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_BIENES_SUBCUENTAS_CONTABLES)) {
                ServletUtils.printMessage("Bienes por Subcuentas Contables", "Permisos insuficientes", response);
                return;   
            }
            
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            // ID EN TABLAS
            if (request.getParameter("tunidad") != null && StringUtils.isNoneBlank(request.getParameter("tunidad").trim())) {
                 if(request.getParameter("tunidad").trim().equals("ua")) {
                     filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                 } else if(request.getParameter("tunidad").trim().equals("ce")) {
                     filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                 }
            }
            if (request.getParameter("uad") != null && StringUtils.isNoneBlank(request.getParameter("uad").trim())) {
                filtro.setUnidadAdministrativaId(Long.parseLong(request.getParameter("uad").trim()));
            } else if (request.getParameter("sed") != null && StringUtils.isNoneBlank(request.getParameter("sed").trim())) {
                filtro.setUnidadAdministrativaId(Long.parseLong(request.getParameter("sed").trim()));
            }
            
            if (request.getParameter("uaf") != null && StringUtils.isNoneBlank(request.getParameter("uaf").trim())) {
                filtro.setUnidadActivoFijoId(Long.parseLong(request.getParameter("uaf").trim()));
            }
            
            if (request.getParameter("depId") != null && StringUtils.isNoneBlank(request.getParameter("depId").trim())) {
                filtro.setDepartamentoId(Long.parseLong(request.getParameter("depId").trim()));
            }
            
            if (request.getParameter("clasId") != null && StringUtils.isNoneBlank(request.getParameter("clasId").trim())) {
                filtro.setClasificacionId(Long.parseLong(request.getParameter("clasId").trim()));
            }
             
            if (request.getParameter("catId") != null && StringUtils.isNoneBlank(request.getParameter("catId").trim())) {
                filtro.setCategoriaId(Long.parseLong(request.getParameter("catId").trim()));
            }

            if (request.getParameter("fuenteId") != null && StringUtils.isNoneBlank(request.getParameter("fuenteId").trim())) {
                filtro.setFuenteId(Long.parseLong(request.getParameter("fuenteId").trim()));
            }
            
            if (request.getParameter("proyectoId") != null && StringUtils.isNoneBlank(request.getParameter("proyectoId").trim())) {
                filtro.setProyectoId(Long.parseLong(request.getParameter("proyectoId").trim()));
            }
            // VALORES DE FECHA
            if (request.getParameter("fechaAdqDesde") != null && StringUtils.isNoneBlank(request.getParameter("fechaAdqDesde").trim())) {
                filtro.setFechaAdquisicionDesde(LocalDate.parse(request.getParameter("fechaAdqDesde").trim()));
            }

            if (request.getParameter("fechaAdqHasta") != null && StringUtils.isNoneBlank(request.getParameter("fechaAdqHasta").trim())) {
                filtro.setFechaAdquisicionHasta(LocalDate.parse(request.getParameter("fechaAdqHasta").trim()));
            }

            // VALORES BGIDECIMAL
            if (request.getParameter("valAdqDesde") != null && StringUtils.isNoneBlank(request.getParameter("valAdqDesde").trim())) {
                filtro.setValorAdquisicionDesde(new BigDecimal(request.getParameter("valAdqDesde").trim()));
            }
            
            if (request.getParameter("valAdqHasta") != null && StringUtils.isNoneBlank(request.getParameter("valAdqHasta").trim())) {
                filtro.setValorAdquisicionHasta(new BigDecimal(request.getParameter("valAdqHasta").trim()));
            }
            //VALOR BOOLEANO
            if (request.getParameter("activos") != null && StringUtils.isNoneBlank(request.getParameter("activos").trim())) {
                filtro.setActivos(Boolean.parseBoolean(request.getParameter("activos").trim()));
            }
            
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(filtro), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Depreciación por subcuentas contables", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
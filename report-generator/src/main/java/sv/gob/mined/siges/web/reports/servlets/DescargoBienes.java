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
import sv.gob.mined.siges.web.reports.generator.DescargoBienesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "descargosBienesServlet ", urlPatterns = "/wa/descargoBienes")
public class DescargoBienes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(DescargoBienes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DescargoBienesGenerator reportGenerator;
        
        
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
        Integer opcion = 0;
        try {
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_DESCARGOS)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_DESCARGOS)) {
                ServletUtils.printMessage("Descargo de bienes", "Permisos insuficientes", response);
                return;   
            }
            
            FiltroDescargosDetalle filtro = new FiltroDescargosDetalle();
            // ID EN TABLAS
            if (request.getParameter("tu") != null && StringUtils.isNoneBlank(request.getParameter("tu").trim())) {
                 if(request.getParameter("tu").trim().equals("ua")) {
                     filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                 } else if(request.getParameter("tu").trim().equals("ce")) {
                     filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                 }
            }
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
            
            if (request.getParameter("depId") != null && StringUtils.isNoneBlank(request.getParameter("depId").trim())) {
                filtro.setDepartamentoId(Long.parseLong(request.getParameter("depId").trim()));
            }
            
            if (request.getParameter("munId") != null && StringUtils.isNoneBlank(request.getParameter("munId").trim())) {
                filtro.setMunicipioId(Long.parseLong(request.getParameter("munId").trim()));
            }
            
            if (request.getParameter("estadoId") != null && StringUtils.isNoneBlank(request.getParameter("estadoId").trim())) {
                filtro.setEstadoId(Long.parseLong(request.getParameter("estadoId").trim()));
            }
            if (request.getParameter("tdesId") != null && StringUtils.isNoneBlank(request.getParameter("tdesId").trim())) {
                filtro.setTipoDescargo(Long.parseLong(request.getParameter("tdesId").trim()));
            }
            
            if (request.getParameter("clasId") != null && StringUtils.isNoneBlank(request.getParameter("clasId").trim())) {
                filtro.setClasificacionId(Long.parseLong(request.getParameter("clasId").trim()));
            }
            
            if (request.getParameter("catId") != null && StringUtils.isNoneBlank(request.getParameter("catId").trim())) {
                filtro.setCategoriaId(Long.parseLong(request.getParameter("catId").trim()));
            }
            
            
            // VALORES STRING
            if (request.getParameter("codInventario") != null && StringUtils.isNoneBlank(request.getParameter("codInventario").trim())) {
                filtro.setCodigoInventario(request.getParameter("codInventario").trim());
            }   
            if (request.getParameter("nacta") != null && StringUtils.isNoneBlank(request.getParameter("nacta").trim())) {
                filtro.setNumeroActa(request.getParameter("nacta").trim());
            }
            if (request.getParameter("codDes") != null && StringUtils.isNoneBlank(request.getParameter("codDes").trim())) {
                filtro.setCodigoDescargo(request.getParameter("codDes").trim());
            }
            // VALORES BOOLEANOS
            if (request.getParameter("activos") != null && StringUtils.isNoneBlank(request.getParameter("activos").trim())) {
                filtro.setActivos(Boolean.parseBoolean(request.getParameter("activos").trim()));
            }
            
            if (request.getParameter("fdesDesde") != null && StringUtils.isNoneBlank(request.getParameter("fdesDesde").trim())) {
                filtro.setFechaDescargoDesde(LocalDate.parse(request.getParameter("fdesDesde").trim()));
            }

            if (request.getParameter("fdesHasta") != null && StringUtils.isNoneBlank(request.getParameter("fdesHasta").trim())) {
                filtro.setFechaDescargoHasta(LocalDate.parse(request.getParameter("fdesHasta").trim()));
            }  
            if (request.getParameter("fsolDesde") != null && StringUtils.isNoneBlank(request.getParameter("fsolDesde").trim())) {
                filtro.setFechaSolicitudDesde(LocalDate.parse(request.getParameter("fsolDesde").trim()));
            }

            if (request.getParameter("fsolHasta") != null && StringUtils.isNoneBlank(request.getParameter("fsolHasta").trim())) {
                filtro.setFechaSolicitudHasta(LocalDate.parse(request.getParameter("fsolHasta").trim()));
            }
            if (request.getParameter("op") != null && StringUtils.isNoneBlank(request.getParameter("op").trim())) {
                opcion = Integer.parseInt(request.getParameter("op").trim());
            } 
           
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(filtro, opcion), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Descargo de Bienes", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
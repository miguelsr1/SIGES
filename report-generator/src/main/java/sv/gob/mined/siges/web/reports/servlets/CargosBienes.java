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
import sv.gob.mined.siges.web.reports.generator.CargosBienesGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "cargosBienesServlet ", urlPatterns = "/wa/cargoBienes")
public class CargosBienes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CargosBienes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CargosBienesGenerator reportGenerator;
        
        
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
            if (sessionBean.getUserCode() == null || !sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CARGOS)) {
                sessionBean.setUserIp(JSFUtils.getRemoteAddress(request));
                sessionBean.setUser(request.getUserPrincipal());
            }

            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_CARGOS)) {
                ServletUtils.printMessage("Cargo de bienes", "Permisos insuficientes", response);
                return;   
            }
            LocalDate fechaReporte = null;
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            // ID EN TABLAS
            if (request.getParameter("tu") != null && StringUtils.isNoneBlank(request.getParameter("tu").trim())) {
                 if(request.getParameter("tu").trim().equals("ua")) {
                      filtro.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                 } else if(request.getParameter("tu").trim().equals("ce")) {
                      filtro.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                 }
            }

            if (request.getParameter("uaf") != null && StringUtils.isNotBlank(request.getParameter("uaf").trim())) {
                filtro.setUnidadActivoFijoId(Long.parseLong(request.getParameter("uaf").trim()));
            }
                   
            if (request.getParameter("uad") != null && StringUtils.isNotBlank(request.getParameter("uad").trim())) {
                filtro.setUnidadAdministrativaId(Long.parseLong(request.getParameter("uad").trim()));
            }
            
            if (request.getParameter("depId") != null && StringUtils.isNoneBlank(request.getParameter("depId").trim())) {
                filtro.setDepartamentoId(Long.parseLong(request.getParameter("depId").trim()));
            }
            
            if (request.getParameter("munId") != null && StringUtils.isNoneBlank(request.getParameter("munId").trim())) {
                filtro.setMunicipioId(Long.parseLong(request.getParameter("munId").trim()));
            }
            
            if (request.getParameter("catId") != null && StringUtils.isNoneBlank(request.getParameter("catId").trim())) {
                filtro.setCategoriaId(Long.parseLong(request.getParameter("catId").trim()));
            }
            
            if (request.getParameter("tipoBienId") != null && StringUtils.isNoneBlank(request.getParameter("tipoBienId").trim())) {
                filtro.setTipoBienId(Long.parseLong(request.getParameter("tipoBienId").trim()));
            }
            
            if (request.getParameter("calidadId") != null && StringUtils.isNoneBlank(request.getParameter("calidadId").trim())) {
                filtro.setCalidadId(Long.parseLong(request.getParameter("calidadId").trim()));
            }
            
            if (request.getParameter("estadoId") != null && StringUtils.isNoneBlank(request.getParameter("estadoId").trim())) {
                filtro.setEstadoId(Long.parseLong(request.getParameter("estadoId").trim()));
            }
            
            if (request.getParameter("formaAdqId") != null && StringUtils.isNoneBlank(request.getParameter("formaAdqId").trim())) {
                filtro.setFormaAdquisicionId(Long.parseLong(request.getParameter("formaAdqId").trim()));
            }

            if (request.getParameter("fuenteId") != null && StringUtils.isNoneBlank(request.getParameter("fuenteId").trim())) {
                filtro.setFuenteId(Long.parseLong(request.getParameter("fuenteId").trim()));
            }
            
            if (request.getParameter("proyectoId") != null && StringUtils.isNoneBlank(request.getParameter("proyectoId").trim())) {
                filtro.setProyectoId(Long.parseLong(request.getParameter("proyectoId").trim()));
            }
            if (request.getParameter("empId") != null && StringUtils.isNoneBlank(request.getParameter("empId").trim())) {
                filtro.setEmpleadoId(Long.parseLong(request.getParameter("empId").trim()));
            }
            
            // VALORES STRING
            if (request.getParameter("codInventario") != null && StringUtils.isNoneBlank(request.getParameter("codInventario").trim())) {
                filtro.setCodigoInventario(request.getParameter("codInventario").trim());
            }
            
            if (request.getParameter("asignadoA") != null && StringUtils.isNoneBlank(request.getParameter("asignadoA").trim())) {
                filtro.setAsignadoA(request.getParameter("asignadoA").trim());
            }
            
            if (request.getParameter("marca") != null && StringUtils.isNoneBlank(request.getParameter("marca").trim())) {
                filtro.setMarca(request.getParameter("marca").trim());
            }
            
            if (request.getParameter("modelo") != null && StringUtils.isNoneBlank(request.getParameter("modelo").trim())) {
                filtro.setModelo(request.getParameter("modelo").trim());
            }
            
            if (request.getParameter("noSerie") != null && StringUtils.isNoneBlank(request.getParameter("noSerie").trim())) {
                filtro.setNoSerie(request.getParameter("noSerie").trim());
            }


            if (request.getParameter("usuarioModif") != null && StringUtils.isNoneBlank(request.getParameter("usuarioModif").trim())) {
                filtro.setUsuarioModificacion(request.getParameter("usuarioModif").trim());
            }
            
            // VALORES BOOLEANOS
            if (request.getParameter("valEstadoSolVacio") != null && StringUtils.isNoneBlank(request.getParameter("valEstadoSolVacio").trim())) {
                filtro.setValidarEstadoSolicitudVacio(Boolean.parseBoolean(request.getParameter("valEstadoSolVacio").trim()));
            }
            
            if (request.getParameter("valEstSol") != null && StringUtils.isNoneBlank(request.getParameter("valEstSol").trim())) {
                filtro.setValidarEstadoSolicitud(Boolean.parseBoolean(request.getParameter("valEstSol").trim()));
            }
            
            if (request.getParameter("activos") != null && StringUtils.isNoneBlank(request.getParameter("activos").trim())) {
                filtro.setValidarEstadoSolicitudVacio(Boolean.parseBoolean(request.getParameter("activos").trim()));
            }
 
            // VALORES BGIDECIMAL
            if (request.getParameter("valAdqDesde") != null && StringUtils.isNoneBlank(request.getParameter("valAdqDesde").trim())) {
                filtro.setValorAdquisicionDesde(new BigDecimal(request.getParameter("valAdqDesde").trim()));
            }
            
            if (request.getParameter("valAdqHasta") != null && StringUtils.isNoneBlank(request.getParameter("valAdqHasta").trim())) {
                filtro.setValorAdquisicionHasta(new BigDecimal(request.getParameter("valAdqHasta").trim()));
            }

            // VALORES DE FECHA
            if (request.getParameter("fechaAdqDesde") != null && StringUtils.isNoneBlank(request.getParameter("fechaAdqDesde").trim())) {
                filtro.setFechaAdquisicionDesde(LocalDate.parse(request.getParameter("fechaAdqDesde").trim()));
            }


            if (request.getParameter("fechaAdqHasta") != null && StringUtils.isNoneBlank(request.getParameter("fechaAdqHasta").trim())) {
                filtro.setFechaAdquisicionHasta(LocalDate.parse(request.getParameter("fechaAdqHasta").trim()));
            }

            
            if (request.getParameter("fechaCreaDesde") != null && StringUtils.isNoneBlank(request.getParameter("fechaCreaDesde").trim())) {
                filtro.setFechaCreacionDesde(LocalDate.parse(request.getParameter("fechaCreaDesde").trim()));
            }
            
            if (request.getParameter("fechaCreaHasta") != null && StringUtils.isNoneBlank(request.getParameter("fechaCreaHasta").trim())) {
                filtro.setFechaCreacionHasta(LocalDate.parse(request.getParameter("fechaCreaHasta").trim()));
            }
            
            if (request.getParameter("fechaModifDesde") != null && StringUtils.isNoneBlank(request.getParameter("fechaModifDesde").trim())) {
                filtro.setFechaModificacionDesde(LocalDate.parse(request.getParameter("fechaModifDesde").trim()));
            }
            
            if (request.getParameter("fechaModifHasta") != null && StringUtils.isNoneBlank(request.getParameter("fechaModifHasta").trim())) {
                filtro.setFechaModificacionHasta(LocalDate.parse(request.getParameter("fechaModifHasta").trim()));
            }
            if (request.getParameter("fechaReporte") != null && StringUtils.isNoneBlank(request.getParameter("fechaReporte").trim())) {
                fechaReporte = LocalDate.parse(request.getParameter("fechaReporte").trim());
            }
                    
            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(filtro, fechaReporte), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Reporte de Bienes Muebles", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
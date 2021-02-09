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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.reports.generator.VehiculosCargosGenerator;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ServletUtils;

@WebServlet(name = "vehiculosCargosBienesServlet ", urlPatterns = "/wa/vehiculosCargoBienes")
public class VehiculosCargoBienes extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CargosBienes.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private VehiculosCargosGenerator reportGenerator;
        
        
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
            
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            filtro.setSoloVehiculos(Boolean.TRUE);
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
            
            if (request.getParameter("uaf") != null && StringUtils.isNoneBlank(request.getParameter("uaf").trim())) {
                filtro.setUnidadActivoFijoId(Long.parseLong(request.getParameter("uaf").trim()));
            }
            
            if (request.getParameter("depId") != null && StringUtils.isNoneBlank(request.getParameter("depId").trim())) {
                filtro.setDepartamentoId(Long.parseLong(request.getParameter("depId").trim()));
            }
            
            if (request.getParameter("munId") != null && StringUtils.isNoneBlank(request.getParameter("munId").trim())) {
                filtro.setMunicipioId(Long.parseLong(request.getParameter("munId").trim()));
            }
            if (request.getParameter("fuenteId") != null && StringUtils.isNoneBlank(request.getParameter("fuenteId").trim())) {
                filtro.setFuenteId(Long.parseLong(request.getParameter("fuenteId").trim()));
            }
            
            if (request.getParameter("estadoId") != null && StringUtils.isNoneBlank(request.getParameter("estadoId").trim())) {
                filtro.setEstadoId(Long.parseLong(request.getParameter("estadoId").trim()));
            }
            
            if (request.getParameter("proyectoId") != null && StringUtils.isNoneBlank(request.getParameter("proyectoId").trim())) {
                filtro.setProyectoId(Long.parseLong(request.getParameter("proyectoId").trim()));
            }
            // VALORES STRING
            if (request.getParameter("codInventario") != null && StringUtils.isNoneBlank(request.getParameter("codInventario").trim())) {
                filtro.setCodigoInventario(request.getParameter("codInventario").trim());
            }
            
            if (request.getParameter("motor") != null && StringUtils.isNoneBlank(request.getParameter("motor").trim())) {
                filtro.setMotor(request.getParameter("motor").trim());
            }
            
            if (request.getParameter("chasis") != null && StringUtils.isNoneBlank(request.getParameter("chasis").trim())) {
                filtro.setChasis(request.getParameter("chasis").trim());
            }
            
            if (request.getParameter("placa") != null && StringUtils.isNoneBlank(request.getParameter("placa").trim())) {
                filtro.setPlaca(request.getParameter("placa").trim());
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

            response.setContentType("application/pdf");
            PdfReportUtil.createPDF(reportGenerator.generarReporte(filtro), response.getOutputStream());

        } catch (BusinessException be) {
            ServletUtils.printMessage("Reporte de Vehiculos", be, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
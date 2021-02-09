/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.web.dto.SgPlanCompras;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanCompras;
import sv.gob.mined.siges.web.restclient.PlanComprasRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;
//import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class PlanDeCompraGenerator {

    @Inject
    private PlanComprasRestClient planComprasRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;

    public MasterReport generarReporte(Long planId) throws Exception {
        try {

            SgEncabezadoPlanCompras plan = new SgEncabezadoPlanCompras();
            BusinessException be = new BusinessException();
            if (planId == null) {
                be.addError("Debes ingresar la id del plan de compras.");
                throw be;
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_LIQUIDACION);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_LIQUIDACION + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("plan_de_compra.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            //Busqueda de Plan de compras
            FiltroEncabezadoPlanCompra filtroEnc = new FiltroEncabezadoPlanCompra();
            filtroEnc.setIncluirCampos(new String[]{
                "plaPk", "plaVersion",
                "plaPresupuestoFk.pesSedeFk.sedPk", "plaPresupuestoFk.pesSedeFk.sedCodigo", "plaPresupuestoFk.pesSedeFk.sedNombre",
                "plaPresupuestoFk.pesSedeFk.sedTipo", "plaPresupuestoFk.pesSedeFk.sedDireccion.dirDireccion",
                "plaPresupuestoFk.pesSedeFk.sedDireccion.dirDepartamento.depNombre",
                "plaPresupuestoFk.pesSedeFk.sedDireccion.dirMunicipio.munNombre",
                "plaPresupuestoFk.pesAnioFiscalFk.aniAnio", "plaUltModFecha"
            });
            filtroEnc.setPlaPk(planId);
            List<SgEncabezadoPlanCompras> listEncPlan = planComprasRestClient.buscarPlan(filtroEnc);

            if (listEncPlan != null && !listEncPlan.isEmpty() && listEncPlan.get(0) != null) {
                plan = listEncPlan.get(0);
            } else {
                //be.addError(Mensajes.obtenerMensaje(Mensajes.LIQUIDACION_NO_ENCONTRADA));
                throw be;
            }

            FiltroPlanCompras filtroPlan = new FiltroPlanCompras();
            filtroPlan.setComPresupuestoFk(plan.getPlaPresupuestoFk().getPesPk());
            filtroPlan.setIncluirCampos(new String[]{
                "comPk",
                "comMovimientosFk.movPk",
                "comMovimientosFk.movActividadPk.dpePk",
                "comMovimientosFk.movActividadPk.dpeActividad",
                "comMovimientosFk.movActividadPk.dpeVersion",
                "comMovimientosFk.movAreaInversionPk.adiPk",
                "comMovimientosFk.movAreaInversionPk.adiNombre",
                "comMovimientosFk.movAreaInversionPk.adiVersion",
                "comMovimientosFk.movRubroPk.ruId",
                "comMovimientosFk.movRubroPk.ruNombre",
                "comMovimientosFk.movRubroPk.ruVersion",
                "comMovimientosFk.movFuenteRecursos",
                "comMovimientosFk.movVersion",
                "comInsumoFk.insPk",
                "comInsumoFk.insNombre",
                "comInsumoFk.insVersion",
                "comCantidad",
                "comUnidadMedida",
                "comPrecioUnitario",
                "comMontoTotal",
                "comFechaEstimadaCompra",
                "comVersion"
            });
            List<SgPlanCompras> planes = planComprasRestClient.buscar(filtroPlan);

            if (!planes.isEmpty()) {
                report.setDataFactory(this.getDataFactory(planes));
            }

            report.getParameterValues().put("anio", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesAnioFiscalFk() != null) ? plan.getPlaPresupuestoFk().getPesAnioFiscalFk().getAniAnio().toString() : "");
            report.getParameterValues().put("centro_educativo", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesSedeFk() != null) ? plan.getPlaPresupuestoFk().getPesSedeFk().getSedNombre() : "");
            report.getParameterValues().put("codigo", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesSedeFk() != null) ? plan.getPlaPresupuestoFk().getPesSedeFk().getSedCodigo() : "");
            report.getParameterValues().put("departamento", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion() != null) ? plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion().getDirDepartamento().getDepNombre() : "");
            report.getParameterValues().put("municipio", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion() != null) ? plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion().getDirMunicipio().getMunNombre() : "");
            report.getParameterValues().put("direccion", (plan.getPlaPresupuestoFk() != null && plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion() != null) ? plan.getPlaPresupuestoFk().getPesSedeFk().getSedDireccion().getDirDireccion() : "");
            report.getParameterValues().put("fecha", Generales.getDateTimeFormat(plan.getPlaUltModFecha(), "dd/MM/yyyy"));

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<SgPlanCompras> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("plan_de_compras", TypedTableModel.class);

        TypedTableModel plan_de_compras = new TypedTableModel();
        plan_de_compras.addColumn("area_inversion", String.class);
        plan_de_compras.addColumn("descripcion", String.class);
        plan_de_compras.addColumn("cantidad", String.class);
        plan_de_compras.addColumn("unidad_medida", String.class);
        plan_de_compras.addColumn("precio_unitario", BigDecimal.class);
        plan_de_compras.addColumn("monto_total", BigDecimal.class);
        plan_de_compras.addColumn("fecha_compra", String.class);

        list.forEach(e -> {
            plan_de_compras.addRow(
                    e.getComMovimientosFk().getMovAreaInversionPk().getAdiNombre(), // Area de inversion
                    e.getComMovimientosFk().getMovFuenteRecursos(), // Descripción
                    e.getComCantidad(), // Cantidad
                    e.getComUnidadMedida(), // Unidad de medida
                    e.getComPrecioUnitario(), // Precio Unitario
                    e.getComMontoTotal(), // Total
                    Generales.getDateFormat(e.getComFechaEstimadaCompra(), "dd/MM/yyyy"));  // Fecha
        });

        model.addRow(plan_de_compras);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;

    }

}

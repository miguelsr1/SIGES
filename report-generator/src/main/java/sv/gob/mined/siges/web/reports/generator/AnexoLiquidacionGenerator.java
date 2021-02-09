/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgLiquidacion;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EvaluacionItemLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class AnexoLiquidacionGenerator {

    @Inject
    private LiquidacionRestClient liquidacionRestClient;

    @Inject
    private MovimientoLiquidacionRestClient movimientoLiqRestClient;

    @Inject
    private EvaluacionItemLiquidacionRestClient evaluacionItemLiquidacionRestClient;

    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoAdminRestClient;

    @Inject
    private OrganismoAdministracionEscolarRestClient oaeRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;

    //materialEducativo
    static BigDecimal materialEducativo = BigDecimal.ZERO;
    //adquisicionMobiliario
    static BigDecimal adquisicionMobiliario = BigDecimal.ZERO;
    //adquisicionEquipo
    static BigDecimal adquisicionEquipo = BigDecimal.ZERO;
    //operacionLog
    static BigDecimal operacionLog = BigDecimal.ZERO;
    //alimentacionEscolar
    static BigDecimal alimentacionEscolar = BigDecimal.ZERO;
    //capacitacionLocal
    static BigDecimal capacitacionLocal = BigDecimal.ZERO;
    //contratacionServProf
    static BigDecimal contratacionServProf = BigDecimal.ZERO;
    //otros
    static BigDecimal otros = BigDecimal.ZERO;

    public MasterReport generarReporte(Long liqId) throws Exception {
        try {

            SgLiquidacion liquidacion = new SgLiquidacion();
            SgOrganismoAdministracionEscolar oae = new SgOrganismoAdministracionEscolar();
            SgCuentasBancarias cuentaBanc = new SgCuentasBancarias();
            BusinessException be = new BusinessException();
            if (liqId == null) {
                be.addError("Debes ingresar la id de la liquidación.");
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
                reportDefinitionURL = classloader.getResource("cuadroResumen_v4.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            //Busqueda de Liquidacion
            FiltroLiquidacion filtroLiq = new FiltroLiquidacion();
            filtroLiq.setIncluirCampos(new String[]{
                "liqPk",
                "liqSedePk.sedPk", "liqSedePk.sedCodigo", "liqSedePk.sedNombre", "liqSedePk.sedTipo", "liqSedePk.sedVersion",
                "liqComponentePk.cpeId","liqComponentePk.cpeNombre",
                "liqSubComponenteFk.gesId","liqSubComponenteFk.gesNombre",
                "liqAnioPk.alePk", "liqAnioPk.aleAnio", "liqAnioPk.aleVersion", "liqEstado", "liqVersion"});
            filtroLiq.setLiqPk(liqId);
            List<SgLiquidacion> list = liquidacionRestClient.buscar(filtroLiq);
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                liquidacion = list.get(0);
            }

            if (liquidacion.getLiqPk() == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.LIQUIDACION_NO_ENCONTRADA));
                throw be;
            }

            //ORDEN 1-PRESIDENTE
            String nombrePresidente = StringUtils.EMPTY;
            //ORDEN 2-TESORERO
            String nombreTesorero = StringUtils.EMPTY;
            //ORDEN 3-CONSEJAL
            String nombreConsejal = StringUtils.EMPTY;

            List<String> codigos = new ArrayList();
            codigos.add(Constantes.CODIGO_CDE);

            FiltroPersonaOrganismoAdministrativo fPOA = new FiltroPersonaOrganismoAdministrativo();
            fPOA.setSedeId(liquidacion.getLiqSedePk().getSedPk());
            fPOA.setCodigosTOA(codigos);
            fPOA.setPoaHabilitado(Boolean.TRUE);
            fPOA.setAscending(new boolean[]{true});
            fPOA.setOrderBy(new String[]{"poaCargo.coaOrden"});
            fPOA.setIncluirCampos(new String[]{"poaPersona.perPrimerNombre", "poaPersona.perSegundoNombre", "poaPersona.perTercerNombre", "poaPersona.perPrimerApellido", "poaPersona.perDui",
                "poaPersona.perSegundoApellido", "poaPersona.perTercerApellido", "poaPersona.perOcupacion.ocuNombre", "poaPersona.perDireccion.dirDireccion",
                "poaPersona.perProfesion.proNombre", "poaCargo.coaCodigo", "poaCargo.coaNombre", "poaPrerregistro", "poaNombre", "poaDui", "poaOcupacion", "poaDomicilio"});

            List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar = personaOrganismoAdminRestClient.buscar(fPOA);

            Integer count = 0;
            Boolean preregistrado = Boolean.FALSE;
            for (SgPersonaOrganismoAdministracion poa : listaOrganismoEscolar) {
                if (poa.getPoaCargo() != null && poa.getPoaCargo().getCoaCodigo() != null) {
                    preregistrado = poa.getPoaPrerregistro() != null ? poa.getPoaPrerregistro() : false;
                    if (poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_PRESIDENTE_CDE)) {
                        if (preregistrado) {
                            nombrePresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "";
                        } else {
                            nombrePresidente = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "";
                        }

                        count++;
                    } else if (poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_TESORERO_CDE)) {
                        if (preregistrado) {
                            nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "";
                        } else {
                            nombreTesorero = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "";
                        }

                    } else if (poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_CONSEJAL_CDE)) {
                        if (preregistrado) {
                            nombreConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "";
                        } else {
                            nombreConsejal = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "";
                        }
                    }
                }
                if (count > 2) {
                    break;
                }
            }

            FiltroMovimientoLiquidacion filtroMovLiq = new FiltroMovimientoLiquidacion();
            filtroMovLiq.setMlqLiquidacionPk(liquidacion.getLiqPk());
            filtroMovLiq.setMovimientoTipo(EnumMovimientosTipo.E);
            filtroMovLiq.setIncluirCampos(new String[]{
                "mlqPk", "mlqMovimientoPk.mcbPk",
                "mlqMovimientoPk.mcbFecha", "mlqMovimientoPk.mcbChequeCb",
                "mlqMovimientoPk.mcbDetalle",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facNumero",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facItemMovimiento.movAreaInversionPk.adiPk",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facItemMovimiento.movAreaInversionPk.adiCodigo",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facItemMovimiento.movAreaInversionPk.adiNombre",
                "mlqMovimientoPk.mcbPagoFk.pgsFactura.facItemMovimiento.movAreaInversionPk.adiVersion",
                "mlqMovimientoPk.mcbMonto",
                "mlqMovimientoPk.mcbVersion", "mlqLiquidacionPk.liqPk", "mlqLiquidacionPk.liqVersion",
                "mlqTipoMovimiento", "mlqUltModFecha", "mlqVersion"
            });

            List<SgMovimientoLiquidacion> listMovs = movimientoLiqRestClient.buscar(filtroMovLiq);
            if (!listMovs.isEmpty()) {
                report.setDataFactory(this.getDataFactory(listMovs));
            } else {
                be.addError("SIN MOVIMIENTOS");
                throw be;
            }

            report.getParameterValues().put("anio", liquidacion.getLiqAnioPk().getAleAnio().toString());
            report.getParameterValues().put("nombreSede", liquidacion.getLiqSedePk().getSedNombre());
            report.getParameterValues().put("codigoSede", liquidacion.getLiqSedePk().getSedCodigo());
            report.getParameterValues().put("componente", liquidacion.getLiqComponentePk().getCpeNombre());
            report.getParameterValues().put("subcomponente", liquidacion.getLiqSubComponenteFk().getGesNombre());
            report.getParameterValues().put("nombrePresidente", nombrePresidente);
            report.getParameterValues().put("nombreTesorero", nombreTesorero);
            report.getParameterValues().put("nombreConcejalDocente", nombreConsejal);

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<SgMovimientoLiquidacion> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("cuadroResumen", TypedTableModel.class);

        TypedTableModel cuadroResumen = new TypedTableModel();
        cuadroResumen.addColumn("fechaCheque", String.class);
        cuadroResumen.addColumn("NoCheque", String.class);
        cuadroResumen.addColumn("concepto", String.class);
        cuadroResumen.addColumn("NoFactura", String.class);
        cuadroResumen.addColumn("materialEducativo", BigDecimal.class);
        cuadroResumen.addColumn("adquisicionMobiliario", BigDecimal.class);
        cuadroResumen.addColumn("adquisicionEquipo", BigDecimal.class);
        cuadroResumen.addColumn("operacionLog", BigDecimal.class);
        cuadroResumen.addColumn("alimentacionEscolar", BigDecimal.class);
        cuadroResumen.addColumn("capacitacionLocal", BigDecimal.class);
        cuadroResumen.addColumn("contratacionServProf", BigDecimal.class);
        cuadroResumen.addColumn("otros", BigDecimal.class);
        cuadroResumen.addColumn("total", BigDecimal.class);

        list.stream().filter(l -> l.getMlqMovimientoPk().getMcbPagoFk() != null)
                .filter(l -> l.getMlqMovimientoPk().getMcbPagoFk().getPgsFactura() != null)
                .filter(l -> l.getMlqMovimientoPk().getMcbPagoFk().getPgsFactura().getFacItemMovimiento() != null)
                .filter(l -> l.getMlqMovimientoPk().getMcbPagoFk().getPgsFactura().getFacItemMovimiento().getMovAreaInversionPk() != null)
                .forEach(l -> {

                    materialEducativo = BigDecimal.ZERO;
                    adquisicionMobiliario = BigDecimal.ZERO;
                    adquisicionEquipo = BigDecimal.ZERO;
                    operacionLog = BigDecimal.ZERO;
                    alimentacionEscolar = BigDecimal.ZERO;
                    capacitacionLocal = BigDecimal.ZERO;
                    contratacionServProf = BigDecimal.ZERO;

                    switch (l.getMlqMovimientoPk().getMcbPagoFk().getPgsFactura().getFacItemMovimiento().getMovAreaInversionPk().getAdiCodigo()) {
                        case Constantes.AREA_MATERIAL_EDUCATIVO:
                            materialEducativo = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_ADQUISICION_MOBILIARIO:
                            adquisicionMobiliario = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_ADQUISICION_EQUIPO:
                            adquisicionEquipo = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_OPERACION_LOGISTICA:
                            operacionLog = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_ALIMENTACION_ESCOLAR:
                            alimentacionEscolar = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_CAPACITACION_LOCAL:
                            capacitacionLocal = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        case Constantes.AREA_CONTRATACION_SERVICIO_PROFESIONAL:
                            contratacionServProf = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                        default:
                            otros = l.getMlqMovimientoPk().getMcbMonto();
                            break;
                    }

                    cuadroResumen.addRow(Generales.getDateTimeFormat(l.getMlqUltModFecha(), "dd/MM/yyyy"),
                            l.getMlqMovimientoPk().getMcbChequeCb(),
                            l.getMlqMovimientoPk().getMcbDetalle(), l.getMlqMovimientoPk().getMcbPagoFk().getPgsFactura().getFacNumero(),
                            materialEducativo, adquisicionMobiliario, adquisicionEquipo, operacionLog, alimentacionEscolar, capacitacionLocal, contratacionServProf, otros,
                            l.getMlqMovimientoPk().getMcbMonto());
                });

        model.addRow(cuadroResumen);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;

    }

}

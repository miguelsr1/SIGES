/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.IngresosEgresosReporte;
import sv.gob.mined.siges.web.dto.LibroIngresosEgresos;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SsFuenteRecursos;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.FuenteRecursosRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class LibroIngresosEgresosGenerator {

    private static final Logger LOGGER = Logger.getLogger(LibroIngresosEgresosGenerator.class.getName());

    @Inject
    private CajaChicaRestClient cajasRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private TransferenciaRestClient trasferenciasRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MovimientosRestClient movRestClient;

    @Inject
    private FuenteRecursosRestClient fuenteRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    private String unidad = new String();
    private String linea = new String();
    private String fuentes = new String();
    private SgCuentasBancarias cuenta;
    private LocalDate currentDate = LocalDate.now();
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;
    private List<String> fuentesLista = new ArrayList();

    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalEgresos = BigDecimal.ZERO;
    private BigDecimal totalSaldo = BigDecimal.ZERO;

    public MasterReport generarReporte(Long sedeId, Integer anioId, Long movTraId, Long componenteId, Long subcomponenteId) throws Exception {
        try {
            SgMovimientos transferencias = new SgMovimientos();
            SgSede datosSede = new SgSede();
            SsCategoriaPresupuestoEscolar componente = new SsCategoriaPresupuestoEscolar();
            SsGesPresEs subComponente = new SsGesPresEs();
            if (componenteId != null && subcomponenteId != null) {
                componente.setCpeId(componenteId);
                subComponente.setGesId(subcomponenteId);
            }
            //SgCajaChica cuentaCaja = new SgCajaChica();
            BusinessException be = new BusinessException();

            if (sedeId == null || anioId == null || movTraId == null) {
                be.addError("Debes seleccionar sede, año fiscal y transferencia");
                throw be;
            }

            LibroIngresosEgresos transferencia = new LibroIngresosEgresos();
            transferencia.setSedePk(sedeId);
            transferencia.setAnio(anioId);
            transferencia.setMovTransferenciaPk(movTraId);

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_LIBRO_INGRESOS_EGRESOS);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_LIBRO_INGRESOS_EGRESOS + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("libroIngresoEgreso.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            //Busqueda de Movimiento Transferencias
            SgMovimientos mov = movRestClient.obtenerPorId(movTraId, true);

            List<SsFuenteRecursos> listFuenteRecurso = fuenteRestClient.buscarFuente(movTraId);
            if (!listFuenteRecurso.isEmpty()) {
                if (listFuenteRecurso.size() > 0) {
                    for (int i = 0; i < listFuenteRecurso.size(); i++) {
                        SsFuenteRecursos che = listFuenteRecurso.get(i);
                        fuentesLista.add(che.getNombre());
                    }

                }

            }

            List<IngresosEgresosReporte> listTransferencias = trasferenciasRestClient.buscarParaReporte(transferencia);
            if (!listTransferencias.isEmpty()) {
                report.setDataFactory(this.getDataFactory(listTransferencias));
            }

            if (componenteId != null && subcomponenteId != null) {
                List<IngresosEgresosReporte> listTransferenciasComponente = trasferenciasRestClient.buscarParaReporteComponentes(transferencia);
                if (!listTransferenciasComponente.isEmpty()) {
                    report.setDataFactory(this.getDataFactory(listTransferenciasComponente));
                }
            }

            //Busqueda de datos de la sede
            FiltroSedes sede = new FiltroSedes();
            sede.setSedPk(sedeId);
            sede.setIncluirCampos(new String[]{"sedPk",
                "sedNombre",
                "sedCodigo",
                "sedTipo",
                "sedDireccion.dirPk",
                "sedDireccion.dirDepartamento.depPk",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirDepartamento.depVersion",
                "sedDireccion.dirVersion",
                "sedVersion"
            });

            List<SgSede> listSede = sedeRestClient.buscar(sede);
            if (!listSede.isEmpty()) {
                datosSede = listSede.get(0);
            } else {
                throw be;
            }

            report.getParameterValues().put("nombreTransferencia", mov.getMovTechoPresupuestal().getSubComponente().getGesCategoriaComponente().getCpeNombre().concat(", ").concat(mov.getMovTechoPresupuestal().getSubComponente().getGesNombre()));
            report.getParameterValues().put("fuenteFinanciamiento", fuentesLista.toString().replace("[", "").replace("]", ""));
            report.getParameterValues().put("anio", anioId.toString());

            report.getParameterValues().put("departamento", datosSede.getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("codigoSede", datosSede.getSedCodigo());
            report.getParameterValues().put("nombreSede", datosSede.getSedNombre());
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<IngresosEgresosReporte> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("libro", TypedTableModel.class);
        model.addColumn("totalIngresoFinal", BigDecimal.class);
        model.addColumn("totalEgresoFinal", BigDecimal.class);
        model.addColumn("totalSaldoFinal", BigDecimal.class);

        TypedTableModel libro = new TypedTableModel();
        libro.addColumn("libroMes", TypedTableModel.class);

        for (Month c : Month.values()) {
            Integer movsMes = 0;

            TypedTableModel libroTraMes = new TypedTableModel();
            libroTraMes.addColumn("fecha", String.class);
            libroTraMes.addColumn("referencia", String.class);
            libroTraMes.addColumn("concepto", String.class);
            libroTraMes.addColumn("ingreso", BigDecimal.class);
            libroTraMes.addColumn("gasto", BigDecimal.class);
            libroTraMes.addColumn("saldo", BigDecimal.class);
            BigDecimal saldoInicial = BigDecimal.ZERO;
            for (IngresosEgresosReporte r : list) {
                if (r.getFecha().getMonth().equals(c)) {
                    libroTraMes.addRow(
                            Generales.getDateTimeFormat(r.getFecha(), "dd/MM/yyyy"), //fecha
                            r.getReferencia().isEmpty() ? "" : r.getReferencia(), //cuenta bancaria
                            r.getConcepto().isEmpty() ? "" : r.getConcepto(), //cuenta bancaria
                            r.getIngreso().compareTo(BigDecimal.ZERO) == 0 ? 0.00 : r.getIngreso(),//ingreso
                            r.getGasto().compareTo(BigDecimal.ZERO) == 0 ? 0.00 : r.getGasto(),//gasto
                            saldoInicial.compareTo(BigDecimal.ZERO) == 0 ? saldoInicial = r.getIngreso().subtract(r.getGasto()) : saldoInicial.add(r.getIngreso().subtract(r.getGasto())),//saldo

                            r.getIngreso().compareTo(BigDecimal.ZERO) == 0 ? 0.00 : (totalIngresos = totalIngresos.add(r.getIngreso())),//ingreso
                            r.getGasto().compareTo(BigDecimal.ZERO) == 0 ? 0.00 : (totalEgresos = totalEgresos.add(r.getGasto())),//ingreso
                            totalSaldo = totalIngresos.subtract(totalEgresos)
                    );
                    movsMes++;
                }
            }
            if (movsMes > 0) {

                libro.addRow(libroTraMes);
            }
        }

        model.addRow(libro, totalIngresos, totalEgresos, totalSaldo);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
}

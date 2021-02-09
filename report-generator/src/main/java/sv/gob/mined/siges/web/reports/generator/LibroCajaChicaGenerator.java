/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
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
import sv.gob.mined.siges.web.dto.CajaChicaReporte;
import sv.gob.mined.siges.web.dto.LibroCajaChica;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgCajaChica;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoCajaChica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class LibroCajaChicaGenerator {

    private static final Logger LOGGER = Logger.getLogger(LibroCajaChicaGenerator.class.getName());

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
    private MovimientoCajaChicaRestClient movimientosCajaRestClient;

    @Inject
    private SessionBean sessionBean;

    private String unidad = new String();
    private String linea = new String();
    private SgCuentasBancarias cuenta;
    private LocalDate currentDate = LocalDate.now();
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;

    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalEgresos = BigDecimal.ZERO;
    private BigDecimal totalSaldo = BigDecimal.ZERO;

    public MasterReport generarReporte(Long cuentaId, Integer anioId) throws Exception {
        try {
            SgMovimientoCajaChica movimientoCuenta = new SgMovimientoCajaChica();
            SgCajaChica cuentaCaja = new SgCajaChica();
            BusinessException be = new BusinessException();

            if (cuentaId == null || anioId == null) {
                be.addError("Debes seleccionar una caja chica y un año fiscal");
                throw be;
            }
            LibroCajaChica caja = new LibroCajaChica();
            caja.setCajaPk(cuentaId);
            caja.setAnio(anioId);

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_LIBRO_CAJA_CHICA);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_LIBRO_CAJA_CHICA + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("libroCajaChica.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            //Busqueda de Movimiento Caja Chica
            FiltroCajaChica filtro = new FiltroCajaChica();
            filtro.setBccPk(cuentaId);
            filtro.setIncluirCampos(new String[]{"bccPk",
                "bccNumeroCuenta",
                "bccTitular",
                "bccSedeFk.sedNombre",
                "bccVersion"
            });

            List<SgCajaChica> list = cajasRestClient.buscar(filtro);

            if (!list.isEmpty()) {
                cuentaCaja = list.get(0);
            }

            List<CajaChicaReporte> listMovCajaChica = movimientosCajaRestClient.buscarParaReporte(caja);
            if (!listMovCajaChica.isEmpty()) {
                report.setDataFactory(this.getDataFactory(listMovCajaChica));
            }

            report.getParameterValues().put("NoCajaChica", cuentaCaja.getBccNumeroCuenta());
            report.getParameterValues().put("anio", anioId.toString());

//            report.getParameterValues().put("departamento", "AHUACHAPAN");
//            report.getParameterValues().put("codigoSede", "10004");
//            report.getParameterValues().put("nombreSede", "CENTRO ESCOLAR \"1° DE JULIO DE 1823\"");
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<CajaChicaReporte> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("libroCaja", TypedTableModel.class);
        model.addColumn("totalIngresoFinal", BigDecimal.class);
        model.addColumn("totalEgresoFinal", BigDecimal.class);
        model.addColumn("totalSaldoFinal", BigDecimal.class);

        TypedTableModel libroCaja = new TypedTableModel();
        libroCaja.addColumn("cajaMes", TypedTableModel.class);

        for (Month c : Month.values()) {
            Integer movsMes = 0;
            TypedTableModel libroCajaMes = new TypedTableModel();
            libroCajaMes.addColumn("fecha", String.class);
            libroCajaMes.addColumn("aNombreDe", String.class);
            libroCajaMes.addColumn("enConceptoDe", String.class);
            libroCajaMes.addColumn("ingreso", BigDecimal.class);
            libroCajaMes.addColumn("gasto", BigDecimal.class);
            libroCajaMes.addColumn("saldo", BigDecimal.class);

            for (CajaChicaReporte r : list) {
                if (r.getFecha().getMonth().equals(c)) {
                    libroCajaMes.addRow(
                            Generales.getDateTimeFormat(r.getFecha(), "dd/MM/yyyy"), //fecha
                            r.getaNombreDe(), //nombre
                            r.getDetalle(), //concepto
                            r.getTipo().equals("HABER") ? r.getMonto() : 0.00, //ingreso
                            r.getTipo().equals("DEBE") ? r.getMonto() : 0.00, //egreso
                            r.getSaldo(),
                            r.getTipo().equals("HABER") ? totalIngresos = totalIngresos.add(r.getMonto()) : 0.00, //suma de ingresos
                            r.getTipo().equals("DEBE") ? totalEgresos = totalEgresos.add(r.getMonto()) : 0.00, //suma de egresos
                            totalSaldo = totalIngresos.subtract(totalEgresos)
                    );
                    movsMes++;
                }
            }
            if (movsMes > 0) {

                libroCaja.addRow(libroCajaMes);
            }
        }

        model.addRow(libroCaja, totalIngresos, totalEgresos, totalSaldo);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
}

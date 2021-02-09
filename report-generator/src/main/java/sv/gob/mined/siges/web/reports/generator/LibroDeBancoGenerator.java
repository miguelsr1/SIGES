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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.CuentaBancariaReporte;
import sv.gob.mined.siges.web.dto.LibroBanco;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class LibroDeBancoGenerator {

    private static final Logger LOGGER = Logger.getLogger(LibroDeBancoGenerator.class.getName());

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private CuentasBancariasRestClient cuentasRestClient;

    @Inject
    private MovimientoCuentaBancariaRestClient movimientosCuentaRestClient;

    private String unidad = new String();
    private String linea = new String();
    private SgCuentasBancarias cuenta;
    private LocalDate currentDate = LocalDate.now();
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;

    private BigDecimal totalIngresos = BigDecimal.ZERO;
    private BigDecimal totalEgresos = BigDecimal.ZERO;
    private BigDecimal totalSaldo = BigDecimal.ZERO;
    private List<String> nombreSubCompoente = new ArrayList();
    private List<String> nombreComponente = new ArrayList();

    public MasterReport generarReporte(Long cuentaId, Integer anioId, Long componenteId, Long subcomponenteId) throws Exception {
        try {
            SgMovimientoCuentaBancaria movimientoCuenta = new SgMovimientoCuentaBancaria();
            SsCategoriaPresupuestoEscolar componente = new SsCategoriaPresupuestoEscolar();
            SsGesPresEs subComponente = new SsGesPresEs();

            if (componenteId != null && subcomponenteId != null) {
                componente.setCpeId(componenteId);
                subComponente.setGesId(subcomponenteId);
            }

            SgCuentasBancarias cuentaBanc = new SgCuentasBancarias();
            SgCuentasBancarias componentes = new SgCuentasBancarias();
            BusinessException be = new BusinessException();

            if (cuentaId == null || anioId == null) {
                be.addError("Debes seleccionar una cuenta bancaria y un año fiscal");
                throw be;
            }
            LibroBanco bac = new LibroBanco();
            bac.setCuentaPk(cuentaId);
            bac.setAnio(anioId);
            if (componenteId != null && subcomponenteId != null) {
                bac.setComponentePk(componenteId);
                bac.setSubComponentePk(subcomponenteId);
            }

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_LIBRO_DE_BANCO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_LIBRO_DE_BANCO + " inexistente.");
                    throw be;   
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("libroDeBanco.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            //Busqueda de Cuenta Bancaria
            FiltroCuentasBancarias filtro = new FiltroCuentasBancarias();
            filtro.setCbcPk(cuentaId);
            filtro.setCbcHabilitado(Boolean.TRUE);
            filtro.setIncluirCampos(new String[]{"cbcPk",
                "cbcNumeroCuenta",
                "cbcTitular",
                "cbcBancoFk.bncNombre",
                "cbcSedeFk.sedCodigo",
                "cbcSedeFk.sedNombre",
                "cbcSedeFk.sedTipo",
                "cbcSedeFk.sedVersion",
                "cbcSedeFk.sedDireccion.dirDepartamento.depPk",
                "cbcSedeFk.sedDireccion.dirDepartamento.depNombre",
                "cbcSedeFk.sedDireccion.dirDepartamento.depVersion",
                "cbcVersion"
            });

            List<SgCuentasBancarias> list = cuentasRestClient.buscar(filtro);

            if (!list.isEmpty()) {
                cuentaBanc = list.get(0);
            }

            List<CuentaBancariaReporte> listMovCuentaBancaria = movimientosCuentaRestClient.buscarParaReporte(bac);

            if (componenteId != null && subcomponenteId != null) {
                List<CuentaBancariaReporte> listComponenteSubcomponente = movimientosCuentaRestClient.buscarComponentes(bac);
                if (!listComponenteSubcomponente.isEmpty()) {
                    for (int i = 0; i < listComponenteSubcomponente.size(); i++) {
                        CuentaBancariaReporte che = listComponenteSubcomponente.get(i);
                        nombreComponente.add(che.getComponente());
                        nombreSubCompoente.add(che.getSubComponente());
                    }
                }
            }

            List<CuentaBancariaReporte> listMovCuentaBancariaComponentes = movimientosCuentaRestClient.buscarParaReporteComponente(bac);

            if (!listMovCuentaBancaria.isEmpty()) {
                report.setDataFactory(this.getDataFactory(listMovCuentaBancaria));
            } else if (!listMovCuentaBancariaComponentes.isEmpty()) {
                report.setDataFactory(this.getDataFactory(listMovCuentaBancariaComponentes));
            }

            report.getParameterValues().put("NoCuenta", cuentaBanc.getCbcNumeroCuenta());
            report.getParameterValues().put("banco", cuentaBanc.getCbcBancoFk().getBncNombre());
            report.getParameterValues().put("anio", anioId.toString());
            report.getParameterValues().put("departamento", cuentaBanc.getCbcSedeFk().getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("codigoSede", cuentaBanc.getCbcSedeFk().getSedCodigo());
            report.getParameterValues().put("nombreSede", cuentaBanc.getCbcSedeFk().getSedNombre());

            report.getParameterValues().put("componente", nombreComponente.toString().replace("[", "").replace("]", ""));
            report.getParameterValues().put("subcomponente", nombreSubCompoente.toString().replace("[", "").replace("]", ""));

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<CuentaBancariaReporte> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("libroBanco", TypedTableModel.class);
        model.addColumn("totalIngreso", BigDecimal.class);
        model.addColumn("totalEgreso", BigDecimal.class);
        model.addColumn("totalSaldoFinal", BigDecimal.class);

        TypedTableModel libroBanco = new TypedTableModel();
        libroBanco.addColumn("libroBancoMes", TypedTableModel.class);

        for (Month c : Month.values()) {
            Integer movsMes = 0;
            TypedTableModel libroBancoMes = new TypedTableModel();

            libroBancoMes.addColumn("fecha", String.class);
            libroBancoMes.addColumn("chequeNo", String.class);
            libroBancoMes.addColumn("aNombreDe", String.class);
            libroBancoMes.addColumn("enConceptoDe", String.class);
            libroBancoMes.addColumn("ingreso", BigDecimal.class);
            libroBancoMes.addColumn("gasto", BigDecimal.class);
            libroBancoMes.addColumn("saldo", BigDecimal.class);

            for (CuentaBancariaReporte r : list) {
                if (r.getFecha().getMonth().equals(c)) {
                    libroBancoMes.addRow(
                            Generales.getDateTimeFormat(r.getFecha(), "dd/MM/yyyy"), //fecha
                            r.getNoCheque().isEmpty() ? "" : r.getNoCheque(), //cuenta bancaria
                            r.getaNombreDe().isEmpty() ? "" : r.getaNombreDe(),//nombre
                            r.getDetalle().isEmpty() ? "" : r.getDetalle(),//detalle
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
                libroBanco.addRow(libroBancoMes);
            }
        }

        model.addRow(libroBanco, totalIngresos, totalEgresos, totalSaldo);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;

    }
}

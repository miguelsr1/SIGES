/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgConciliacionesBancarias;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConciliacionBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.restclient.ConciliacionBancariaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;
import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ConciliacionBancariaGenerator {

    private static final Logger LOGGER = Logger.getLogger(ConciliacionBancariaGenerator.class.getName());

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;


    @Inject
    private ConciliacionBancariaRestClient conciliacionBancRestClient;

    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject 
    private MovimientoCuentaBancariaRestClient movCBancRestClient;

    public MasterReport generarReporte(Long conciliacionId) throws Exception {
        try {
            
            SgConciliacionesBancarias conciliacion = new SgConciliacionesBancarias();
            BusinessException be = new BusinessException();

            if (conciliacionId == null) {
                be.addError("Debes ingresar la id de la conciliación bancaria.");
                throw be;
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
                reportDefinitionURL = classloader.getResource("conciliacion_bancaria.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            //Busqueda de Conciliacion Bancaria
            FiltroConciliacionBancaria filtro = new FiltroConciliacionBancaria();
            filtro.setIncluirCampos(new String[]{
                "conPk",
                "conCuentaFk.cbcPk",
                "conCuentaFk.cbcNumeroCuenta",
                "conCuentaFk.cbcVersion",
                "conCuentaFk.cbcBancoFk.bncNombre",
                "conCuentaFk.cbcBancoFk.bncPk",
                "conCuentaFk.cbcBancoFk.bncVersion",
                "conCuentaFk.cbcSedeFk.sedPk",
                "conCuentaFk.cbcSedeFk.sedCodigo",
                "conCuentaFk.cbcSedeFk.sedNombre",
                "conCuentaFk.cbcSedeFk.sedVersion",
                "conCuentaFk.cbcSedeFk.sedTipo",
                "conCuentaFk.cbcSedeFk.sedDireccion.dirDepartamento.depNombre",
                "conCuentaFk.cbcSedeFk.sedDireccion.dirMunicipio.munNombre",
                "conFechaDesde",
                "conFechaHasta",
                "conMonto",
                "conUltModFecha",
                "conVersion"
            });
            filtro.setConPk(conciliacionId);
            
            List<SgConciliacionesBancarias> list = conciliacionBancRestClient.buscar(filtro);
            
            if(!list.isEmpty()){
                conciliacion = list.get(0);
            }

            if(conciliacion==null){
            
            }
            
            FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
            filtroMov.setIncluirCampos(new String[]{
                "mcbPk",
                "mcbFecha",
                "mcbDetalle",
                "mcbPagoFk.pgsFactura.facProveedorFk.proId",
                "mcbPagoFk.pgsFactura.facProveedorFk.proNombre",
                "mcbPagoFk.pgsFactura.facProveedorFk.proVersion",
                "mcbChequeCb",
                "mcbMonto",
                "mcbChequeCobrado",
                "mcbTipoMovimiento",
                "mcbSaldo",
                "mcbVersion"

            });
            filtroMov.setOrderBy(new String[]{"mcbFecha", "mcbPk"});
            filtroMov.setAscending(new boolean[]{true, false});
            filtroMov.setMcbFechaDesde(conciliacion.getConFechaDesde().atTime(LocalTime.MIN));
            filtroMov.setMcbFechaHasta(conciliacion.getConFechaHasta().atTime(LocalTime.MAX));
            filtroMov.setMcbCuentaFK(conciliacion.getConCuentaFk().getCbcPk());
            filtroMov.setMcbChequeCobrado(Boolean.FALSE);
            filtroMov.setMcbAplicaConciliacion(Boolean.TRUE);
            filtroMov.setMcbTipoMovimiento(TipoMovimiento.DEBE);
            
            List<SgMovimientoCuentaBancaria> listMov = movCBancRestClient.buscar(filtroMov);
            
            if(listMov==null || listMov.isEmpty()){
                throw be;
            }
            
            report.setDataFactory(this.getDataFactory(listMov));
            
            report.getParameterValues().put("nombreSede", conciliacion.getConCuentaFk().getCbcSedeFk().getSedNombre());
            if(conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion()!=null){
                report.getParameterValues().put("municipio", conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirMunicipio()!= null && StringUtils.isNotBlank(conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirMunicipio().getMunNombre()) ?  conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirMunicipio().getMunNombre().trim() : "" );
                report.getParameterValues().put("departamento", conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirDepartamento()!=null && StringUtils.isNotBlank(conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirDepartamento().getDepNombre()) ?  conciliacion.getConCuentaFk().getCbcSedeFk().getSedDireccion().getDirDepartamento().getDepNombre().trim() : "");
            }
            report.getParameterValues().put("cuentaNo", conciliacion.getConCuentaFk().getCbcNumeroCuenta());
            report.getParameterValues().put("nombreBanco", conciliacion.getConCuentaFk().getCbcBancoFk().getBncNombre());
            report.getParameterValues().put("cantidad_solicitada", NumberToLetterConverter.convertNumberToLetter(conciliacion.getConMonto(), Boolean.TRUE).trim().toUpperCase());
            report.getParameterValues().put("cantidad", conciliacion.getConMonto());
            report.getParameterValues().put("nombre1",StringUtils.EMPTY);
            report.getParameterValues().put("cargo1", StringUtils.EMPTY);
            report.getParameterValues().put("nombre2",StringUtils.EMPTY);
            report.getParameterValues().put("cargo2", StringUtils.EMPTY);

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<SgMovimientoCuentaBancaria> list) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("tabla1", TypedTableModel.class);
        
        TypedTableModel tabla1 = new TypedTableModel();
        tabla1.addColumn("fecha", String.class);
        tabla1.addColumn("numero", String.class);
        tabla1.addColumn("aFavorDe", String.class);
        tabla1.addColumn("monto", BigDecimal.class);
        
        for(SgMovimientoCuentaBancaria mov : list){
            tabla1.addRow(Generales.getDateTimeFormat(mov.getMcbFecha(), "dd/MM/yyyy"),//fecha
                         mov.getMcbChequeCb(),//numero
                         mov.getMcbPagoFk().getPgsFactura().getFacProveedorFk().getProNombre(),//aFavorDe
                         mov.getMcbMonto()//monto 
                        );
        }
        
        TableDataFactory dataFactory = new TableDataFactory();
        model.addRow(tabla1);
        dataFactory.addTable("param-query", model);
        return dataFactory;

    }
}

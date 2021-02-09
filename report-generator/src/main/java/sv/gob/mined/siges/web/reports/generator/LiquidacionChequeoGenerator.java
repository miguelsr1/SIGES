/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
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
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.dto.finanzas.SgLiquidacion;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EvaluacionItemLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class LiquidacionChequeoGenerator {

    
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
    private SedeRestClient sedeRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;
    
    private String unidad = new String();
    private String linea = new String();
    private BigDecimal montoDepositado = BigDecimal.ZERO;
    private BigDecimal saldo = BigDecimal.ZERO;

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
                reportDefinitionURL = classloader.getResource("listaDeCotejoLiquidacion.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            //Busqueda de Liquidacion
            FiltroLiquidacion filtroLiq = new FiltroLiquidacion();
            filtroLiq.setIncluirCampos(new String[]{
            "liqPk","liqComponentePk.cpeNombre","liqSubComponenteFk.gesNombre",
            "liqSedePk.sedPk","liqSedePk.sedCodigo","liqSedePk.sedNombre","liqSedePk.sedTipo",
            "liqSedePk.sedDireccion.dirDireccion","liqSedePk.sedDireccion.dirDepartamento.depNombre","liqSedePk.sedDireccion.dirCanton.canNombre",
            "liqSedePk.sedDireccion.dirMunicipio.munNombre",
            "liqSedePk.sedDireccion.dirCaserio.casNombre","liqSedePk.sedTelefono","liqSedePk.sedVersion",
            "evaluacionLiquidacion.elqComentario",
            "liqAnioPk.alePk","liqAnioPk.aleAnio","liqAnioPk.aleVersion","liqEstado","liqUltModFecha","liqVersion"});
            filtroLiq.setLiqPk(liqId);
            List<SgLiquidacion> list = liquidacionRestClient.buscar(filtroLiq);
            if(list!=null && !list.isEmpty() && list.get(0)!=null){
                liquidacion = list.get(0);
            }
            

            if (liquidacion.getLiqPk()==null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.LIQUIDACION_NO_ENCONTRADA));
                throw be;
            }
            
            FiltroEvaluacionItemLiquidacion filtroEva = new FiltroEvaluacionItemLiquidacion();
            filtroEva.setEilLiqFk(liquidacion.getLiqPk());
            filtroEva.setIncluirCampos(new String[]{
            "eilPk","eilLiqFk.liqPk","eilLiqFk.liqVersion","eilItemFk.iliPk",
            "eilItemFk.iliCodigo","eilItemFk.iliNombre","eilItemFk.iliVersion","eilMarcado","eilVersion"
            });
            
            List<SgEvaluacionItemLiquidacion> listEva = evaluacionItemLiquidacionRestClient.buscar(filtroEva);
            
            if(listEva.isEmpty()){
                be.addError("ERROR ITEMS NO ENCONTRADOS");
                throw be;
            }
            else{
                report.setDataFactory(this.getDataFactory(listEva));
            }
            
            FiltroMovimientoLiquidacion filtroMovLiq1 = new FiltroMovimientoLiquidacion();
            filtroMovLiq1.setMlqLiquidacionPk(liquidacion.getLiqPk());
            filtroMovLiq1.setMovimientoTipo(EnumMovimientosTipo.I);
            filtroMovLiq1.setIncluirCampos(new String[]{"mlqPk",
            "mlqMovimientoPk.mcbDesembolsoCedFk.dceDetDesembolsoFk.dedFuenteRecursosFk.fuenteFinanciamiento.id",
            "mlqMovimientoPk.mcbDesembolsoCedFk.dceDetDesembolsoFk.dedFuenteRecursosFk.fuenteFinanciamiento.nombre",
            "mlqTipoMovimiento","mlqVersion",
            });
            
            String fuenteFinanciamiento = StringUtils.EMPTY;
            
            List<SgMovimientoLiquidacion> listMovsI = movimientoLiqRestClient.buscar(filtroMovLiq1);
            if(!listMovsI.isEmpty()){
                fuenteFinanciamiento=listMovsI.stream().filter(l -> l.getMlqMovimientoPk().getMcbDesembolsoCedFk().getDceDetDesembolsoFk().getDedFuenteRecursosFk().getFuenteFinanciamiento()!=null)
                                              .map(l->l.getMlqMovimientoPk().getMcbDesembolsoCedFk().getDceDetDesembolsoFk().getDedFuenteRecursosFk().getFuenteFinanciamiento().getNombre())
                                              .collect(Collectors.joining(","));
            }
            else{
                throw be;
            }
            
            SgCentroEducativo ced = sedeRestClient.obtenerCentroEducativoPorId(liquidacion.getLiqSedePk().getSedPk());
            
            report.getParameterValues().put("nombreTransferencia", liquidacion.getLiqComponentePk().getCpeNombre().concat(" - ").concat(liquidacion.getLiqSubComponenteFk().getGesNombre()));
            report.getParameterValues().put("nombreSede", liquidacion.getLiqSedePk().getSedNombre());
            report.getParameterValues().put("fuenteFinanciamiento", fuenteFinanciamiento);
            report.getParameterValues().put("codigo", liquidacion.getLiqSedePk().getSedCodigo());
            report.getParameterValues().put("municipio", liquidacion.getLiqSedePk().getSedDireccion().getDirMunicipio().getMunNombre());
            report.getParameterValues().put("departamento", liquidacion.getLiqSedePk().getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("canton", (liquidacion.getLiqSedePk().getSedDireccion().getDirCanton()!=null) ? liquidacion.getLiqSedePk().getSedDireccion().getDirCanton().getCanNombre() : "" );
            report.getParameterValues().put("caserio", (liquidacion.getLiqSedePk().getSedDireccion().getDirCaserio()!=null) ? liquidacion.getLiqSedePk().getSedDireccion().getDirCaserio().getCasNombre() : "");
            report.getParameterValues().put("anio", liquidacion.getLiqAnioPk().getAleAnio().toString());
            report.getParameterValues().put("fecha", Generales.getDateTimeFormat(liquidacion.getLiqUltModFecha(),"dd/MM/yyyy"));
            report.getParameterValues().put("1",ced!=null && ced.getCedTipoOrganismoAdministrativo()!=null ? ced.getCedTipoOrganismoAdministrativo().getToaCodigo(): "");
            report.getParameterValues().put("observaciones",liquidacion.getEvaluacionLiquidacion() != null && StringUtils.isNotBlank(liquidacion.getEvaluacionLiquidacion().getElqComentario()) ?  liquidacion.getEvaluacionLiquidacion().getElqComentario().trim() : "");
            

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }
    
    
    private DataFactory getDataFactory(List<SgEvaluacionItemLiquidacion> list) throws Exception {
        
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("inconsistencias", TypedTableModel.class);
        
        TypedTableModel inconsistencias = new TypedTableModel();
        inconsistencias.addColumn("n", String.class);
        inconsistencias.addColumn("inconsistencia", String.class);
        inconsistencias.addColumn("Inf", String.class);
        
        list.forEach(e-> {
            inconsistencias.addRow(e.getEilItemFk().getIliCodigo(),e.getEilItemFk().getIliNombre(),e.getEilMarcado().equals(Boolean.TRUE)? "SI" : "");
        });
        
        
        model.addRow(inconsistencias);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    
    }
    
    
    
    
}

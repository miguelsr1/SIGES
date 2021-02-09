/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.finanzas.SgLiquidacion;
import sv.gob.mined.siges.web.dto.finanzas.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
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
public class LiquidacionGenerator {

    
    @Inject
    private LiquidacionRestClient liquidacionRestClient;
    
    @Inject 
    private MovimientoLiquidacionRestClient movimientoLiqRestClient;
    
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
                reportDefinitionURL = classloader.getResource("autoLiquidacion.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            //Busqueda de Liquidacion
            FiltroLiquidacion filtroLiq = new FiltroLiquidacion();
            filtroLiq.setIncluirCampos(new String[]{
            "liqPk","liqComponentePk.cpeNombre","liqSubComponenteFk.gesNombre",
            "liqSubComponenteFk.gesConvenio","liqSubComponenteFk.gesProyecto","liqSubComponenteFk.gesCategoria",
            "liqSubComponenteFk.gesSubactividad","liqSubComponenteFk.categoriaGastoConvenio",
            "liqSedePk.sedPk","liqSedePk.sedCodigo","liqSedePk.sedNombre","liqSedePk.sedTipo",
            "liqSedePk.sedDireccion.dirDireccion","liqSedePk.sedDireccion.dirDepartamento.depNombre","liqSedePk.sedDireccion.dirCanton.canNombre",
            "liqSedePk.sedDireccion.dirCaserio.casNombre","liqSedePk.sedTelefono","liqSedePk.sedVersion",
            "liqAnioPk.alePk","liqAnioPk.aleAnio","liqAnioPk.aleVersion","liqEstado","liqVersion"});
            filtroLiq.setLiqPk(liqId);
            List<SgLiquidacion> list = liquidacionRestClient.buscar(filtroLiq);
            if(list!=null && !list.isEmpty() && list.get(0)!=null){
                liquidacion = list.get(0);
            }
            

            if (liquidacion.getLiqPk()==null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.LIQUIDACION_NO_ENCONTRADA));
                throw be;
            }
            
            
            FiltroOrganismoAdministrativoEscolar filtroOae = new FiltroOrganismoAdministrativoEscolar();
            filtroOae.setOaeEstado(EnumEstadoOrganismoAdministrativo.APROBADO);
            filtroOae.setOaeSedeFk(liquidacion.getLiqSedePk().getSedPk());

            filtroOae.setIncluirCampos(new String[]{
                "oaeNombre",
                "oaeTipoOrganismoAdministrativo.toaNombre"});
            
            List<SgOrganismoAdministracionEscolar> listOae = oaeRestClient.buscar(filtroOae);
            if(!listOae.isEmpty()){
                oae = listOae.get(0);
            }
            else{
                be.addError(Mensajes.obtenerMensaje(Mensajes.OAE_NO_ENCONTRADA));
                throw be;
            }
            
             //ORDEN 1-PRESIDENTE
            String nombrePresidente = StringUtils.EMPTY;

            //ORDEN 2-TESORERO
            String nombreTesorero = StringUtils.EMPTY;

            //ORDEN 3-CONSEJAL
            String nombreConsejal = StringUtils.EMPTY;

            List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar = new ArrayList();
            
            List<String> codigos = new ArrayList();
            codigos.add(Constantes.CODIGO_CDE);

            FiltroPersonaOrganismoAdministrativo fPOA = new FiltroPersonaOrganismoAdministrativo();
            fPOA.setSedeId(liquidacion.getLiqSedePk().getSedPk());
            fPOA.setCodigosTOA(codigos);
            fPOA.setPoaHabilitado(Boolean.TRUE);
            fPOA.setAscending(new boolean[]{true});
            fPOA.setOrderBy(new String[]{"poaCargo.coaOrden"});
            fPOA.setIncluirCampos(new String[]{"poaPersona.perPrimerNombre","poaPersona.perSegundoNombre","poaPersona.perTercerNombre","poaPersona.perPrimerApellido",
                                "poaPersona.perSegundoApellido","poaPersona.perTercerApellido","poaCargo.coaCodigo","poaCargo.coaNombre",
                                "poaPrerregistro","poaNombre","poaOcupacion","poaDomicilio"});


            listaOrganismoEscolar =  personaOrganismoAdminRestClient.buscar(fPOA); 
            
            Integer count = 0;
            Boolean preregistrado = Boolean.FALSE;
            for(SgPersonaOrganismoAdministracion poa : listaOrganismoEscolar) {
                    if(poa.getPoaCargo() != null && poa.getPoaCargo().getCoaCodigo() != null) {
                       preregistrado = poa.getPoaPrerregistro() != null ? poa.getPoaPrerregistro() : false;
                       if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_PRESIDENTE_CDE)) {
                            if(preregistrado) {
                                nombrePresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            } else {
                                nombrePresidente = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            }
                            count ++;
                       } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_TESORERO_CDE)) {
                           if(preregistrado) {
                                nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            } else {
                                nombreTesorero = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            }
                            count ++;
                       } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_CONSEJAL_CDE)) {
                            if(preregistrado) {
                                nombreConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            } else {
                                nombreConsejal = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            }
                            count ++;
                       }
                    }
                    if(count > 2) break;
            }
            
            
            FiltroMovimientoLiquidacion filtroMovLiq1 = new FiltroMovimientoLiquidacion();
            filtroMovLiq1.setMlqLiquidacionPk(liquidacion.getLiqPk());
            filtroMovLiq1.setMovimientoTipo(EnumMovimientosTipo.I);
            filtroMovLiq1.setIncluirCampos(new String[]{"mlqPk",
            "mlqMovimientoPk.mcbCuentaFK.cbcPk","mlqMovimientoPk.mcbCuentaFK.cbcNumeroCuenta",
            "mlqMovimientoPk.mcbCuentaFK.cbcTitular","mlqMovimientoPk.mcbCuentaFK.cbcBancoFk.bncNombre",
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
                
                List<SgCuentasBancarias> lisCuentas =listMovsI.stream().filter(l->l.getMlqMovimientoPk().getMcbCuentaFK()!=null).map(l->l.getMlqMovimientoPk().getMcbCuentaFK()).collect(Collectors.toList());
                
                if(!lisCuentas.isEmpty()){
                    cuentaBanc=lisCuentas.get(0);
                }
                else{
                    throw be;
                }
                
            }
            else{
                throw be;
            }
            
            FiltroMovimientoLiquidacion filtroMovLiq = new FiltroMovimientoLiquidacion();
            filtroMovLiq.setMlqLiquidacionPk(liquidacion.getLiqPk());
            filtroMovLiq.setIncluirCampos(new String[]{"mlqPk","mlqMovimientoPk.mcbPk","mlqMovimientoPk.mcbFecha",
            "mlqMovimientoPk.mcbDetalle","mlqMovimientoPk.mcbMonto","mlqMovimientoPk.mcbVersion","mlqLiquidacionPk.liqPk","mlqLiquidacionPk.liqVersion",
            "mlqTipoMovimiento","mlqVersion"
            });
            
            
            report.getParameterValues().put("anio", liquidacion.getLiqAnioPk().getAleAnio().toString());
            report.getParameterValues().put("componente",liquidacion.getLiqComponentePk().getCpeNombre());
            report.getParameterValues().put("subcomponente", liquidacion.getLiqSubComponenteFk().getGesNombre());
            report.getParameterValues().put("fuenteFinanciamiento", fuenteFinanciamiento);
            report.getParameterValues().put("tipoOrganismo", oae.getOaeTipoOrganismoAdministrativo().getToaNombre());
            report.getParameterValues().put("codigoInfraestructura", liquidacion.getLiqSedePk().getSedCodigo());
            report.getParameterValues().put("nombreCentroEducativo", liquidacion.getLiqSedePk().getSedCodigoNombre());
            report.getParameterValues().put("nombreOAE", oae.getOaeNombre());
            
            report.getParameterValues().put("direccion", liquidacion.getLiqSedePk().getSedDireccion().getDirDireccion());
            report.getParameterValues().put("departamento", liquidacion.getLiqSedePk().getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("canton", (liquidacion.getLiqSedePk().getSedDireccion().getDirCanton()!=null) ? liquidacion.getLiqSedePk().getSedDireccion().getDirCanton().getCanNombre() : "");
            report.getParameterValues().put("caserio", (liquidacion.getLiqSedePk().getSedDireccion().getDirCaserio()!=null) ? liquidacion.getLiqSedePk().getSedDireccion().getDirCaserio().getCasNombre() : "");
            report.getParameterValues().put("telefono", liquidacion.getLiqSedePk().getSedTelefono());
            report.getParameterValues().put("nit", "");
            report.getParameterValues().put("isss", "");
            
            report.getParameterValues().put("nombreBanco", cuentaBanc.getCbcBancoFk().getBncNombre());
            report.getParameterValues().put("NoCuenta", cuentaBanc.getCbcNumeroCuenta());
            report.getParameterValues().put("nombreCuenta", cuentaBanc.getCbcTitular());
            report.getParameterValues().put("nombrePresidente", nombrePresidente);
            report.getParameterValues().put("nombreTesorero", nombreTesorero);
            report.getParameterValues().put("nombreConsejal", nombreConsejal);
            report.getParameterValues().put("observaciones", "");
            report.getParameterValues().put("componenteLiquidado", liquidacion.getLiqComponentePk().getCpeNombre());
            report.getParameterValues().put("fechaLiquidacion", Generales.getDateTimeFormat(liquidacion.getLiqUltModFecha(), "dd/MM/yyyy"));
            
            if(StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesConvenio()) || StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesProyecto()) ||  
               StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesCategoria()) || StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesSubactividad()) ||
               liquidacion.getLiqSubComponenteFk().getCategoriaGastoConvenio()!=null){
                report.getParameterValues().put("datos_convenio", Constantes.SI);
                report.getParameterValues().put("convenio", StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesConvenio()) ? liquidacion.getLiqSubComponenteFk().getGesConvenio().trim() : "");
                report.getParameterValues().put("proyecto", StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesProyecto()) ? liquidacion.getLiqSubComponenteFk().getGesProyecto().trim() : "");
                report.getParameterValues().put("componente_convenio", StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesCategoria()) ? liquidacion.getLiqSubComponenteFk().getGesCategoria().trim() : "");
                report.getParameterValues().put("subcomponente_convenio", StringUtils.isNotBlank(liquidacion.getLiqSubComponenteFk().getGesSubactividad()) ?  liquidacion.getLiqSubComponenteFk().getGesSubactividad().trim() : "");
                report.getParameterValues().put("cat_gasto_convenio", liquidacion.getLiqSubComponenteFk().getCategoriaGastoConvenio()!=null ?  liquidacion.getLiqSubComponenteFk().getCategoriaGastoConvenio().toString().trim() : "");
                
            }
            
            
            List<SgMovimientoLiquidacion> listMovs = movimientoLiqRestClient.buscar(filtroMovLiq);
            if(!listMovs.isEmpty()){
                report.setDataFactory(this.getDataFactory(listMovs));
            }
            else{
                throw be;
            }
            
            report.getParameterValues().put("montoTransferido", montoDepositado);
            report.getParameterValues().put("saldo", saldo);
            
            

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }
    
    
    private DataFactory getDataFactory(List<SgMovimientoLiquidacion> list) throws Exception {
        
        BigDecimal depositado = BigDecimal.ZERO;
        BigDecimal invertido = BigDecimal.ZERO;
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("tablaMontoTransferido1", TypedTableModel.class);
        model.addColumn("tablaMontoTransferido2", TypedTableModel.class);
        
        TypedTableModel tablaMontoTransferido1 = new TypedTableModel();
        tablaMontoTransferido1.addColumn("conceptoDepositado", String.class);
        tablaMontoTransferido1.addColumn("cantidadDepositado", BigDecimal.class);
        
        TypedTableModel tablaMontoTransferido2 = new TypedTableModel();
        tablaMontoTransferido2.addColumn("conceptoInvertido", String.class);
        tablaMontoTransferido2.addColumn("cantidadInvertido", BigDecimal.class);
        
        
        list.stream().filter(l->l.getMlqMovimientoPk()!=null).filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).forEach(l->{
            tablaMontoTransferido1.addRow(l.getMlqMovimientoPk().getMcbDetalle(),l.getMlqMovimientoPk().getMcbMonto());
        });
        
        list.stream().filter(l->l.getMlqMovimientoPk()!=null).filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E)).forEach(l->{
            tablaMontoTransferido2.addRow(l.getMlqMovimientoPk().getMcbDetalle(),l.getMlqMovimientoPk().getMcbMonto());
        });
        
        depositado = list.stream().filter(l->l.getMlqMovimientoPk()!=null).filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I))
                                       .map(l->l.getMlqMovimientoPk().getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        invertido = list.stream().filter(l->l.getMlqMovimientoPk()!=null).filter(l->l.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E))
                                       .map(l->l.getMlqMovimientoPk().getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        montoDepositado = depositado;
        
        if(depositado!=null && invertido!=null){
            saldo = depositado.subtract(invertido);
        }
        else if(depositado!=null && invertido==null){
            saldo = depositado;
        }
        else if(depositado==null && invertido!=null){
            saldo = invertido;
        }
                
        
        model.addRow(tablaMontoTransferido1,tablaMontoTransferido2);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    
    }
    
    
    
    
}

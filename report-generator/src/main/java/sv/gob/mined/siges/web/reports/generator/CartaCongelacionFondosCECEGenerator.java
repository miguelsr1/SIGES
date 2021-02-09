/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
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
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
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
public class CartaCongelacionFondosCECEGenerator {
    private static final Logger LOGGER = Logger.getLogger(CartaCongelacionFondosCECEGenerator.class.getName());
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoAdministracionRestClient;
    
    @Inject
    private CuentasBancariasRestClient cuentasBancariasRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath; 
    
    @Inject
    private SessionBean sessionBean;
      
    public MasterReport generarReporte(Long sedeId) throws Exception {
       try {
            BusinessException be = new BusinessException();
            if (sedeId == null) {
                be.addError("Debes ingresar la sede.");
                throw be;
            }
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CARTA_CONGELA_FONDOS_CECE);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CARTA_CONGELA_FONDOS_CECE + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("CARTA_CONGELA_FONDOS_CECE.prpt");
            }
                    
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
           
            SgSede sede = sedeRestClient.obtenerPorId(sedeId);
            if (sede == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                throw be;
            }
            
            FiltroCuentasBancarias fcuentas = new FiltroCuentasBancarias();
            fcuentas.setIncluirCampos(new String[]{"cbcNumeroCuenta","cbcBancoFk.bncNombre","cbcTitular"});
            fcuentas.setCbcSedeFk(sedeId);
            fcuentas.setCbcHabilitado(Boolean.TRUE);
            List<SgCuentasBancarias> cuentas = cuentasBancariasRestClient.buscar(fcuentas);
            
            if (cuentas == null) {
                be.addError("No se han encontrado cuentas asociadas a la sede");
                throw be;
            }
            
            
            List<String> codigos = new ArrayList();
            codigos.add(Constantes.CODIGO_CECE);
            
            FiltroPersonaOrganismoAdministrativo fPOA = new FiltroPersonaOrganismoAdministrativo();
            fPOA.setSedeId(sede.getSedPk());
            fPOA.setCodigosTOA(codigos);
            fPOA.setAscending(new boolean[]{true});
            fPOA.setPoaHabilitado(Boolean.TRUE);
            fPOA.setOrderBy(new String[]{"poaCargo.coaOrden"});
            fPOA.setIncluirCampos(new String[]{"poaPersona.perPrimerNombre","poaPersona.perSegundoNombre","poaPersona.perTercerNombre","poaPersona.perPrimerApellido",
                                "poaPersona.perSegundoApellido","poaPersona.perTercerApellido","poaPersona.perDui","poaCargo.coaCodigo","poaCargo.coaNombre",
                                "poaPrerregistro","poaNombre","poaDui","poaOcupacion","poaDomicilio"});
            
            
            List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar =  personaOrganismoAdministracionRestClient.buscar(fPOA);          
            
            report.setDataFactory(this.getDataFactory(cuentas, sede, listaOrganismoEscolar));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<SgCuentasBancarias> cuentas, SgSede sede,List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar) throws Exception {
        String departamentoUnidad = StringUtils.EMPTY;
        String fecha = StringUtils.EMPTY;
        
        //ORDEN 1-PRESIDENTE
        String nombrePresidente = StringUtils.EMPTY;
        String documentoPresidente = StringUtils.EMPTY;
        
        //ORDEN 2-TESORERO
        String nombreTesorero = StringUtils.EMPTY;
        String documentoTesorero = StringUtils.EMPTY;
        
        //ORDEN 3-CONSEJAL
        String nombreConsejal = StringUtils.EMPTY;
        String documentoConsejal = StringUtils.EMPTY;
        
        fecha = LocalDate.now().getDayOfMonth() + " de " + Generales.getMesDescripcion(LocalDate.now().getMonthValue()) + " de " + LocalDate.now().getYear();

        if(sede != null) { 
            departamentoUnidad = sede.getSedDireccion() != null && sede.getSedDireccion().getDirDepartamento() != null ? sede.getSedDireccion().getDirDepartamento().getDepNombre().toUpperCase().trim() : "" ;
        }
        
        
        Integer count = 0;
        Boolean preregistrado = Boolean.FALSE;
        for(SgPersonaOrganismoAdministracion poa : listaOrganismoEscolar) {
                if(poa.getPoaCargo() != null && poa.getPoaCargo().getCoaCodigo() != null) {
                   preregistrado = poa.getPoaPrerregistro() != null ? poa.getPoaPrerregistro() : false;
                   if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_PRESIDENTE_CECE)) {
                        if(preregistrado) {
                            nombrePresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            documentoPresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                        } else {
                            nombrePresidente = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            documentoPresidente = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                        }
                        count ++;
                   } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_TESORERO_CECE)) {
                       if(preregistrado) {
                            nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            documentoTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                        } else {
                            nombreTesorero = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            documentoTesorero = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                        }
                        count ++;
                   } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_CONSEJAL_CECE)) {
                        if(preregistrado) {
                            nombreConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            documentoConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                        } else {
                            nombreConsejal = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            documentoConsejal = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                        }
                        count ++;
                   }
                }
                if(count > 2) break;
        }
        
        TypedTableModel model = new TypedTableModel();
 
        model.addColumn("lugar", String.class);
        model.addColumn("fecha", String.class);
        model.addColumn("nombre_banco", String.class);
        model.addColumn("numero_cuenta", String.class);
        model.addColumn("nombre_cta", String.class);
        
        //DATOS DE CDE
        model.addColumn("presidente", String.class);
        model.addColumn("Dui_presidente", String.class);
        
        model.addColumn("tesorero", String.class);
        model.addColumn("Dui_tesorero", String.class);
         
        model.addColumn("consejal", String.class);
        model.addColumn("Dui_consejal", String.class);
        
        String banco = StringUtils.EMPTY;
        for (SgCuentasBancarias cuenta : cuentas) {
            banco = StringUtils.EMPTY;
            if(cuenta.getCbcBancoFk() != null && cuenta.getCbcBancoFk().getBncNombre() != null && StringUtils.isNotBlank(cuenta.getCbcBancoFk().getBncNombre())) {
                banco = cuenta.getCbcBancoFk().getBncNombre().toUpperCase().trim().replace("[\\n\\t]", "");
            }
            
            model.addRow(
                    departamentoUnidad,//lugar
                    fecha,//fecha
                    banco,//nombre_banco
                    cuenta.getCbcNumeroCuenta() != null && StringUtils.isNotBlank(cuenta.getCbcNumeroCuenta()) ? cuenta.getCbcNumeroCuenta().trim() : "",//numero_cuenta
                    cuenta.getCbcTitular() != null  && StringUtils.isNotBlank(cuenta.getCbcTitular())? cuenta.getCbcTitular().trim().toUpperCase():"",//nombre_cta
                    //PRESIDENTE
                    nombrePresidente,//nombre_presidente
                    documentoPresidente,//Dui_presidente
                    //TESORERO
                    nombreTesorero,//nombre_tesorero
                    documentoTesorero,//Dui_tesorero
                    //CONSEJAL
                    nombreConsejal,//nombre_consejal
                    documentoConsejal//Dui_consejal
                  );
        }
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
}
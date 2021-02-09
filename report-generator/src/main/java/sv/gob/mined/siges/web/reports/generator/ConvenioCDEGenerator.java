/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
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
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.finanzas.SgDireccionDepartamental;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDireccionDepartamental;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioEducativo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ConvenioCDEGenerator {

    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private DireccionDepartamentalRestClient direccionDepartamentalRestClient;
    
    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoAdministracionRestClient;
    
    @Inject
    private ServicioEducativoRestClient servicioEducativoRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
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
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CONVENIO_CDE);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CONVENIO_CDE + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("CONVCDE.prpt");
            }
                    
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            SgDireccionDepartamental departamental = null;
            SgSede sede = sedeRestClient.obtenerPorId(sedeId);
            if (sede == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                throw be;
            }
            
            List<String> codigos = new ArrayList();
            codigos.add(Constantes.CODIGO_CDE);
            
            FiltroPersonaOrganismoAdministrativo fPOA = new FiltroPersonaOrganismoAdministrativo();
            fPOA.setSedeId(sedeId);
            fPOA.setCodigosTOA(codigos);
            fPOA.setPoaHabilitado(Boolean.TRUE);
            fPOA.setAscending(new boolean[]{true});
            fPOA.setOrderBy(new String[]{"poaCargo.coaOrden"});
            fPOA.setIncluirCampos(new String[]{"poaPersona.perPrimerNombre","poaPersona.perSegundoNombre","poaPersona.perTercerNombre","poaPersona.perPrimerApellido","poaPersona.perDui",
                                "poaPersona.perSegundoApellido","poaPersona.perTercerApellido","poaPersona.perOcupacion.ocuNombre","poaPersona.perDireccion.dirDireccion",
                                "poaPersona.perProfesion.proNombre", "poaCargo.coaCodigo","poaCargo.coaNombre","poaPrerregistro","poaNombre","poaDui","poaOcupacion","poaDomicilio",
                                "poaDuiExpedidoEn","poaFechaExpedicionDUI"});
            
            
            List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar =  personaOrganismoAdministracionRestClient.buscar(fPOA);          
            
            if(sede.getSedDireccion() != null && sede.getSedDireccion().getDirDepartamento() != null && sede.getSedDireccion().getDirDepartamento().getDepPk() != null) {
                FiltroDireccionDepartamental fdireccion = new FiltroDireccionDepartamental();
                fdireccion.setDepartamentoPk(sede.getSedDireccion().getDirDepartamento().getDepPk());
                fdireccion.setMaxResults(1L);
                fdireccion.setDedHabilitado(Boolean.TRUE);
                fdireccion.setAscending(new boolean[]{false});
                fdireccion.setOrderBy(new String[]{"dedPk"});
                fdireccion.setIncluirCampos(new String[]{"decDirectorNombre","decDirectorCargo","decDirectorDomicilio","dedDirectorProfesionFk.proNombre"});          
                List<SgDireccionDepartamental>  departamentales = direccionDepartamentalRestClient.buscar(fdireccion);
                if(departamentales != null && !departamentales.isEmpty()) {
                    departamental = departamentales.get(0);
                }
            }

            report.setDataFactory(this.getDataFactory(sede, departamental, listaOrganismoEscolar));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgSede sede,SgDireccionDepartamental departamental, List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar) throws Exception {
        String nombreUnidad = StringUtils.EMPTY;
        String codigoUnidad = StringUtils.EMPTY;
        String direccionUnidad = StringUtils.EMPTY;
        String departamentoUnidad = StringUtils.EMPTY;
        String municipioUnidad = StringUtils.EMPTY;
        //ORDEN 1-PRESIDENTE
        String nombrePresidente = StringUtils.EMPTY;
        String ocupacionPresidente = StringUtils.EMPTY;
        String domicilioPresidente = StringUtils.EMPTY;
        String documentoPresidente = StringUtils.EMPTY;
        String extendidaPresidente = StringUtils.EMPTY;
        String fechaEntensionPresidente = StringUtils.EMPTY;
        String cargoPresidente = StringUtils.EMPTY;
        
        //ORDEN 2-TESORERO
        String nombreTesorero = StringUtils.EMPTY;
        String ocupacionTesorero = StringUtils.EMPTY;
        String domicilioTesorero = StringUtils.EMPTY;
        String documentoTesorero = StringUtils.EMPTY;
        String extendidaTesorero = StringUtils.EMPTY;
        String fechaExtensionTesorero = StringUtils.EMPTY;
        String cargoTesorero = StringUtils.EMPTY;
        
        //ORDEN 3-CONSEJAL
        String nombreConsejal = StringUtils.EMPTY;
        String ocupacionConsejal = StringUtils.EMPTY;
        String domicilioConsejal = StringUtils.EMPTY;
        String documentoConsejal = StringUtils.EMPTY;
        String extendidaConsejal = StringUtils.EMPTY;
        String fechaExtensionConsejal = StringUtils.EMPTY;
        String cargoConsejal = StringUtils.EMPTY;
        
        String niveles = StringUtils.EMPTY;
   
        String fechaGeneracion = LocalDate.now().getDayOfMonth() + " días del mes de " + Generales.getMesDescripcion(LocalDate.now().getMonthValue()) + " de " + LocalDate.now().getYear();
        
        if(sede != null) {
            nombreUnidad  = sede.getSedNombre() != null ? sede.getSedNombre().trim().toUpperCase() : "";
            codigoUnidad = sede.getSedCodigo() !=null ? sede.getSedCodigo().trim().toUpperCase() : "";
            direccionUnidad = sede.getSedDireccion() != null && sede.getSedDireccion().getDirDireccion() != null ? sede.getSedDireccion().getDirDireccion().toUpperCase() : "" ;
            departamentoUnidad = sede.getSedDireccion() != null && sede.getSedDireccion().getDirDepartamento() != null ? sede.getSedDireccion().getDirDepartamento().getDepNombre().toUpperCase().trim() : "" ;
            municipioUnidad = sede.getSedDireccion() != null && sede.getSedDireccion().getDirMunicipio() != null ? sede.getSedDireccion().getDirMunicipio().getMunNombre().trim().toUpperCase() : "" ;
        }
   
        Integer count = 0;
        Boolean preregistrado = Boolean.FALSE;
        for(SgPersonaOrganismoAdministracion poa : listaOrganismoEscolar) {
                if(poa.getPoaCargo() != null && poa.getPoaCargo().getCoaCodigo() != null) {
                   preregistrado = poa.getPoaPrerregistro() != null ? poa.getPoaPrerregistro() : false;
                   if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_PRESIDENTE_CDE)) {
                        if(preregistrado) {
                            nombrePresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            ocupacionPresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerOcupacion() != null ?  poa.getPoaPersona().getPerOcupacion().getOcuNombre().trim() : "";
                            domicilioPresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDireccion() != null ? poa.getPoaPersona().getPerDireccion().getDirDireccion().trim().toUpperCase() : "" ;
                            documentoPresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                            extendidaPresidente = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaEntensionPresidente = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        } else {
                            nombrePresidente = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            ocupacionPresidente = poa.getPoaOcupacion() != null && StringUtils.isNotBlank(poa.getPoaOcupacion())?  poa.getPoaOcupacion().toUpperCase().trim() : "";
                            domicilioPresidente = poa.getPoaDomicilio() != null && StringUtils.isNotBlank(poa.getPoaDomicilio()) ? poa.getPoaDomicilio().trim().toUpperCase() : "" ;
                            documentoPresidente = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                            extendidaPresidente = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaEntensionPresidente = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        }
                        
                        cargoPresidente = poa.getPoaCargo() != null ?  poa.getPoaCargo().getCoaNombre().trim() : "";
                        count ++;
                   } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_TESORERO_CDE)) {
                       if(preregistrado) {
                            nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            ocupacionTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerOcupacion() != null ?  poa.getPoaPersona().getPerOcupacion().getOcuNombre().trim() : "";
                            domicilioTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDireccion() != null ? poa.getPoaPersona().getPerDireccion().getDirDireccion().trim().toUpperCase() : "" ;
                            documentoTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                            extendidaTesorero = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaExtensionTesorero = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        } else {
                            nombreTesorero = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            ocupacionTesorero = poa.getPoaOcupacion() != null && StringUtils.isNotBlank(poa.getPoaOcupacion())?  poa.getPoaOcupacion().toUpperCase().trim() : "";
                            domicilioTesorero = poa.getPoaDomicilio() != null && StringUtils.isNotBlank(poa.getPoaDomicilio()) ? poa.getPoaDomicilio().trim().toUpperCase() : "" ;
                            documentoTesorero = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                            extendidaTesorero = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaExtensionTesorero = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        }
                      
                        cargoTesorero = poa.getPoaCargo() != null ?  poa.getPoaCargo().getCoaNombre().trim() : "";
                        count ++;
                   } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(Constantes.CODIGO_CONSEJAL_CDE)) {
                        if(preregistrado) {
                            nombreConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                            ocupacionConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerOcupacion() != null ?  poa.getPoaPersona().getPerOcupacion().getOcuNombre().trim() : "";
                            domicilioConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDireccion() != null ? poa.getPoaPersona().getPerDireccion().getDirDireccion().trim().toUpperCase() : "" ;
                            documentoConsejal = poa.getPoaPersona() != null && poa.getPoaPersona().getPerDui()!= null ? poa.getPoaPersona().getPerDui().toString() : "" ;
                            extendidaConsejal = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaExtensionConsejal = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        } else {
                            nombreConsejal = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                            ocupacionConsejal = poa.getPoaOcupacion() != null && StringUtils.isNotBlank(poa.getPoaOcupacion())?  poa.getPoaOcupacion().toUpperCase().trim() : "";
                            domicilioConsejal = poa.getPoaDomicilio() != null && StringUtils.isNotBlank(poa.getPoaDomicilio()) ? poa.getPoaDomicilio().trim().toUpperCase() : "" ;
                            documentoConsejal = poa.getPoaDui() != null && StringUtils.isNotBlank(poa.getPoaDui())  ? poa.getPoaDui().toString() : "" ;
                            extendidaConsejal = poa.getPoaDuiExpedidoEn() != null && StringUtils.isNotBlank(poa.getPoaDuiExpedidoEn())?  poa.getPoaDuiExpedidoEn().toUpperCase().trim() : "";
                            fechaExtensionConsejal = poa.getPoaFechaExpedicionDUI() != null ? Generales.getDateFormat(poa.getPoaFechaExpedicionDUI(),"dd/MM/yyyy") : "";
                        }
                        
                        cargoConsejal = poa.getPoaCargo() != null ?  poa.getPoaCargo().getCoaNombre().trim() : "";
                        count ++;
                   }
                }
                if(count > 2) break;
                
        }
        
        
        FiltroServicioEducativo filtroServicio = new FiltroServicioEducativo();
        filtroServicio.setIncluirCampos(new String[]{
        "sduPk","sduVersion","sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
        "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivCodigo",
        "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
        "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivHabilitado",
        "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivVersion",
        "sduEstado"});
        filtroServicio.setSecSedeFk(sede.getSedPk());
        filtroServicio.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
        
        List<SgServicioEducativo> listServicio = servicioEducativoRestClient.buscar(filtroServicio);
        
        if(!listServicio.isEmpty()){
            List<SgNivel> listNivel = listServicio.stream().filter(s->s.getSduGrado()!=null).filter(s->s.getSduGrado().getGraRelacionModalidad()!=null)
                                                          .filter(s->s.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa()!=null)
                                                          .filter(s->s.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo()!=null)
                                                          .map(s->s.getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel())
                                                          .distinct()
                                                          .collect(Collectors.toList());
            if(!listNivel.isEmpty()){
                niveles = listNivel.stream().filter(n->n.getNivNombre()!=null).map(n->n.getNivNombre()).collect(Collectors.joining(","));
            }
        
        }
        
        TypedTableModel model = new TypedTableModel();
        //DATOS DE SEDE
        model.addColumn("nombre_entidad_educativa", String.class);
        model.addColumn("codigo_entidad_educativa", String.class);
        model.addColumn("direccion_entidad", String.class);
        model.addColumn("municipio_entidad", String.class);
        model.addColumn("departamento_entidad", String.class);
        
        //DATOS DEL DIRECTOR
        model.addColumn("codigo_director", String.class);
        model.addColumn("profesion", String.class);
        model.addColumn("domicilio", String.class);
        model.addColumn("cargo_director", String.class);
        
        //DATOS DE CDE
        model.addColumn("nombre_presidente", String.class);
        model.addColumn("ocupacion_presidente", String.class);
        model.addColumn("domicilio_presidente", String.class);
        model.addColumn("cip_presidente", String.class);
        model.addColumn("extendida_presidente", String.class);
        model.addColumn("fecha_extension_presidente", String.class);
        model.addColumn("cargo_presidente", String.class);
        
        model.addColumn("nombre_tesorero", String.class);
        model.addColumn("ocupacion_tesorero", String.class);
        model.addColumn("domicilio_tesorero", String.class);
        model.addColumn("cip_tesorero", String.class);
        model.addColumn("extendida_tesorero", String.class);
        model.addColumn("fecha_extension_tesorero", String.class);
        model.addColumn("cargo_tesorero", String.class);
        
        model.addColumn("nombre_consejal", String.class);
        model.addColumn("ocupacion_consejal", String.class);
        model.addColumn("domicilio_consejal", String.class);
        model.addColumn("cip_consejal", String.class);
        model.addColumn("extendida_consejal", String.class);
        model.addColumn("fecha_extension_consejal", String.class);
        model.addColumn("cargo_consejal", String.class);
        
        model.addColumn("name_4", String.class);
        model.addColumn("convenio1", String.class);
        model.addColumn("name_2", String.class);
        model.addColumn("name_5", String.class);
        
        model.addRow(
                    nombreUnidad,//nombre_entidad_educativa
                    codigoUnidad,//codigo_entidad_educativa
                    direccionUnidad,//direccion_entidad
                    municipioUnidad,//municipio_entidad
                    departamentoUnidad,//departamento_entidad
                    departamental != null && departamental.getDecDirectorNombre() != null && StringUtils.isNotBlank(departamental.getDecDirectorNombre()) ? departamental.getDecDirectorNombre().trim().toUpperCase() : "",//codigo_director
                    departamental != null && departamental.getDedDirectorProfesionFk() != null && StringUtils.isNotBlank(departamental.getDedDirectorProfesionFk().getProNombre()) ? departamental.getDedDirectorProfesionFk().getProNombre().trim().toUpperCase() : "",//profesion
                    departamental != null && departamental.getDecDirectorDomicilio() != null && StringUtils.isNotBlank(departamental.getDecDirectorDomicilio()) ? departamental.getDecDirectorDomicilio().trim().toUpperCase() : "",//domicilio
                    departamental != null && departamental.getDecDirectorCargo() != null && StringUtils.isNotBlank(departamental.getDecDirectorCargo())? departamental.getDecDirectorCargo().trim().toUpperCase() : "",//cargo_director
                    nombrePresidente,//nombre_presidente
                    ocupacionPresidente,//ocupacion_presidente
                    domicilioPresidente,//domicilio_presidente
                    documentoPresidente,//cip_presidente
                    extendidaPresidente,//extendida_presidente
                    fechaEntensionPresidente,//fecha_extension_presidente
                    cargoPresidente,//cargo_presidente
                    nombreTesorero,//nombre_tesorero
                    ocupacionTesorero,//ocupacion_tesorero
                    domicilioTesorero,//domicilio_tesorero
                    documentoTesorero,//cip_tesorero
                    extendidaTesorero,//extendida_tesorero
                    fechaExtensionTesorero,//fecha_extension_tesorero
                    cargoTesorero,//cargo_presidente
                    nombreConsejal,//nombre_consejal
                    ocupacionConsejal,//ocupacion_consejal
                    domicilioConsejal,//domicilio_consejal
                    documentoConsejal,//cip_consejal
                    extendidaConsejal,//extendida_consejal
                    fechaExtensionConsejal,//fecha_extension_consejal
                    cargoConsejal,//cargo_consejal
                    niveles,//name_4
                    departamentoUnidad,//convenio_1
                    fechaGeneracion,//name_2
                    nombreUnidad
                  );
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
}

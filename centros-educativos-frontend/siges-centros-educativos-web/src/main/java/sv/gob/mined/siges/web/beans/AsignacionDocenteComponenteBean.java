/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgComponenteDocente;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgHorarioEscolar;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativaNoJsonIdentity;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponenteDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponenteDocenteRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.HorarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AsignacionDocenteComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsignacionDocenteComponenteBean.class.getName());

    @Inject
    private ComponentePlanGradoRestClient restClient;

    @Inject
    private ComponentePlanEstudioRestClient cpeRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    
    @Inject
    private PersonalSedeEducativaRestClient restPersonalSede;
    
    @Inject
    private ComponenteDocenteRestClient componenteDocenteRestClient;
    
    @Inject
    private HorarioEscolarRestClient horarioEscolarRestClient;

    private SgHorarioEscolar horarioEscolarEnEdicion;
    private List<SgComponenteDocente> componenteDocentes;
    private Boolean soloLectura=Boolean.FALSE;
    private FiltroComponenteDocente filtro;
    private List<SgComponenteDocente> listCompDocDeHorario;
    private List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes;
    private List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentesAux;
    private Boolean esUnicoDocente;
    private SgComponenteDocente componenteDocenteEnEdicion;
    private SgPersonalSedeEducativaNoJsonIdentity docenteSeleccionado;
    private List<SgComponentePlanEstudio> compPlanEstudioList;
    private Long personalPkEliminarContexto;


    public AsignacionDocenteComponenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            soloLectura = request.getAttribute("soloLectura") != null ? (Boolean) request.getAttribute("soloLectura") : soloLectura;
            horarioEscolarEnEdicion = (SgHorarioEscolar) request.getAttribute("horarioEscolar");
     
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALENDARIOS)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void forceInit() {
    }
    
    public void cargarDatos(){    
            if(horarioEscolarEnEdicion.getHesSeccion()!=null){
                cargarDocentes();
                 if(horarioEscolarEnEdicion.getHesPk()!=null){
                    if(horarioEscolarEnEdicion.getHesUnicoDocente()!=null){
                        if(!horarioEscolarEnEdicion.getHesUnicoDocente()){
                            buscarComponenteDocente();
                        }else{
                            if(horarioEscolarEnEdicion.getHesDocente()!=null){
                                List<SgPersonalSedeEducativaNoJsonIdentity> docs=listaDocentes.stream().filter(c->c.getPsePk().equals(horarioEscolarEnEdicion.getHesDocente().getPsePk())).collect(Collectors.toList());
                                if(!docs.isEmpty()){
                                    horarioEscolarEnEdicion.setHesDocente(docs.get(0));
                                }
                            }
                        }               
                    }
                 }
                 armarListComponenteDocente();
            }
            
            
    }
    
    public void buscarComponenteDocente(){
        try{
            filtro =new FiltroComponenteDocente();
            filtro.setCdoHorarioEscolar(horarioEscolarEnEdicion.getHesPk());
            filtro.setIncluirCampos(new String[]{"cdoPk","cdoVersion","cdoComponente.cpePk","cdoComponente.cpeTipo","cdoComponente.cpeNombre","cdoComponente.cpeVersion","cdoDocente.psePk",
                "cdoDocente.psePersona.perPk","cdoDocente.psePersona.perVersion",
            "cdoDocente.psePersona.perVersion",
            "cdoDocente.psePersona.perPrimerNombre",
        "cdoDocente.psePersona.perSegundoNombre",
        "cdoDocente.psePersona.perPrimerApellido",
        "cdoDocente.psePersona.perSegundoApellido",
        "cdoDocente.psePersona.perDui"});

            listCompDocDeHorario=new ArrayList(componenteDocenteRestClient.buscar(filtro));
         } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void armarListComponenteDocente(){
        
        if(listCompDocDeHorario==null){
            listCompDocDeHorario=new ArrayList();
            for(SgComponentePlanEstudio compCpe:compPlanEstudioList){
                SgComponenteDocente compDoc=new SgComponenteDocente();
                compDoc.setCdoComponente(compCpe);
                compDoc.setCdoHorarioEscolar(horarioEscolarEnEdicion);
                listCompDocDeHorario.add(compDoc);
            }
        }else{
            for(SgComponentePlanEstudio compCpe:compPlanEstudioList){
                List<SgComponenteDocente> existeProfesorAsignado=listCompDocDeHorario.stream().filter(c->c.getCdoComponente().getCpePk().equals(compCpe.getCpePk())).collect(Collectors.toList());
                if(existeProfesorAsignado.isEmpty()){
                    SgComponenteDocente compDoc=new SgComponenteDocente();
                    compDoc.setCdoComponente(compCpe);
                    compDoc.setCdoHorarioEscolar(horarioEscolarEnEdicion);
                    listCompDocDeHorario.add(compDoc);
                }
            }
           
        }
        if(!listCompDocDeHorario.isEmpty()){
                Collections.sort(listCompDocDeHorario,(o1,o2)->o1.getCdoComponente().getCpeNombre().compareTo(o2.getCdoComponente().getCpeNombre()));
            }
        
    }
    
    public void cargarDocentes() {
    try {
            FiltroPersonalSedeEducativa fps = new FiltroPersonalSedeEducativa();
            fps.setPerCentroEducativo(horarioEscolarEnEdicion.getHesSeccion().getSecServicioEducativo().getSduSede().getSedPk());
            fps.setDocenteOActividadDocente(Boolean.TRUE);
            fps.setPersonalActivo(Boolean.TRUE);
            fps.setOrderBy(new String[]{"psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre"});
            fps.setAscending(new boolean[]{true, true, true, true, true, true});
            fps.setIncluirCampos(new String[]{"psePk", "psePersona.perPk", "psePersona.perVersion",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido", "psePersona.perDui", "pseVersion"});

            listaDocentes = restPersonalSede.buscarLite(fps);
            listaDocentesAux=new ArrayList(listaDocentes);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    public void editarDocente(){
        docenteSeleccionado=new SgPersonalSedeEducativaNoJsonIdentity();
    }

    public void editarComponenteDocente(SgComponenteDocente dco){   
          personalPkEliminarContexto=null;
          if(dco.getCdoPk()!=null){
            List<SgComponenteDocente> compDoc=listCompDocDeHorario.stream().filter(c->c.getCdoPk()!=null && c.getCdoDocente().getPsePk().equals(dco.getCdoDocente().getPsePk())).collect(Collectors.toList());
            if(compDoc.size()==1){
                personalPkEliminarContexto = dco.getCdoDocente().getPsePk();
            }            
          }
          componenteDocenteEnEdicion = dco;
          docenteSeleccionado=new SgPersonalSedeEducativaNoJsonIdentity();
          listaDocentesAux=listaDocentes;
            
    }
    
    public void guardarComponenteDocente(){
        try{
            if(horarioEscolarEnEdicion.getHesUnicoDocente()){
                
                SgHorarioEscolar horarioEsc=(SgHorarioEscolar) SerializationUtils.clone(horarioEscolarEnEdicion);
                horarioEsc.setHesDocente(docenteSeleccionado);
                horarioEsc=horarioEscolarRestClient.guardarLite(horarioEsc);
                horarioEscolarEnEdicion.setHesDocente(docenteSeleccionado);
                horarioEscolarEnEdicion.setHesVersion(horarioEsc.getHesVersion());
                horarioEscolarBean.setEntidadEnEdicion(horarioEscolarEnEdicion);
                listCompDocDeHorario=null;
                armarListComponenteDocente();
            }else{               
                SgComponenteDocente compDo=(SgComponenteDocente) SerializationUtils.clone(componenteDocenteEnEdicion);
                compDo.setCdoDocente(docenteSeleccionado);
                compDo.setCdoHorarioEscolar(horarioEscolarEnEdicion); 
                compDo.setEliminarContextoDePersonalPk(personalPkEliminarContexto);
                
                
                
                compDo=componenteDocenteRestClient.guardar(compDo);

                horarioEscolarEnEdicion.setHesDocente(null); 
                horarioEscolarEnEdicion.setHesVersion(compDo.getCdoHorarioEscolar().getHesVersion());
                horarioEscolarBean.setEntidadEnEdicion(horarioEscolarEnEdicion);
                componenteDocenteEnEdicion.setCdoDocente(docenteSeleccionado);
                componenteDocenteEnEdicion.setCdoHorarioEscolar(horarioEscolarEnEdicion); 
                componenteDocenteEnEdicion.setEliminarContextoDePersonalPk(personalPkEliminarContexto);
                componenteDocenteEnEdicion.setCdoPk(compDo.getCdoPk());
                componenteDocenteEnEdicion.setCdoVersion(compDo.getCdoVersion());
           
            }        
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");    
          
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            personalPkEliminarContexto=null;

            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminarDocente(){
        try{
            if(horarioEscolarEnEdicion.getHesUnicoDocente()){
                horarioEscolarEnEdicion.setHesDocente(docenteSeleccionado);
                horarioEscolarEnEdicion.setHesDocentes(new ArrayList());
                horarioEscolarEnEdicion=horarioEscolarRestClient.guardar(horarioEscolarEnEdicion);
                horarioEscolarBean.setEntidadEnEdicion(horarioEscolarEnEdicion);
            }else{               
                
                componenteDocenteEnEdicion.setCdoHorarioEscolar(horarioEscolarEnEdicion); 
                componenteDocenteRestClient.eliminarElemento(componenteDocenteEnEdicion);
                componenteDocenteEnEdicion.setCdoDocente(null);
                componenteDocenteEnEdicion.setCdoPk(null);
                
                componenteDocenteEnEdicion=null;
            }
          
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
          
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public Boolean existenDocentes(){
        if(horarioEscolarEnEdicion.getHesPk()==null){
            return Boolean.TRUE;
        }
        
        Boolean existenDocentesAsignados=Boolean.FALSE;
        if(horarioEscolarEnEdicion.getHesUnicoDocente()!=null){
            if(horarioEscolarEnEdicion.getHesUnicoDocente()){
                if(horarioEscolarEnEdicion.getHesDocente()!=null){
                    existenDocentesAsignados=Boolean.TRUE;
                }
            }else{
                if(listCompDocDeHorario!=null){
                    for(SgComponenteDocente comp:listCompDocDeHorario){
                        if(comp.getCdoDocente()!=null){
                            existenDocentesAsignados=Boolean.TRUE;
                            break;
                        }
                    }
                }
            }
        }
        return existenDocentesAsignados;
    }


    public List<SgComponenteDocente> getComponenteDocentes() {
        return componenteDocentes;
    }

    public void setComponenteDocentes(List<SgComponenteDocente> componenteDocentes) {
        this.componenteDocentes = componenteDocentes;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgHorarioEscolar getHorarioEscolarEnEdicion() {
        return horarioEscolarEnEdicion;
    }

    public void setHorarioEscolarEnEdicion(SgHorarioEscolar horarioEscolarEnEdicion) {
        this.horarioEscolarEnEdicion = horarioEscolarEnEdicion;
    }

    public FiltroComponenteDocente getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroComponenteDocente filtro) {
        this.filtro = filtro;
    }

    public List<SgComponenteDocente> getListCompDocDeHorario() {
        return listCompDocDeHorario;
    }

    public void setListCompDocDeHorario(List<SgComponenteDocente> listCompDocDeHorario) {
        this.listCompDocDeHorario = listCompDocDeHorario;
    }


    public List<SgPersonalSedeEducativaNoJsonIdentity> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public Boolean getEsUnicoDocente() {
        return esUnicoDocente;
    }

    public void setEsUnicoDocente(Boolean esUnicoDocente) {
        this.esUnicoDocente = esUnicoDocente;
    }

    public SgComponenteDocente getComponenteDocenteEnEdicion() {
        return componenteDocenteEnEdicion;
    }

    public void setComponenteDocenteEnEdicion(SgComponenteDocente componenteDocenteEnEdicion) {
        this.componenteDocenteEnEdicion = componenteDocenteEnEdicion;
    }

    public SgPersonalSedeEducativaNoJsonIdentity getDocenteSeleccionado() {
        return docenteSeleccionado;
    }

    public void setDocenteSeleccionado(SgPersonalSedeEducativaNoJsonIdentity docenteSeleccionado) {
        this.docenteSeleccionado = docenteSeleccionado;
    }

    public List<SgComponentePlanEstudio> getCompPlanEstudioList() {
        return compPlanEstudioList;
    }

    public void setCompPlanEstudioList(List<SgComponentePlanEstudio> compPlanEstudioList) {
        this.compPlanEstudioList = compPlanEstudioList;
    }

    public List<SgPersonalSedeEducativaNoJsonIdentity> getListaDocentesAux() {
        return listaDocentesAux;
    }

    public void setListaDocentesAux(List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentesAux) {
        this.listaDocentesAux = listaDocentesAux;
    }

 
}

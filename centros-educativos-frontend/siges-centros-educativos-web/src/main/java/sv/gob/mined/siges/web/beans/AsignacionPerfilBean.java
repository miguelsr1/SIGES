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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAsignacionPerfil;
import sv.gob.mined.siges.web.dto.SgAsignacionPerfilPersonal;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativaNoJsonIdentity;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgPerfilesUsuariosIngresadosCe;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsignacionPerfil;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsignacionPerfilPersonal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyAsignacionPerfilDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsignacionPerfilPersonalRestClient;
import sv.gob.mined.siges.web.restclient.AsignacionPerfilRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AsignacionPerfilBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsignacionPerfilBean.class.getName());

    @Inject
    private AsignacionPerfilRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private AsignacionPerfilPersonalRestClient asignacionPerfilPersonalrestClient;
    
    @Inject
    private PersonalSedeEducativaRestClient restPersonalSede;

    @Inject
    @Param(name = "id")
    private Long asignacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private FiltroAsignacionPerfil filtro = new FiltroAsignacionPerfil();
    private SgAsignacionPerfil entidadEnEdicion = new SgAsignacionPerfil();
    private List<RevHistorico> historialAsignacionPerfil = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAsignacionPerfilDataModel asignacionPerfilLazyModel;
    private SgSede sedeSeleccionada;
    private Boolean soloLectura;
    private List<SgPerfilesUsuariosIngresadosCe> listPerfiles;
    private List<SgAsignacionPerfilPersonal> listPerfilPersonal;
    private List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes;
    private List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentesAux;
    private SgAsignacionPerfilPersonal asignacionPerfilPersonalEnEdicion;
    private SgPersonalSedeEducativaNoJsonIdentity personalSeleccionado;

    public AsignacionPerfilBean() {
    }

    @PostConstruct
    public void init() {
        try {
            buscarPerfiles();
            if (asignacionId != null && asignacionId > 0) {
                actualizar(restClient.obtenerPorId(asignacionId));
                soloLectura = editable != null ? !editable : soloLectura;
                cargarDocentes();
            } else {
                agregar();
            }
            cargarSedePorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDocentes() {
    try {
        listaDocentes =new ArrayList();
        listaDocentesAux=new ArrayList();
        if(entidadEnEdicion.getApeSedeFk()!=null){
            FiltroPersonalSedeEducativa fps = new FiltroPersonalSedeEducativa();
            fps.setPerCentroEducativo(entidadEnEdicion.getApeSedeFk().getSedPk());
            fps.setDocenteOActividadDocente(Boolean.TRUE);
            fps.setPersonalActivo(Boolean.TRUE);
            fps.setOrderBy(new String[]{"psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre"});
            fps.setAscending(new boolean[]{true, true, true, true, true, true});
            fps.setIncluirCampos(new String[]{"psePk",  "psePersona.perPk", "psePersona.perVersion",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido", "psePersona.perDui", "pseVersion"});

            listaDocentes = restPersonalSede.buscarLite(fps);
            listaDocentesAux=new ArrayList(listaDocentes);
        }else{
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar sede", "");
            return;
        }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void buscarPerfiles() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"puiNombre"});
            fc.setAscending(new boolean[]{true});
            listPerfiles = catalogoRestClient.buscarPerfiles(fc);

        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            seleccionarSede();
        }
    }

    public void seleccionarSede() {
        try {
            listPerfilPersonal = new ArrayList();
            entidadEnEdicion.setApeSedeFk(null);

            if (sedeSeleccionada != null) {
                entidadEnEdicion.setApeSedeFk(sedeSeleccionada);

                FiltroAsignacionPerfil fpp = new FiltroAsignacionPerfil();
                fpp.setApeSedeFk(sedeSeleccionada.getSedPk());
                fpp.setAgregarSede(Boolean.TRUE);
                List<SgAsignacionPerfil> as = restClient.buscar(fpp);
                if (as != null && !as.isEmpty()) {
                    entidadEnEdicion = as.get(0);
                }
                buscarAsignacionPerfilPersonal();
                cargarDocentes();
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarAsignacionPerfilPersonal() {
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getApePk() != null) {
                FiltroAsignacionPerfilPersonal fpp = new FiltroAsignacionPerfilPersonal();
                fpp.setAppAsignacionPerfilFk(entidadEnEdicion.getApePk());
                fpp.setIncluirCampos(new String[]{
                    "appPk",
                    "appVersion", 
                    "appPerfilFk.puiPk",
                    "appPerfilFk.puiNombre",
                    "appPerfilFk.puiVersion",
                    "appAsignacionPerfilFk.apePk",
                    "appAsignacionPerfilFk.apeSedeFk.sedPk",
                    "appAsignacionPerfilFk.apeSedeFk.sedVersion",
                    "appAsignacionPerfilFk.apeSedeFk.sedTipo",
                    "appAsignacionPerfilFk.apeVersion",
                    "appPersonalFk.psePk",
                    "appPersonalFk.psePersona.perPk",
                    "appPersonalFk.psePersona.perVersion",
                    "appPersonalFk.psePersona.perPrimerNombre",
                    "appPersonalFk.psePersona.perSegundoNombre",
                    "appPersonalFk.psePersona.perPrimerApellido",
                    "appPersonalFk.psePersona.perSegundoApellido",
                    "appPersonalFk.psePersona.perDui",
                    "appPersonalFk.pseVersion"          
                });
                List<SgAsignacionPerfilPersonal> perPer= asignacionPerfilPersonalrestClient.buscar(fpp);
          
                entidadEnEdicion.setApeAsignacionesPerfilPersonal(perPer);
            }
            prepararListaPerfilesPersonal();
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);        
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void prepararListaPerfilesPersonal() {
        listPerfilPersonal = new ArrayList();
        if (!listPerfiles.isEmpty()) {
            List<SgPerfilesUsuariosIngresadosCe> perilesSinAsignar = new ArrayList(listPerfiles);
            if (entidadEnEdicion.getApePk() != null && entidadEnEdicion.getApeAsignacionesPerfilPersonal().size() > 0) {
                listPerfilPersonal.addAll(entidadEnEdicion.getApeAsignacionesPerfilPersonal());
                List<SgPerfilesUsuariosIngresadosCe> perfiles = entidadEnEdicion.getApeAsignacionesPerfilPersonal().stream().map(c -> c.getAppPerfilFk()).collect(Collectors.toList());
                perilesSinAsignar.removeAll(perfiles);
            }

            for (SgPerfilesUsuariosIngresadosCe per : perilesSinAsignar) {
                SgAsignacionPerfilPersonal asigPerfil = new SgAsignacionPerfilPersonal();
                asigPerfil.setAppPerfilFk(per);
                listPerfilPersonal.add(asigPerfil);
            }
            Collections.sort(listPerfilPersonal, (o1, o2) -> o1.getAppPerfilFk().getPuiNombre().compareTo(o2.getAppPerfilFk().getPuiNombre()));

        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "La lista de perfiles es vacia", "");
            return;
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }


    public void limpiar() {
        filtro = new FiltroAsignacionPerfil();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAsignacionPerfil();
    }

    public void actualizar(SgAsignacionPerfil var) {        
        entidadEnEdicion = (SgAsignacionPerfil) SerializationUtils.clone(var);
        buscarAsignacionPerfilPersonal();
        
    }
    
   
    public void prepararAsignacionPerfilPersonal(SgAsignacionPerfilPersonal per){
        JSFUtils.limpiarMensajesError();
        personalSeleccionado=new SgPersonalSedeEducativaNoJsonIdentity();
        asignacionPerfilPersonalEnEdicion=(SgAsignacionPerfilPersonal) SerializationUtils.clone(per);
        listaDocentesAux=listaDocentes;
    }

    public void guardarAsignacionPerfilPersonal() {
        try {
            if(asignacionPerfilPersonalEnEdicion.getAppPerfilFk()==null ){
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar perfil.", "");
                return;
            }
            if(personalSeleccionado==null){
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Debe seleccionar personal.", "");
                return;
            }
            asignacionPerfilPersonalEnEdicion.setAppPersonalFk(personalSeleccionado);
            if(entidadEnEdicion.getApePk()==null){
                List<SgAsignacionPerfilPersonal> list=new ArrayList();
                list.add(asignacionPerfilPersonalEnEdicion);
                entidadEnEdicion.setApeAsignacionesPerfilPersonal(list);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                this.actualizar(entidadEnEdicion);
            }else{
                asignacionPerfilPersonalEnEdicion.setAppAsignacionPerfilFk(entidadEnEdicion);
                asignacionPerfilPersonalrestClient.guardar(asignacionPerfilPersonalEnEdicion);
                this.actualizar(entidadEnEdicion);
            }
            
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            PrimeFaces.current().executeScript("PF('dialogTable').clearFilters();");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            personalSeleccionado=null;
            asignacionPerfilPersonalEnEdicion=null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminarAsignacionPerfilPersonal(){
        try{
            if(asignacionPerfilPersonalEnEdicion.getAppPk()!=null){
                asignacionPerfilPersonalrestClient.eliminar(asignacionPerfilPersonalEnEdicion.getAppPk());
                actualizar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            }
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getApePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");           
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialAsignacionPerfil = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroAsignacionPerfil getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAsignacionPerfil filtro) {
        this.filtro = filtro;
    }

    public SgAsignacionPerfil getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAsignacionPerfil entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialAsignacionPerfil() {
        return historialAsignacionPerfil;
    }

    public void setHistorialAsignacionPerfil(List<RevHistorico> historialAsignacionPerfil) {
        this.historialAsignacionPerfil = historialAsignacionPerfil;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyAsignacionPerfilDataModel getAsignacionPerfilLazyModel() {
        return asignacionPerfilLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyAsignacionPerfilDataModel asignacionPerfilLazyModel) {
        this.asignacionPerfilLazyModel = asignacionPerfilLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgPerfilesUsuariosIngresadosCe> getListPerfiles() {
        return listPerfiles;
    }

    public void setListPerfiles(List<SgPerfilesUsuariosIngresadosCe> listPerfiles) {
        this.listPerfiles = listPerfiles;
    }

    public List<SgAsignacionPerfilPersonal> getListPerfilPersonal() {
        return listPerfilPersonal;
    }

    public void setListPerfilPersonal(List<SgAsignacionPerfilPersonal> listPerfilPersonal) {
        this.listPerfilPersonal = listPerfilPersonal;
    }

    public List<SgPersonalSedeEducativaNoJsonIdentity> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public List<SgPersonalSedeEducativaNoJsonIdentity> getListaDocentesAux() {
        return listaDocentesAux;
    }

    public void setListaDocentesAux(List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentesAux) {
        this.listaDocentesAux = listaDocentesAux;
    }

    public SgAsignacionPerfilPersonal getAsignacionPerfilPersonalEnEdicion() {
        return asignacionPerfilPersonalEnEdicion;
    }

    public void setAsignacionPerfilPersonalEnEdicion(SgAsignacionPerfilPersonal asignacionPerfilPersonalEnEdicion) {
        this.asignacionPerfilPersonalEnEdicion = asignacionPerfilPersonalEnEdicion;
    }

    public SgPersonalSedeEducativaNoJsonIdentity getPersonalSeleccionado() {
        return personalSeleccionado;
    }

    public void setPersonalSeleccionado(SgPersonalSedeEducativaNoJsonIdentity personalSeleccionado) {
        this.personalSeleccionado = personalSeleccionado;
    }

}

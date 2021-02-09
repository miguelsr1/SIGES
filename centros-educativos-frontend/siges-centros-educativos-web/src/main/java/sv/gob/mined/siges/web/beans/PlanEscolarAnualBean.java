/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgPlanEscolarAnual;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlanEscolarAnualRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.dto.SgPropuestaPedagogica;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgRecurso;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPEA;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPropuestaPedagogica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DetallePlanEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PropuestaPedagogicaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PlanEscolarAnualBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlanEscolarAnualBean.class.getName());

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private PlanEscolarAnualRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PropuestaPedagogicaRestClient restPropuesta;

    @Inject
    private DetallePlanEscolarRestClient restDetalle;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private AnioLectivoRestClient restAnio;

    @Inject
    @Param(name = "id")
    private Long planId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "validar")
    private Boolean validar;
    
    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    private Boolean soloLectura = Boolean.FALSE;
    private SgPlanEscolarAnual entidadEnEdicion = new SgPlanEscolarAnual();    
    private SgDetallePlanEscolar entidadEnEdicionDetallePlan = new SgDetallePlanEscolar();    
    private SgSede sedeSeleccionada;
    private SofisComboG<SgPropuestaPedagogica> comboPropuesta;
    private SofisComboG<SgRecurso> comboRecurso;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private Boolean editarDetalle = Boolean.TRUE;
    private List<SgDetallePlanEscolar> listaDetallesValidos = new ArrayList<>();
    private Boolean procesarEnviar = Boolean.FALSE;
    private List<RevHistorico> historial = new ArrayList();
    private Integer cantidadCaracteres=500;
    
    public PlanEscolarAnualBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (planId != null && planId > 0) {
                this.actualizar(restClient.obtenerPorId(planId));
                soloLectura = editable != null ? !editable : soloLectura;
                if(entidadEnEdicion.getPeaEstado()!=null && entidadEnEdicion.getPeaEstado().equals(EnumEstadoPEA.REVISADO)){
                    procesarEnviar = Boolean.TRUE;
                    soloLectura = Boolean.TRUE;
                }else if(entidadEnEdicion.getPeaEstado()!=null && entidadEnEdicion.getPeaEstado().equals(EnumEstadoPEA.VALIDADO)){
                    procesarEnviar = Boolean.FALSE;
                    soloLectura = Boolean.TRUE;
                }
            } else {
                agregar();
            }
                                

            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }
 

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTENTICADO)) {
                JSFUtils.redirectToIndex();
            }
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_VALIDAR_PEA)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getPeaPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_PLAN_ESCOLAR_ANUAL)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_PLAN_ESCOLAR_ANUAL)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void actualizar(SgPlanEscolarAnual cal) {
        entidadEnEdicion = cal;
        sedeSeleccionada = entidadEnEdicion.getPeaSede();
        seleccionarSede();
        comboPropuesta.setSelectedT(entidadEnEdicion.getPeaPropuestaPedagogica());
        comboAnioLectivo.setSelectedT(entidadEnEdicion.getPeaAnioLectivo());
        
        if(!entidadEnEdicion.getPeaDetallePlanEscolar().isEmpty()){
            listaDetallesValidos = entidadEnEdicion.getPeaDetallePlanEscolar()
                    .stream()
                    .filter((d) -> BooleanUtils.isTrue(d.getDpeValidado()))
                    .collect(Collectors.toList());
        }
    }
    
    public void cargarCombos() {
        try {
            comboPropuesta = new SofisComboG();
            comboPropuesta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera filtroC = new FiltroCodiguera();
            filtroC.setHabilitado(Boolean.TRUE);
            
            filtroC.setOrderBy(new String[]{"ffiNombre"});
            filtroC.setIncluirCampos(new String[]{"ffiNombre", "ffiVersion"});
            List<SgFuenteFinanciamiento> listaFuenteFinanciamiento = restCatalogo.buscarFuenteFinanciamiento(filtroC);
            comboFuenteFinanciamiento = new SofisComboG(listaFuenteFinanciamiento, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            filtroC.setOrderBy(new String[]{"rcsNombre"});
            filtroC.setIncluirCampos(new String[]{"rcsNombre", "rcsVersion"});
            List<SgRecurso> listaRecursos = restCatalogo.buscarRecurso(filtroC);
            comboRecurso = new SofisComboG(listaRecursos, "rcsNombre");
            comboRecurso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
            fc2.setOrderBy(new String[]{"aleAnio"});
            fc2.setAscending(new boolean[]{false});
            fc2.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            comboAnioLectivo = new SofisComboG(restAnio.buscar(fc2), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
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
            return sedeClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void seleccionarSede(){
        try {
            
            comboPropuesta = new SofisComboG();
            comboPropuesta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if (sedeSeleccionada != null) {
                FiltroPropuestaPedagogica fp = new FiltroPropuestaPedagogica();
                fp.setPpeSede(sedeSeleccionada.getSedPk());
                fp.setAscending(new boolean[]{true});
                fp.setOrderBy(new String[]{"ppeNombre"});
                fp.setIncluirCampos(new String[]{"ppePk", "ppeNombre", "ppeVersion"});
                
                List<SgPropuestaPedagogica> lispe = restPropuesta.buscar(fp);

                comboPropuesta = new SofisComboG(lispe, "ppeNombre");
                comboPropuesta.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
    }

    public void limpiar() {
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgPlanEscolarAnual();
        sedeSeleccionada = sessionBean.getSedeDefecto();
        seleccionarSede();
    }


    public void guardar() {
        try {
            entidadEnEdicion.setPeaSede(sedeSeleccionada);
            entidadEnEdicion.setPeaPropuestaPedagogica(comboPropuesta!=null?comboPropuesta.getSelectedT():null);
            entidadEnEdicion.setPeaAnioLectivo(comboAnioLectivo!=null?comboAnioLectivo.getSelectedT():null);
            if(entidadEnEdicion.getPeaPk()==null){
                entidadEnEdicion.setPeaEstado(EnumEstadoPEA.ENVIADO);
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void revisar() {
        try {
            entidadEnEdicion.setPeaEstado(EnumEstadoPEA.REVISADO);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.PLANES_ESCOLAR_ANUAL);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validar() {
        try {
            
            for(SgDetallePlanEscolar det : listaDetallesValidos){
                det.setDpeValidado(Boolean.TRUE);
                entidadEnEdicion.getPeaDetallePlanEscolar().set(entidadEnEdicion.getPeaDetallePlanEscolar().indexOf(det) , det);
            }
            
            Long noValidadas = listaDetallesValidos
                    .stream()
                    .filter((d) -> BooleanUtils.isFalse(d.getDpeValidado()))
                    .count();
            
            if(listaDetallesValidos.size() == entidadEnEdicion.getPeaDetallePlanEscolar().size() && noValidadas==0){
                entidadEnEdicion.setPeaEstado(EnumEstadoPEA.VALIDADO);
                entidadEnEdicion.setPeaFechaValido(LocalDate.now());
                entidadEnEdicion.setPeaUsuarioValido(sessionBean.getEntidadUsuario());
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);

                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.PLANES_ESCOLAR_ANUAL);
            }else{
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VALIDAR_ACTIVIADADES), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    public void limpiarCombosDetalle() {
        entidadEnEdicionDetallePlan = new SgDetallePlanEscolar();
        comboFuenteFinanciamiento.setSelected(-1);
        comboRecurso.setSelected(-1);
        JSFUtils.limpiarMensajesError();
    }
    
    

    public void guardarDetalle() {
        try {
            entidadEnEdicionDetallePlan.setDpePlanEscolarAnual(entidadEnEdicion);
            entidadEnEdicionDetallePlan.setDpeRecursos(comboRecurso!=null?comboRecurso.getSelectedT():null);
            entidadEnEdicionDetallePlan.setDpeFuenteFinanciamiento(comboFuenteFinanciamiento!=null?comboFuenteFinanciamiento.getSelectedT():null);
            entidadEnEdicionDetallePlan = restDetalle.guardar(entidadEnEdicionDetallePlan);
            
            if(entidadEnEdicion.getPeaDetallePlanEscolar().contains(entidadEnEdicionDetallePlan)){
                entidadEnEdicion.getPeaDetallePlanEscolar().set(entidadEnEdicion.getPeaDetallePlanEscolar().indexOf(entidadEnEdicionDetallePlan) , entidadEnEdicionDetallePlan);
            }else{
                entidadEnEdicion.getPeaDetallePlanEscolar().add(entidadEnEdicionDetallePlan);
            }
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void validarDetalle() {
        try {
            entidadEnEdicionDetallePlan.setDpeValidado(Boolean.TRUE);
            entidadEnEdicionDetallePlan = restDetalle.guardar(entidadEnEdicionDetallePlan);
            
            if(entidadEnEdicion.getPeaDetallePlanEscolar().contains(entidadEnEdicionDetallePlan)){
                entidadEnEdicion.getPeaDetallePlanEscolar().set(entidadEnEdicion.getPeaDetallePlanEscolar().indexOf(entidadEnEdicionDetallePlan) , entidadEnEdicionDetallePlan);
            }else{
                entidadEnEdicion.getPeaDetallePlanEscolar().add(entidadEnEdicionDetallePlan);
            }
            
            listaDetallesValidos.add(entidadEnEdicionDetallePlan);
            
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarDetalle(SgDetallePlanEscolar var) {
        editarDetalle = Boolean.TRUE;
        limpiarCombos();
        entidadEnEdicionDetallePlan = (SgDetallePlanEscolar) SerializationUtils.clone(var);
        comboFuenteFinanciamiento.setSelectedT(entidadEnEdicionDetallePlan.getDpeFuenteFinanciamiento());
        comboRecurso.setSelectedT(entidadEnEdicionDetallePlan.getDpeRecursos());
        JSFUtils.limpiarMensajesError();
    }

    public void actualizarDetalleFoto(Long var) {
        try{
            if(soloLectura){
                editarDetalle = Boolean.FALSE;
            }else{
                editarDetalle = Boolean.TRUE;
            }
            entidadEnEdicionDetallePlan = restDetalle.obtenerPorId(var);
            JSFUtils.limpiarMensajesError();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    public void guardarDetalleFoto() {
        try {
            entidadEnEdicionDetallePlan = restDetalle.guardar(entidadEnEdicionDetallePlan);
            
            PrimeFaces.current().executeScript("PF('itemDialog2').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void verDetalle(SgDetallePlanEscolar var) {
        editarDetalle = Boolean.FALSE;
        limpiarCombos();
        entidadEnEdicionDetallePlan = (SgDetallePlanEscolar) SerializationUtils.clone(var);
        comboFuenteFinanciamiento.setSelectedT(entidadEnEdicionDetallePlan.getDpeFuenteFinanciamiento());
        comboRecurso.setSelectedT(entidadEnEdicionDetallePlan.getDpeRecursos());
        JSFUtils.limpiarMensajesError();
    }

    public void eliminarDetalle() {
        try {
            restDetalle.eliminar(entidadEnEdicionDetallePlan.getDpePk());
            entidadEnEdicion.getPeaDetallePlanEscolar().remove(entidadEnEdicionDetallePlan);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicionDetallePlan = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    

    public void handleFileUpload(FileUploadEvent event) {
        SgArchivo nuevo = new SgArchivo();
        FileUploadUtils.handleFileUpload(event.getFile(), nuevo, tmpBasePath);
        if(entidadEnEdicionDetallePlan.getDpeFotos() == null){
            entidadEnEdicionDetallePlan.setDpeFotos(new ArrayList<>());
        }
        entidadEnEdicionDetallePlan.getDpeFotos().add(nuevo);
    }

    public void eliminarFotoUnica() {
        String valorPk = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pk");
        Long fotoPk = (valorPk!=null?Long.valueOf(valorPk):null);
        String temporal =  FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tempo");
        
        SgArchivo eliminar = null;
        
        if(fotoPk!=null){
            eliminar = entidadEnEdicionDetallePlan.getDpeFotos().stream()
                    .filter((arc) -> Objects.equals(arc.getAchPk(), fotoPk))
                    .findAny()
                    .orElse(null);
        }else{
            eliminar = entidadEnEdicionDetallePlan.getDpeFotos().stream()
                    .filter((arc) -> Objects.equals(arc.getAchTmpPath(), temporal))
                    .findAny()
                    .orElse(null);
        }
        
        entidadEnEdicionDetallePlan.getDpeFotos().remove(eliminar);
    }
    
    public String cargarHistorial(Long id) {
        try {
            historial = restDetalle.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public SgPlanEscolarAnual getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPlanEscolarAnual entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgPropuestaPedagogica> getComboPropuesta() {
        return comboPropuesta;
    }

    public void setComboPropuesta(SofisComboG<SgPropuestaPedagogica> comboPropuesta) {
        this.comboPropuesta = comboPropuesta;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public SgDetallePlanEscolar getEntidadEnEdicionDetallePlan() {
        return entidadEnEdicionDetallePlan;
    }

    public void setEntidadEnEdicionDetallePlan(SgDetallePlanEscolar entidadEnEdicionDetallePlan) {
        this.entidadEnEdicionDetallePlan = entidadEnEdicionDetallePlan;
    }

    public SofisComboG<SgRecurso> getComboRecurso() {
        return comboRecurso;
    }

    public void setComboRecurso(SofisComboG<SgRecurso> comboRecurso) {
        this.comboRecurso = comboRecurso;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public Boolean getEditarDetalle() {
        return editarDetalle;
    }

    public void setEditarDetalle(Boolean editarDetalle) {
        this.editarDetalle = editarDetalle;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public List<SgDetallePlanEscolar> getListaDetallesValidos() {
        return listaDetallesValidos;
    }

    public void setListaDetallesValidos(List<SgDetallePlanEscolar> listaDetallesValidos) {
        this.listaDetallesValidos = listaDetallesValidos;
    }

    public Boolean getProcesarEnviar() {
        return procesarEnviar;
    }

    public void setProcesarEnviar(Boolean procesarEnviar) {
        this.procesarEnviar = procesarEnviar;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<RevHistorico> historial) {
        this.historial = historial;
    }

    public Integer getCantidadCaracteres() {
        return cantidadCaracteres;
    }

    public void setCantidadCaracteres(Integer cantidadCaracteres) {
        this.cantidadCaracteres = cantidadCaracteres;
    }
    
}

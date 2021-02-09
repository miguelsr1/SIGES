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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.web.dto.SgRelModEdModAten;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelModEdModAten;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyHabilitacionPeriodoMatriculaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.HabilitacionPeriodoMatriculaRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.RelModEdModAtenRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class HabilitacionPeriodoMatriculaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HabilitacionPeriodoMatriculaBean.class.getName());

    @Inject
    private HabilitacionPeriodoMatriculaRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private RelModEdModAtenRestClient relModRestClient;
    
    @Inject
    private CatalogosRestClient catalogoRestClient;

    private FiltroHabilitacionPeriodoMatricula filtro = new FiltroHabilitacionPeriodoMatricula();
    private SgHabilitacionPeriodoMatricula entidadEnEdicion = new SgHabilitacionPeriodoMatricula();
    private List<RevHistorico> historialHabilitacionPeriodoMatricula = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyHabilitacionPeriodoMatriculaDataModel habilitacionPeriodoMatriculaLazyModel;
    private Boolean soloLectura;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgModalidadAtencion> modalidadAtencionCombo;
    private SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo;
    private SofisComboG<SgNivel> nivelCombo;
    private List<SgRelModEdModAten> listRelModEdModAten;
    private String mensajeMatricula;

    @Inject
    @Param(name = "id")
    private Long habilitacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    public HabilitacionPeriodoMatriculaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (habilitacionId != null && habilitacionId > 0) {
                actualizar(new SgHabilitacionPeriodoMatricula(habilitacionId, 0));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                agregar();
            }
            cargarCombos();
            cargarSedePorDefecto();
            buscarMensajes() ;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
     public void buscarMensajes() {
        try {
            mensajeMatricula=null;
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.SOLICITUD_HABILITACION_PERIODO_MAT);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if(conf!=null && !conf.isEmpty() && !StringUtils.isBlank(conf.get(0).getConValor())){
                mensajeMatricula=conf.get(0).getConValor();
            }
        
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            seleccionarSede();
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

    public void seleccionarSede() {
        try {
            entidadEnEdicion.setHmpSedeFk(null);
            entidadEnEdicion.setHmpRelHabilitacionMatriculaNivel(new ArrayList());
            nivelCombo = new SofisComboG();
            nivelCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            modalidadAtencionCombo = new SofisComboG();
            modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            subModalidadAtencionCombo = null;
            listRelModEdModAten = new ArrayList();
            
            if (sedeSeleccionada != null) {
                entidadEnEdicion.setHmpSedeFk(sedeSeleccionada);
                FiltroNivel fc = new FiltroNivel();
                fc.setNivHabilitado(Boolean.TRUE);
                fc.setSedPk(sedeSeleccionada.getSedPk());
                fc.setIncluirCampos(new String[]{"nivPk", "nivNombre", "nivVersion"});
                List<SgNivel> listNivel = nivelRestClient.buscar(fc);
                nivelCombo = new SofisComboG(listNivel, "nivNombre");
                nivelCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarNivel() {
        try {
            modalidadAtencionCombo = new SofisComboG();
            modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            subModalidadAtencionCombo = null;
            listRelModEdModAten = new ArrayList();
            if (sedeSeleccionada != null && nivelCombo.getSelectedT() != null) {
                FiltroRelModEdModAten filtroRel = new FiltroRelModEdModAten();
                filtroRel.setReaNivel(nivelCombo.getSelectedT().getNivPk());
                filtroRel.setSedePk(sedeSeleccionada.getSedPk());
                listRelModEdModAten = relModRestClient.buscar(filtroRel);

                List<SgModalidadAtencion> listModAt = listRelModEdModAten.stream().map(c -> c.getReaModalidadAtencion()).distinct().collect(Collectors.toList());
                modalidadAtencionCombo = new SofisComboG(listModAt, "matNombre");
                modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarModalidadAtencion() {
        try {
            subModalidadAtencionCombo = null;
            if (modalidadAtencionCombo.getSelectedT() != null && nivelCombo.getSelectedT() != null) {

                List<SgSubModalidadAtencion> listSub = listRelModEdModAten.stream().filter(c -> c.getReaModalidadAtencion().equals(modalidadAtencionCombo.getSelectedT()) && c.getReaSubModalidadAtencion() != null).map(c -> c.getReaSubModalidadAtencion()).collect(Collectors.toList());
                if (!listSub.isEmpty()) {
                    subModalidadAtencionCombo = new SofisComboG(listSub, "smoNombre");
                    subModalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarNivel() {
        if (nivelCombo.getSelectedT() != null) {
            SgRelHabilitacionMatriculaNivel rel = new SgRelHabilitacionMatriculaNivel();
            rel.setRhnNivelFk(nivelCombo.getSelectedT());

            if (modalidadAtencionCombo.getSelectedT() == null) {

                List<SgRelHabilitacionMatriculaNivel> listaRelacionesNivel = new ArrayList();
                for (SgRelHabilitacionMatriculaNivel relNiv : entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel()) {

                    if (!relNiv.getRhnNivelFk().equals(nivelCombo.getSelectedT())) {
                        listaRelacionesNivel.add(relNiv);
                    }
                }
                listaRelacionesNivel.add(rel);
                entidadEnEdicion.setHmpRelHabilitacionMatriculaNivel(listaRelacionesNivel);
                Collections.sort(entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel(), (o1, o2) -> o1.getRhnNivelFk().getNivNombre().compareTo(o2.getRhnNivelFk().getNivNombre()));

            } else {
                rel.setRhnModalidadAtencionFk(modalidadAtencionCombo.getSelectedT());

                List<SgSubModalidadAtencion> listSub = listRelModEdModAten.stream().filter(c -> c.getReaModalidadAtencion().equals(modalidadAtencionCombo.getSelectedT()) && c.getReaSubModalidadAtencion() != null).map(c -> c.getReaSubModalidadAtencion()).collect(Collectors.toList());

                if (!listSub.isEmpty()) {
                    if (subModalidadAtencionCombo.getSelectedT() == null) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SUBMODALIDAD_VACIO), "");
                        return;
                    } else {
                        rel.setRhnSubmodaliadadFk(subModalidadAtencionCombo.getSelectedT());
                    }

                }

                List<SgRelHabilitacionMatriculaNivel> soloNivel = entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel().stream().filter(c -> c.getRhnNivelFk().equals(nivelCombo.getSelectedT()) && c.getRhnModalidadAtencionFk() == null).collect(Collectors.toList());

                if (!soloNivel.isEmpty()) {
                    List<SgRelHabilitacionMatriculaNivel> listaRelacionesNivel = new ArrayList();
                    for (SgRelHabilitacionMatriculaNivel relNiv : entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel()) {

                        if (!relNiv.getRhnNivelFk().equals(nivelCombo.getSelectedT())) {
                            listaRelacionesNivel.add(relNiv);
                        }
                    }
                    listaRelacionesNivel.add(rel);
                    entidadEnEdicion.setHmpRelHabilitacionMatriculaNivel(listaRelacionesNivel);
                }else{
                   List<SgRelHabilitacionMatriculaNivel> elemento = entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel().stream().filter(c -> c.getRhnNivelFk().equals(nivelCombo.getSelectedT()) 
                           && modalidadAtencionCombo.getSelectedT().equals(c.getRhnModalidadAtencionFk())
                           && ( (subModalidadAtencionCombo.getSelectedT()!=null && c.getRhnSubmodaliadadFk()!=null)?subModalidadAtencionCombo.getSelectedT().equals(c.getRhnSubmodaliadadFk())
                                   :((subModalidadAtencionCombo.getSelectedT()==null && c.getRhnSubmodaliadadFk()==null) ? Boolean.TRUE:Boolean.FALSE))).collect(Collectors.toList());
 
                   if(elemento.isEmpty()){
                       entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel().add(rel);
                   }
                }
                Collections.sort(entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel(), (o1, o2) -> o1.getRhnNivelFk().getNivNombre().compareTo(o2.getRhnNivelFk().getNivNombre()));
            }

        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NIVEL_VACIO), "");
        }
    }

    public void eliminarNivel(SgRelHabilitacionMatriculaNivel rel) {
        if (entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel() != null) {
            entidadEnEdicion.getHmpRelHabilitacionMatriculaNivel().remove(rel);
        }
    }
    
    public void cargarCombos() {
        nivelCombo = new SofisComboG();
        nivelCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        modalidadAtencionCombo = new SofisComboG();
        modalidadAtencionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroHabilitacionPeriodoMatricula();

    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgHabilitacionPeriodoMatricula();
    }

    public void actualizar(SgHabilitacionPeriodoMatricula var) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(var.getHmpPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try { 
            
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.HABILITACIONES_PERIODOS_MATRICULA);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aprobar() {
        try {
            entidadEnEdicion = restClient.aprobar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.APROBADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void rechazar() {
        try {
            entidadEnEdicion = restClient.rechazar(entidadEnEdicion);
            actualizar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.RECHAZADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String tituloDialog() {
        if (BooleanUtils.isFalse(soloLectura) && EnumEstadoHabilitacionPeriodoMatricula.PENDIENTE.equals(entidadEnEdicion.getHmpEstado())) {
            return Etiquetas.getValue("hevaluarCambioMatricula");
        }
        return Etiquetas.getValue("hsolicitudesCambiosMatricula");
    }

    public FiltroHabilitacionPeriodoMatricula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroHabilitacionPeriodoMatricula filtro) {
        this.filtro = filtro;
    }

    public SgHabilitacionPeriodoMatricula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgHabilitacionPeriodoMatricula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialHabilitacionPeriodoMatricula() {
        return historialHabilitacionPeriodoMatricula;
    }

    public void setHistorialHabilitacionPeriodoMatricula(List<RevHistorico> historialHabilitacionPeriodoMatricula) {
        this.historialHabilitacionPeriodoMatricula = historialHabilitacionPeriodoMatricula;
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

    public LazyHabilitacionPeriodoMatriculaDataModel getHabilitacionPeriodoMatriculaLazyModel() {
        return habilitacionPeriodoMatriculaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyHabilitacionPeriodoMatriculaDataModel habilitacionPeriodoMatriculaLazyModel) {
        this.habilitacionPeriodoMatriculaLazyModel = habilitacionPeriodoMatriculaLazyModel;
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

    public SofisComboG<SgModalidadAtencion> getModalidadAtencionCombo() {
        return modalidadAtencionCombo;
    }

    public void setModalidadAtencionCombo(SofisComboG<SgModalidadAtencion> modalidadAtencionCombo) {
        this.modalidadAtencionCombo = modalidadAtencionCombo;
    }

    public SofisComboG<SgSubModalidadAtencion> getSubModalidadAtencionCombo() {
        return subModalidadAtencionCombo;
    }

    public void setSubModalidadAtencionCombo(SofisComboG<SgSubModalidadAtencion> subModalidadAtencionCombo) {
        this.subModalidadAtencionCombo = subModalidadAtencionCombo;
    }

    public SofisComboG<SgNivel> getNivelCombo() {
        return nivelCombo;
    }

    public void setNivelCombo(SofisComboG<SgNivel> nivelCombo) {
        this.nivelCombo = nivelCombo;
    }


    public List<SgRelModEdModAten> getListRelModEdModAten() {
        return listRelModEdModAten;
    }

    public void setListRelModEdModAten(List<SgRelModEdModAten> listRelModEdModAten) {
        this.listRelModEdModAten = listRelModEdModAten;
    }

    public String getMensajeMatricula() {
        return mensajeMatricula;
    }

    public void setMensajeMatricula(String mensajeMatricula) {
        this.mensajeMatricula = mensajeMatricula;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgManifestacionViolencia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroManifestacionViolencia;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ManifestacionViolenciaRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroTiposManifestacionViolencia;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ManifestacionViolenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ManifestacionViolenciaBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long estId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private ManifestacionViolenciaRestClient restClientEstVal;
    
    @Inject
    private SessionBean sessionBean;

    private SgEstudiante entidadEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgTipoManifestacion> comboTipoManifestacion;

    private List<SgManifestacionViolencia> manifestaciones;
    private SgManifestacionViolencia selectedManifestacion = new SgManifestacionViolencia();
    private Boolean editManifestacion;

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void cargarCombos() {
        try {

            FiltroTiposManifestacionViolencia fc = new FiltroTiposManifestacionViolencia();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"tmaNombre"});
            List<SgTipoManifestacion> tiposManifestaciones = restCatalogo.buscarTipoManifestacion(fc);
            comboTipoManifestacion = new SofisComboG(new ArrayList(tiposManifestaciones), "tmaNombre");
            comboTipoManifestacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarValidaciones() {
        try {
            manifestaciones = new ArrayList<>();
            if (entidadEnEdicion != null) {
                FiltroManifestacionViolencia filtro = new FiltroManifestacionViolencia();
                filtro.setMviEstudiante(entidadEnEdicion.getEstPk());
                manifestaciones = restClientEstVal.buscar(filtro);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void actualizar(SgEstudiante est) {
        try {
            if (est != null) {
                entidadEnEdicion = est;
                this.cargarValidaciones();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarManifestacion() {
        try {
            selectedManifestacion.setMviTipoManifestacion(comboTipoManifestacion.getSelectedT());
            selectedManifestacion.setMviEstudiante(entidadEnEdicion);
            selectedManifestacion = restClientEstVal.guardar(selectedManifestacion);
            cargarValidaciones();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('manifestacionDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_VIO, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<Integer> getIndices() {
        List r = new ArrayList<>();
        r.add(1);
        r.add(2);
        return r;
    }

    public void editarManifestacion(SgManifestacionViolencia manifestacion) {
        if (manifestacion != null) {
            selectedManifestacion = manifestacion;
            comboTipoManifestacion.setSelectedT(selectedManifestacion.getMviTipoManifestacion());
            editManifestacion = Boolean.TRUE;
        }
    }

    public void verManifestacion(SgManifestacionViolencia manifestacion) {
        if (manifestacion != null) {
            selectedManifestacion = manifestacion;
            comboTipoManifestacion.setSelectedT(selectedManifestacion.getMviTipoManifestacion());
            editManifestacion = Boolean.FALSE;
        }
    }

    public void agregarManifestacion() {
        if (manifestaciones == null) {
            manifestaciones = new ArrayList<>();
        }
        editManifestacion = Boolean.TRUE;
        selectedManifestacion = new SgManifestacionViolencia();
        comboTipoManifestacion.setSelected(-1);
    }

    public void eliminarManifestacion(SgManifestacionViolencia manifestacion) {
        if (manifestacion != null) {
            selectedManifestacion = manifestacion;
            try {
                if (selectedManifestacion.getMviPk() != null && selectedManifestacion.getMviPk() != 0) {
                    restClientEstVal.eliminar(selectedManifestacion.getMviPk());
                    cargarValidaciones();
                }
            } catch (BusinessException be) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }
    }

    public List<SgManifestacionViolencia> getManifestaciones() {
        return manifestaciones;
    }

    public void setManifestaciones(List<SgManifestacionViolencia> manifestaciones) {
        this.manifestaciones = manifestaciones;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgTipoManifestacion> getComboTipoManifestacion() {
        return comboTipoManifestacion;
    }

    public void setComboTipoManifestacion(SofisComboG<SgTipoManifestacion> comboTipoManifestacion) {
        this.comboTipoManifestacion = comboTipoManifestacion;
    }

    public SgManifestacionViolencia getSelectedManifestacion() {
        return selectedManifestacion;
    }

    public void setSelectedManifestacion(SgManifestacionViolencia selectedManifestacion) {
        this.selectedManifestacion = selectedManifestacion;
    }

    public Boolean getEditManifestacion() {
        return editManifestacion;
    }

    public void setEditManifestacion(Boolean editManifestacion) {
        this.editManifestacion = editManifestacion;
    }

    

}

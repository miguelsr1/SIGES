/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import sv.gob.mined.siges.web.dto.SgEstudianteValoracion;
import sv.gob.mined.siges.web.enumerados.TipoValoracion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudianteValoracion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstudianteValoracionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudianteValoracionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudianteValoracionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long estId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private EstudianteValoracionRestClient restClientEstVal;

    private SgEstudiante entidadEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<TipoValoracion> comboTipoValoracion;

    private List<SgEstudianteValoracion> valoraciones;
    private SgEstudianteValoracion selectedValoracion = new SgEstudianteValoracion();
    private Boolean editValoracion;

    public EstudianteValoracionBean() {
    }

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
            List<TipoValoracion> tiposValoraciones = new ArrayList(Arrays.asList(TipoValoracion.values()));
            comboTipoValoracion = new SofisComboG(tiposValoraciones, "text");
            comboTipoValoracion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarValidaciones() {
        try {
            valoraciones = new ArrayList<>();
            if (entidadEnEdicion != null) {
                FiltroEstudianteValoracion filtro = new FiltroEstudianteValoracion();
                filtro.setEsvEstudiante(entidadEnEdicion.getEstPk());
                valoraciones = restClientEstVal.buscar(filtro);
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
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarValoracion() {
        try {
            selectedValoracion.setEsvTipoValoracion(comboTipoValoracion.getSelectedT());
            selectedValoracion.setEsvEstudiante(entidadEnEdicion);
            selectedValoracion = restClientEstVal.guardar(selectedValoracion);
            PrimeFaces.current().executeScript("PF('valoracionDialog').hide()");
            cargarValidaciones();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_VALORACION, FacesMessage.SEVERITY_ERROR, be.getErrores());
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

    public void editarValoracion(SgEstudianteValoracion valoracion) {
        if (valoracion != null) {
            selectedValoracion = valoracion;
            comboTipoValoracion.setSelectedT(selectedValoracion.getEsvTipoValoracion());
            editValoracion = Boolean.TRUE;
        }
    }

    public void verValoracion(SgEstudianteValoracion valoracion) {
        if (valoracion != null) {
            selectedValoracion = valoracion;
            comboTipoValoracion.setSelectedT(selectedValoracion.getEsvTipoValoracion());
            editValoracion = Boolean.FALSE;
        }
    }

    public void agregarValoracion() {
        if (valoraciones == null) {
            valoraciones = new ArrayList<>();
        }
        editValoracion = Boolean.TRUE;
        selectedValoracion = new SgEstudianteValoracion();
        comboTipoValoracion.setSelected(null);
    }

    public void eliminarValoracion(SgEstudianteValoracion valoracion) {
        if (valoracion != null) {
            selectedValoracion = valoracion;
            try {
                if (selectedValoracion.getEsvPk() != null && selectedValoracion.getEsvPk() != 0) {
                    restClientEstVal.eliminar(selectedValoracion.getEsvPk());
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

    public List<SgEstudianteValoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<SgEstudianteValoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<TipoValoracion> getComboTipoValoracion() {
        return comboTipoValoracion;
    }

    public void setComboTipoValoracion(SofisComboG<TipoValoracion> comboTipoValoracion) {
        this.comboTipoValoracion = comboTipoValoracion;
    }

    public SgEstudianteValoracion getSelectedValoracion() {
        return selectedValoracion;
    }

    public void setSelectedValoracion(SgEstudianteValoracion selectedValoracion) {
        this.selectedValoracion = selectedValoracion;
    }

    public Boolean getEditValoracion() {
        return editValoracion;
    }

    public void setEditValoracion(Boolean editValoracion) {
        this.editValoracion = editValoracion;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgOcupacion;
import sv.gob.mined.siges.web.dto.catalogo.SgProfesion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class OtrosDatosAllegadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OtrosDatosAllegadoBean.class.getName());

    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private SessionBean sessionBean;

    private SgAllegado entidadEnEdicion;
    private SofisComboG<SgProfesion> comboProfesion;
    private SofisComboG<SgEscolaridad> comboEscolaridad;
    private SofisComboG<SgOcupacion> comboOcupacion;
    private Boolean soloLectura = Boolean.FALSE;

    public OtrosDatosAllegadoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            soloLectura = request.getAttribute("soloLecturaOtrosDatosAllegado") != null ? (Boolean) request.getAttribute("soloLecturaOtrosDatosAllegado") : soloLectura;
            if (!soloLectura) {
                cargarCombos();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarCombos() {

        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"proNombre"});
            fc.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgProfesion> profesiones = restCatalogo.buscarProfesion(fc);
            comboProfesion = new SofisComboG(profesiones, "proNombre");
            comboProfesion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"escNombre"});
            fc.setIncluirCampos(new String[]{"escNombre", "escVersion"});
            List<SgEscolaridad> escolaridades = restCatalogo.buscarEscolaridad(fc);
            comboEscolaridad = new SofisComboG(escolaridades, "escNombre");
            comboEscolaridad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"ocuNombre"});
            fc.setIncluirCampos(new String[]{"ocuNombre", "ocuVersion"});
            comboOcupacion = new SofisComboG(restCatalogo.buscarOcupacion(fc), "ocuNombre");
            comboOcupacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiarCombos(){
        comboProfesion.setSelected(0);
        comboEscolaridad.setSelected(0);
        comboOcupacion.setSelected(0);
    }

    public void actualizar(SgAllegado all) {
        try {
           
            if (all != null) {
                entidadEnEdicion = all;
                if (!soloLectura) {
                    limpiarCombos();
                    comboProfesion.setSelectedT(entidadEnEdicion.getAllPersona().getPerProfesion());
                    comboEscolaridad.setSelectedT(entidadEnEdicion.getAllPersona().getPerEscolaridad());
                    comboOcupacion.setSelectedT(entidadEnEdicion.getAllPersona().getPerOcupacion());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void comboProfesionSelected() {
        entidadEnEdicion.getAllPersona().setPerProfesion(comboProfesion.getSelectedT());
    }

    public void comboEscolaridadSelected() {
        entidadEnEdicion.getAllPersona().setPerEscolaridad(comboEscolaridad.getSelectedT());
    }

    public void comboOcupacionSelected() {
        entidadEnEdicion.getAllPersona().setPerOcupacion(comboOcupacion.getSelectedT());
    }

    public SgAllegado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAllegado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgProfesion> getComboProfesion() {
        return comboProfesion;
    }

    public void setComboProfesion(SofisComboG<SgProfesion> comboProfesion) {
        this.comboProfesion = comboProfesion;
    }

    public SofisComboG<SgEscolaridad> getComboEscolaridad() {
        return comboEscolaridad;
    }

    public void setComboEscolaridad(SofisComboG<SgEscolaridad> comboEscolaridad) {
        this.comboEscolaridad = comboEscolaridad;
    }

    public SofisComboG<SgOcupacion> getComboOcupacion() {
        return comboOcupacion;
    }

    public void setComboOcupacion(SofisComboG<SgOcupacion> comboOcupacion) {
        this.comboOcupacion = comboOcupacion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}

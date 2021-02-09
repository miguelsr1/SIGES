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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoSangre;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class OtrosDatosPersonalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OtrosDatosPersonalBean.class.getName());

    @Inject
    private CatalogosRestClient restCatalogo;
    
    @Inject
    private SessionBean sessionBean;

    private SgPersonalSedeEducativa entidadEnEdicion;
    private SofisComboG<SgTipoSangre> comboTipoSangre;
    private Boolean soloLectura = Boolean.FALSE;

    public OtrosDatosPersonalBean() {
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

            fc.setOrderBy(new String[]{"tsaNombre"});
            fc.setIncluirCampos(new String[]{"tsaNombre", "tsaVersion"});
            List<SgTipoSangre> tipoSangre = restCatalogo.buscarTipoSangre(fc);
            comboTipoSangre = new SofisComboG(tipoSangre, "tsaNombre");
            comboTipoSangre.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiarCombos(){
        comboTipoSangre.setSelected(0);
    }

    public void actualizar(SgPersonalSedeEducativa all) {
        try {
            if (all != null) {
                entidadEnEdicion = all;
                if (!soloLectura) {
                    limpiarCombos();
                    comboTipoSangre.setSelectedT(entidadEnEdicion.getPsePersona().getPerTipoSangre());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void comboTipoSangreSelected() {
        entidadEnEdicion.getPsePersona().setPerTipoSangre(comboTipoSangre.getSelectedT());
    }


    public SgPersonalSedeEducativa getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPersonalSedeEducativa entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgTipoSangre> getComboTipoSangre() {
        return comboTipoSangre;
    }

    public void setComboTipoSangre(SofisComboG<SgTipoSangre> comboTipoSangre) {
        this.comboTipoSangre = comboTipoSangre;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

 
}

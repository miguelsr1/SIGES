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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCarrera;
import sv.gob.mined.siges.web.dto.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ConfiguracionFirmaElectronicaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ConfiguracionFirmaElectronicaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfiguracionFirmaElectronicaBean.class.getName());

    @Inject
    private ConfiguracionFirmaElectronicaRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgCarrera entidadEnEdicion = new SgCarrera();
    
    private List<SgConfiguracionFirmaElectronica> configs;

    public ConfiguracionFirmaElectronicaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            configs = restClient.buscar(new FiltroCodiguera());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCarrera();
    }

    public void actualizar(SgCarrera var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCarrera) SerializationUtils.clone(var);
    }

    public void guardar(SgConfiguracionFirmaElectronica c) {
        try {
            restClient.guardar(c);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public ConfiguracionFirmaElectronicaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ConfiguracionFirmaElectronicaRestClient restClient) {
        this.restClient = restClient;
    }

    public List<SgConfiguracionFirmaElectronica> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SgConfiguracionFirmaElectronica> configs) {
        this.configs = configs;
    }



    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCarrera getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCarrera entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }


}

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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgInfTipoDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyInfTipoDocumentoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.InfTipoDocumentoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InfTipoDocumentoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InfTipoDocumentoBean.class.getName());

    @Inject
    private InfTipoDocumentoRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgInfTipoDocumento entidadEnEdicion = new SgInfTipoDocumento();
    private List<SgInfTipoDocumento> historialInfTipoDocumento = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyInfTipoDocumentoDataModel infTipoDocumentoLazyModel;

    public InfTipoDocumentoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            infTipoDocumentoLazyModel = new LazyInfTipoDocumentoDataModel(restClient, filtro, totalResultados, paginado);
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
        entidadEnEdicion = new SgInfTipoDocumento();
        entidadEnEdicion.setTidHabilitado(Boolean.TRUE);
    }

    public void actualizar(SgInfTipoDocumento var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgInfTipoDocumento) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTidPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
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
            historialInfTipoDocumento = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgInfTipoDocumento getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInfTipoDocumento entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgInfTipoDocumento> getHistorialInfTipoDocumento() {
        return historialInfTipoDocumento;
    }

    public void setHistorialInfTipoDocumento(List<SgInfTipoDocumento> historialInfTipoDocumento) {
        this.historialInfTipoDocumento = historialInfTipoDocumento;
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

    public LazyInfTipoDocumentoDataModel getInfTipoDocumentoLazyModel() {
        return infTipoDocumentoLazyModel;
    }

    public void setInfTipoDocumentoLazyModel(LazyInfTipoDocumentoDataModel infTipoDocumentoLazyModel) {
        this.infTipoDocumentoLazyModel = infTipoDocumentoLazyModel;
    }

}

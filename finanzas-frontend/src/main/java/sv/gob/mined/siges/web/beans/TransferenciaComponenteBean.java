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
import sv.gob.mined.siges.web.dto.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaComponenteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.TransferenciaComponenteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean correspondiente a la gestin de transferencias por componente.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TransferenciaComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TransferenciaComponenteBean.class.getName());

    @Inject
    private TransferenciaComponenteRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SsTransferenciaComponente entidadEnEdicion = new SsTransferenciaComponente();
    private List<SsTransferenciaComponente> historialTransferencia = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTransferenciaComponenteDataModel transferenciaLazyModel;

    /**
     * Constructor de la clase.
     */
    public TransferenciaComponenteBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Transferencia
     * componente.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de Transferencia componente según los filtros
     * utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            transferenciaLazyModel = new LazyTransferenciaComponenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {

    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Transferencia
     * componente .
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SsTransferenciaComponente();
    }

    /**
     * Crea o actualiza un registro de Transferencia componente .
     *
     * @param SgFactura : var.
     */
    public void actualizar(SsTransferenciaComponente var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SsTransferenciaComponente) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de Transferencia componente .
     */
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

    /**
     * Elimina un registro de Transferencia componente .
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTcId());
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

    /**
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialTransferencia = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SsTransferenciaComponente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SsTransferenciaComponente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SsTransferenciaComponente> getHistorialTransferencia() {
        return historialTransferencia;
    }

    public void setHistorialTransferencia(List<SsTransferenciaComponente> historialTransferencia) {
        this.historialTransferencia = historialTransferencia;
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

    public LazyTransferenciaComponenteDataModel getTransferenciaLazyModel() {
        return transferenciaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTransferenciaComponenteDataModel transferenciaLazyModel) {
        this.transferenciaLazyModel = transferenciaLazyModel;
    }

    // </editor-fold>
}

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
import sv.gob.mined.siges.web.dto.SgDesembolsoCed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDesembolsoCed;
import sv.gob.mined.siges.web.lazymodels.LazyDesembolsoCedDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DesembolsoCedRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de Desembolsos.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DesembolsoCedBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DesembolsoCedBean.class.getName());

    @Inject
    private DesembolsoCedRestClient restClient;

    private FiltroDesembolsoCed filtro = new FiltroDesembolsoCed();
    private SgDesembolsoCed entidadEnEdicion = new SgDesembolsoCed();
    private List<SgDesembolsoCed> historialDesembolsoCed = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDesembolsoCedDataModel desembolsoCedLazyModel;

    /**
     * Constructor de la clase.
     */
    public DesembolsoCedBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Desembolsos.
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
     * Busca los registros de desembolsos según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{
                "dcePk", "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedPk",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedCodigo", "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedNombre",
                "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedVersion", "dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedTipo",
                "dceEstado", "dceMonto", "dceVersion"
            });
            totalResultados = restClient.buscarTotal(filtro);
            desembolsoCedLazyModel = new LazyDesembolsoCedDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroDesembolsoCed();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de desembolso.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgDesembolsoCed();
    }

    /**
     * Crea o actualiza un registro de Desembolso.
     *
     * @param SgDesembolsoCed var.
     */
    public void actualizar(SgDesembolsoCed var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgDesembolsoCed) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de Desembolso.
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
     * Elimina un registro de Desembolso.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDcePk());
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
            historialDesembolsoCed = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroDesembolsoCed getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDesembolsoCed filtro) {
        this.filtro = filtro;
    }

    public SgDesembolsoCed getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDesembolsoCed entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgDesembolsoCed> getHistorialDesembolsoCed() {
        return historialDesembolsoCed;
    }

    public void setHistorialDesembolsoCed(List<SgDesembolsoCed> historialDesembolsoCed) {
        this.historialDesembolsoCed = historialDesembolsoCed;
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

    public LazyDesembolsoCedDataModel getDesembolsoCedLazyModel() {
        return desembolsoCedLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDesembolsoCedDataModel desembolsoCedLazyModel) {
        this.desembolsoCedLazyModel = desembolsoCedLazyModel;
    }
    // </editor-fold>
}

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
import sv.gob.mined.siges.web.dto.siap2.SsTransferencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferencia;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.TransferenciaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean correspondiente a la gestin de transferencias.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TransferenciasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TransferenciasBean.class.getName());

    @Inject
    private TransferenciaRestClient restClient;

    private FiltroTransferencia filtro = new FiltroTransferencia();
    private SsTransferencia entidadEnEdicion = new SsTransferencia();
    private List<SsTransferencia> historialTransferencia = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTransferenciaDataModel transferenciaLazyModel;

    /**
     * Constructor de la clase.
     */
    public TransferenciasBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Transferencia.
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
     * Busca los registros de Transferencia según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"traId",
                "traAnioFiscal.aniAnio",
                "traAnioFiscal.aniVersion",
                "traSubcomponente.gesCategoriaComponente.cpeId",
                "traSubcomponente.gesCategoriaComponente.cpeNombre",
                "traSubcomponente.gesCategoriaComponente.cpeVersion",
                "traSubcomponente.gesId",
                "traSubcomponente.gesNombre",
                "traSubcomponente.gesVersion",
                "traLineaPresupuestaria.cuCuentaPadre.cuId",
                "traLineaPresupuestaria.cuCuentaPadre.cuNombre",
                "traLineaPresupuestaria.cuCuentaPadre.cuVersion",
                "traLineaPresupuestaria.cuId",
                "traLineaPresupuestaria.cuNombre",
                "traLineaPresupuestaria.cuVersion",
                "traVersion"});
            totalResultados = restClient.buscarTotal(filtro);
            transferenciaLazyModel = new LazyTransferenciaDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroTransferencia();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Transferencia.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SsTransferencia();
    }

    /**
     * Crea o actualiza un registro de Transferencia.
     *
     * @param SsTransferencia : var.
     */
    public void actualizar(SsTransferencia var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SsTransferencia) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de movimientos de Transferencia.
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
    public FiltroTransferencia getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTransferencia filtro) {
        this.filtro = filtro;
    }

    public SsTransferencia getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SsTransferencia entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SsTransferencia> getHistorialTransferencia() {
        return historialTransferencia;
    }

    public void setHistorialTransferencia(List<SsTransferencia> historialTransferencia) {
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

    public LazyTransferenciaDataModel getTransferenciaLazyModel() {
        return transferenciaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTransferenciaDataModel transferenciaLazyModel) {
        this.transferenciaLazyModel = transferenciaLazyModel;
    }

    // </editor-fold>
}

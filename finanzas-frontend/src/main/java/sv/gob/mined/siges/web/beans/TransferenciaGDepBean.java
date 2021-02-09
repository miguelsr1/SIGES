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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgTransferenciaGDep;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaGDep;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaGDepDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.TransferenciaGDepRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TransferenciaGDepBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TransferenciaGDepBean.class.getName());

    @Inject
    private TransferenciaGDepRestClient restClient;

    private FiltroTransferenciaGDep filtro = new FiltroTransferenciaGDep();
    private SgTransferenciaGDep entidadEnEdicion = new SgTransferenciaGDep();
    private List<SgTransferenciaGDep> historialTransferenciaGDep = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTransferenciaGDepDataModel transferenciaGDepLazyModel;

    /**
     * Constructor de la clase.
     */
    public TransferenciaGDepBean() {
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
            filtro.setOrderBy(new String[]{"tacPk"});
            filtro.setIncluirCampos(new String[]{"tgdPk",
                "tgdDepartamentoFk.depPk",
                "tgdDepartamentoFk.depCodigo",
                "tgdDepartamentoFk.depNombre",
                "tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeId",
                "tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeNombre",
                "tgdTransferenciaFk.traSubcomponente.gesId",
                "tgdTransferenciaFk.traSubcomponente.gesNombre",
                "tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuId",
                "tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuNombre",
                "tgdTransferenciaFk.traLineaPresupuestaria.cuId",
                "tgdTransferenciaFk.traLineaPresupuestaria.cuNombre",
                "tgdTransferenciaFk.traAnioFiscal.aniAnio",
                "tgdMonto", "tgdVersion"});
            filtro.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            transferenciaGDepLazyModel = new LazyTransferenciaGDepDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroTransferenciaGDep();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Transferencia.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTransferenciaGDep();
    }

    /**
     * Crea o actualiza un registro de Facturas.
     *
     * @param SgFactura : var.
     */
    public void actualizar(SgTransferenciaGDep var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTransferenciaGDep) SerializationUtils.clone(var);
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialTransferenciaGDep = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroTransferenciaGDep getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTransferenciaGDep filtro) {
        this.filtro = filtro;
    }

    public SgTransferenciaGDep getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTransferenciaGDep entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTransferenciaGDep> getHistorialTransferenciaGDep() {
        return historialTransferenciaGDep;
    }

    public void setHistorialTransferenciaGDep(List<SgTransferenciaGDep> historialTransferenciaGDep) {
        this.historialTransferenciaGDep = historialTransferenciaGDep;
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

    public LazyTransferenciaGDepDataModel getTransferenciaGDepLazyModel() {
        return transferenciaGDepLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTransferenciaGDepDataModel transferenciaGDepLazyModel) {
        this.transferenciaGDepLazyModel = transferenciaGDepLazyModel;
    }

    // </editor-fold>
}

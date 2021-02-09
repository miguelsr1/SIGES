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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.lazymodels.LazyPresupuestosEscolaresDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de movimientos presupuesto.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientosBean.class.getName());

    @Inject
    private MovimientosRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    @Param(name = "id")
    private Long MovimientoId;

    private SgMovimientos entidadEnEdicion = new SgMovimientos();
    private FiltroMovimientos filtro = new FiltroMovimientos();
    private Long totalResultados;
    private LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel;
    private Integer paginado = 10;
    private List<RevHistorico> historialMovimientos = new ArrayList();
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private Boolean soloLectura = Boolean.FALSE;
    private Integer selectedTab;

    public MovimientosBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            buscar();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            anioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            anioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea un nuevo objeto de movimientos
     */
    public void agregar() {

        entidadEnEdicion = new SgMovimientos();

    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
//            totalResultados = restClient.buscarTotal(filtro);
//            reglasPresupuestoEscolarDataModel = new LazyPresupuestosEscolaresDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un movimiento para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgMovimientos req) {
        try {
            if (req != null) {
                if (req.getMovPk() != null) {
                    entidadEnEdicion = restClient.obtenerPorId(req.getMovPk(), true);
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Obtiene el título de la página correspondiente al bean
     *
     * @return
     */
    public String getTituloPagina() {
        if (this.MovimientoId == null) {
            return Etiquetas.getValue("nuevoMovimiento");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verMovimiento");
        } else {
            return Etiquetas.getValue("edicionMovimiento");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialMovimientos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroMovimientos();
        buscar();
    }

    /**
     * Elimina un registro de movimientos.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMovPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos.
     */
    public void guardar() {
        try {
            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            //buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public void setEntidadEnEdicion(SgMovimientos entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgMovimientos getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroMovimientos getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMovimientos filtro) {
        this.filtro = filtro;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyPresupuestosEscolaresDataModel getReglasPresupuestoEscolarDataModel() {
        return reglasPresupuestoEscolarDataModel;
    }

    public void setReglasPresupuestoEscolarDataModel(LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel) {
        this.reglasPresupuestoEscolarDataModel = reglasPresupuestoEscolarDataModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialMovimientos;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialMovimientos) {
        this.historialMovimientos = historialMovimientos;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Long getMovimientoId() {
        return MovimientoId;
    }

    public void setMovimientoId(Long MovimientoId) {
        this.MovimientoId = MovimientoId;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    // </editor-fold>
}

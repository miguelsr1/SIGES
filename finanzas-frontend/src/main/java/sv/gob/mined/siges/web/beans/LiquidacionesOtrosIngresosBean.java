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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgLiquidacionOtroIngreso;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacionOtroIngreso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLiquidacionOtroIngresoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionOtroIngresoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de liquidaciones.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class LiquidacionesOtrosIngresosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LiquidacionesOtrosIngresosBean.class.getName());

    @Inject
    private LiquidacionOtroIngresoRestClient restClient;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    private Integer paginado = 10;
    private Long totalResultados;
    private SgSede sedeSeleccionadaFiltro;

    private FiltroLiquidacionOtroIngreso filtro = new FiltroLiquidacionOtroIngreso();
    private SgLiquidacionOtroIngreso entidadEnEdicion = new SgLiquidacionOtroIngreso();
    private List<SgLiquidacionOtroIngreso> historialLiquidacionOtroIngreso = new ArrayList();
    private LazyLiquidacionOtroIngresoDataModel liquidacionOtroIngresoLazyModel;

    private SofisComboG<EnumEstadoLiquidacion> comboEstado = new SofisComboG<>();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamento = new SofisComboG<>();

    /**
     * Constructor de la clase.
     */
    public LiquidacionesOtrosIngresosBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Liquidaciones.
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
     * Busca los registros de Liquidaciones según los filtros utilizados.
     */
    public void buscar() {
        try {

            if (comboAnioLectivo.getSelectedT() != null) {
                filtro.setAnioFiscal(comboAnioLectivo.getSelectedT().getAleAnio());
            }

            if (sedeSeleccionadaFiltro != null) {
                filtro.setLoiSedePk(sedeSeleccionadaFiltro.getSedPk());
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setEstado(comboEstado.getSelectedT());
            }
            
            if(comboDepartamento.getSelectedT()!=null){
                filtro.setDepartamentoPk(comboDepartamento.getSelectedT().getDepPk());
            }

            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"loiPk", "loiEstado", "loiVersion",
                "loiSedePk.sedPk", "loiSedePk.sedCodigo",
                "loiSedePk.sedNombre", "loiSedePk.sedTipo", "loiSedePk.sedVersion",
                "loiAnioPk.aleAnio", "loiAnioPk.alePk", "loiAnioPk.aleVersion", "loiUltModFecha"
            });
            totalResultados = restClient.buscarTotal(filtro);
            liquidacionOtroIngresoLazyModel = new LazyLiquidacionOtroIngresoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {
            comboEstado = new SofisComboG<>(Arrays.asList(EnumEstadoLiquidacion.values()), "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAnioLectivo = new SofisComboG<>(cargarAnios(), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depPk","depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de años electivos
     *
     * @return List<SgAnioElectivo>
     */
    public List<SgAnioLectivo> cargarAnios() {
        try {
            FiltroAnioLectivo filtroAnioLectivo = new FiltroAnioLectivo();
            filtro.setIncluirCampos(new String[]{"alePk", "aleAnio", "aleVersion"});
            filtroAnioLectivo.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            filtroAnioLectivo.setOrderBy(new String[]{"aleAnio"});
            filtroAnioLectivo.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAniosLectivos = anioLectivoRestClient.buscar(filtroAnioLectivo);
            return listAniosLectivos;
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Carga el combo de Sede para creación y edición de registros.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
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
        filtro = new FiltroLiquidacionOtroIngreso();
        sedeSeleccionadaFiltro = null;
        comboAnioLectivo.setSelected(-1);
        comboEstado.setSelected(-1);
        comboDepartamento.setSelected(-1);
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidaciones.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgLiquidacionOtroIngreso();
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     *
     * @param SgLiquidacion : var.
     */
    public void actualizar(SgLiquidacionOtroIngreso var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgLiquidacionOtroIngreso) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
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
     * Elimina un registro de Liquidaciones.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getLoiPk());
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
            historialLiquidacionOtroIngreso = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroLiquidacionOtroIngreso getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroLiquidacionOtroIngreso filtro) {
        this.filtro = filtro;
    }

    public SgLiquidacionOtroIngreso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgLiquidacionOtroIngreso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgLiquidacionOtroIngreso> getHistorialLiquidacionOtroIngreso() {
        return historialLiquidacionOtroIngreso;
    }

    public void setHistorialLiquidacionOtroIngreso(List<SgLiquidacionOtroIngreso> historialLiquidacionOtroIngreso) {
        this.historialLiquidacionOtroIngreso = historialLiquidacionOtroIngreso;
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

    public LazyLiquidacionOtroIngresoDataModel getLiquidacionOtroIngresoLazyModel() {
        return liquidacionOtroIngresoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyLiquidacionOtroIngresoDataModel liquidacionOtroIngresoLazyModel) {
        this.liquidacionOtroIngresoLazyModel = liquidacionOtroIngresoLazyModel;
    }

    public SofisComboG<EnumEstadoLiquidacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoLiquidacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }
    
    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }
    
    // </editor-fold>


    
    
}

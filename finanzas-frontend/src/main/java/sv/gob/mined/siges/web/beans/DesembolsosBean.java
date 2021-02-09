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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDesembolso;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDesembolso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.lazymodels.LazyDesembolsoDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyRequerimientoFondoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.DesembolsoRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
/**
 * Bean para la gestión de desembolsos.
 *
 * @author Sofis Solutions
 */
public class DesembolsosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DesembolsosBean.class.getName());

    @Inject
    private DesembolsoRestClient restClient;

    @Inject
    private RequerimientoFondoRestClient restReqFondo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    @Param(name = "id")
    private Long desembolsoId;

    private SgDesembolso entidadEnEdicion = new SgDesembolso();
    private FiltroDesembolso filtro = new FiltroDesembolso();
    private FiltroRequerimientosFondo filtroReqFondo = new FiltroRequerimientosFondo();

    private Long totalResultados;
    private Long totalResultadosReq;
    private LazyDesembolsoDataModel desembolsoDataModel;
    private LazyRequerimientoFondoDataModel requerimientoFondoDataModel;
    private Integer paginado = 10;
    private List<RevHistorico> historialMovimientos = new ArrayList();
    private SofisComboG<EnumDesembolsoEstado> estadoDesembolsoCombo = new SofisComboG();
    private List<SgDesembolso> listaDesembolsosSeleccionados = new ArrayList();

    private Boolean soloLectura = Boolean.FALSE;
    private Integer selectedTab;

    public DesembolsosBean() {
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
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {
            estadoDesembolsoCombo = new SofisComboG(Arrays.asList(EnumDesembolsoEstado.values()), "text");
            estadoDesembolsoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea un nuevo objeto de desembolso
     */
    public void agregar() {

        entidadEnEdicion = new SgDesembolso();
        obtenerReqsFondo();
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void obtenerReqsFondo() {
        try {
            filtroReqFondo.setFirst(new Long(0));

            filtroReqFondo.setIncluirCampos(new String[]{"strPk",
                "strTransferenciaGDepFk.tgdPk",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traAnioFiscal.aniAnio",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traAnioFiscal.aniVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traLineaPresupuestaria.cuCuentaPadre.cuVersion",
                "strTransferenciaGDepFk.tgdVersion",
                "strVersion"
            });
            filtroReqFondo.setOrderBy(new String[]{"strPk"});
            filtroReqFondo.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            requerimientoFondoDataModel = new LazyRequerimientoFondoDataModel(restReqFondo, filtroReqFondo, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"dsbPk", "dsbEstado", "dsbMonto", "dsbUltModFecha", "dsbVersion"});
            filtro.setOrderBy(new String[]{"dsbPk"});
            filtro.setAscending(new boolean[]{false});
            if (estadoDesembolsoCombo.getSelectedT() != null) {
                filtro.setEstado(estadoDesembolsoCombo.getSelectedT());
            }

            totalResultados = restClient.buscarTotal(filtro);
            desembolsoDataModel = new LazyDesembolsoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un desembolso para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgDesembolso req) {
        try {
            entidadEnEdicion = restClient.obtenerPorId(req.getDsbPk());

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
        if (this.desembolsoId == null) {
            return Etiquetas.getValue("nuevoDesembolso");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verDesembolso");
        } else {
            return Etiquetas.getValue("edicionDesembolso");
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
        filtro = new FiltroDesembolso();
        estadoDesembolsoCombo.setSelected(-1);
        buscar();
    }

    /**
     * Elimina un registro de desembolso.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDsbPk());
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
     * Crea o actualiza un registro de desembolso.
     */
    public void guardar() {
        try {

            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public void setEntidadEnEdicion(SgDesembolso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgDesembolso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroDesembolso getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDesembolso filtro) {
        this.filtro = filtro;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    public LazyDesembolsoDataModel getDesembolsoDataModel() {
        return desembolsoDataModel;
    }

    public void setDesembolsoDataModel(LazyDesembolsoDataModel desembolsoDataModel) {
        this.desembolsoDataModel = desembolsoDataModel;
    }

    public List<SgDesembolso> getListaDesembolsosSeleccionados() {
        return listaDesembolsosSeleccionados;
    }

    public void setListaDesembolsosSeleccionados(List<SgDesembolso> listaDesembolsosSeleccionados) {
        this.listaDesembolsosSeleccionados = listaDesembolsosSeleccionados;
    }

    public Long getTotalResultadosReq() {
        return totalResultadosReq;
    }

    public void setTotalResultadosReq(Long totalResultadosReq) {
        this.totalResultadosReq = totalResultadosReq;
    }

    public LazyRequerimientoFondoDataModel getRequerimientoFondoDataModel() {
        return requerimientoFondoDataModel;
    }

    public void setRequerimientoFondoDataModel(LazyRequerimientoFondoDataModel requerimientoFondoDataModel) {
        this.requerimientoFondoDataModel = requerimientoFondoDataModel;
    }

    public SofisComboG<EnumDesembolsoEstado> getEstadoDesembolsoCombo() {
        return estadoDesembolsoCombo;
    }

    public void setEstadoDesembolsoCombo(SofisComboG<EnumDesembolsoEstado> estadoDesembolsoCombo) {
        this.estadoDesembolsoCombo = estadoDesembolsoCombo;
    }

    // </editor-fold>
}

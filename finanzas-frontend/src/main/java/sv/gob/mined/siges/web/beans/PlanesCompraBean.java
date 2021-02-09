/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumPlanCompraEstado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyPlanesCompraDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.PlanesCompraRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de planes de compra.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PlanesCompraBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlanesCompraBean.class.getName());

    @Inject
    private PlanesCompraRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;
    private LazyPlanesCompraDataModel planesCompraLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroEncabezadoPlanCompra filtro = new FiltroEncabezadoPlanCompra();
    private SgEncabezadoPlanCompras entidadEnEdicion = new SgEncabezadoPlanCompras();
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sede;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private SofisComboG<EnumPlanCompraEstado> comboEstado;
    private Locale locale;
    @Inject
    @Param(name = "id")
    private Long planId;

    /**
     * Constructor de la clase.
     */
    public PlanesCompraBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Proveedores.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (planId != null && planId > 0) {
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroEncabezadoPlanCompra();
        sedeSeleccionadaFiltro = null;
        anioFiscalCombo.setSelected(-1);
        comboEstado.setSelected(-1);
        buscar();
    }

    /**
     * Busca los registros de Proveedores según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"plaPk",
                "plaPresupuestoFk.pesPk",
                "plaPresupuestoFk.pesSedeFk.sedPk",
                "plaPresupuestoFk.pesSedeFk.sedCodigo",
                "plaPresupuestoFk.pesSedeFk.sedNombre",
                "plaPresupuestoFk.pesSedeFk.sedTipo",
                "plaPresupuestoFk.pesSedeFk.sedVersion",
                "plaPresupuestoFk.pesAnioFiscalFk.aniPk",
                "plaPresupuestoFk.pesAnioFiscalFk.aniAnio",
                "plaPresupuestoFk.pesAnioFiscalFk.aniVersion",
                "plaPresupuestoFk.pesVersion",
                "plaComentario",
                "plaEstado",
                "plaUltModFecha",
                "plaUltModUsuario",
                "plaVersion"

            });
            if (sedeSeleccionadaFiltro != null) {
                filtro.setSedeFk(sede.getSedPk());
            }
            if (anioFiscalCombo.getSelectedT() != null) {
                filtro.setAnioFiscalFk(anioFiscalCombo.getSelectedT().getAniAnio());
            }
            if (comboEstado.getSelectedT() != null) {
                filtro.setPlaEstado(comboEstado.getSelectedT());
            }
            totalResultados = restClient.buscarTotal(filtro);
            planesCompraLazyModel = new LazyPlanesCompraDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el oncomplete de sedes en presupuestos escolares.
     */
    public List<SgSede> completeSedeFiltro(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
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
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setIncluirCampos(new String[]{"aniPk", "aniAnio", "aniVersion"});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumPlanCompraEstado> listEstados = new ArrayList(Arrays.asList(EnumPlanCompraEstado.values()));
            comboEstado = new SofisComboG(listEstados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Setea el valor de la Sede Selecccionada devuelve el objeto Sede.
     */
    public void setearSede() {
        JSFUtils.limpiarMensajesError();
        sede = sedeSeleccionadaFiltro;
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public LazyPlanesCompraDataModel getPlanesCompraLazyModel() {
        return planesCompraLazyModel;
    }

    public void setPlanesCompraLazyModel(LazyPlanesCompraDataModel planesCompraLazyModel) {
        this.planesCompraLazyModel = planesCompraLazyModel;
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

    public FiltroEncabezadoPlanCompra getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEncabezadoPlanCompra filtro) {
        this.filtro = filtro;
    }

    public SgEncabezadoPlanCompras getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEncabezadoPlanCompras entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    public SofisComboG<EnumPlanCompraEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumPlanCompraEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }
    // </editor-fold>
}

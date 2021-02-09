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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCargaExterna;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargaExterna;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.lazymodels.LazyCargaExternaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CargaExternaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.IndicadorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CargasExternasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargasExternasBean.class.getName());

    @Inject
    private CargaExternaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private IndicadorRestClient indicadoresClient;

    private FiltroCargaExterna filtro = new FiltroCargaExterna();
    private SgCargaExterna entidadEnEdicion = new SgCargaExterna();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCargaExternaDataModel cargasExternasLazyModel;
    private List<RevHistorico> historialCargaExterna = new ArrayList();

    private SofisComboG<SgEstIndicador> comboIndicadores;
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtr;

    public CargasExternasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGAS_EXTERNAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {

            filtro.setNombrePk(this.comboNombresExtr.getSelectedT() != null ? this.comboNombresExtr.getSelectedT().getNexPk() : null);
            filtro.setIndicadorPk(this.comboIndicadores.getSelectedT() != null ? this.comboIndicadores.getSelectedT().getIndPk() : null);

            totalResultados = restClient.buscarTotal(filtro);
            cargasExternasLazyModel = new LazyCargaExternaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            FiltroIndicadores fi = new FiltroIndicadores();
            fi.setHabilitado(Boolean.TRUE);
            fi.setEsExterno(Boolean.TRUE);
            fi.setAscending(new boolean[]{true});
            fi.setIncluirCampos(new String[]{"indNombre"});
            //fi.setCategoriaPk(this.comboCategorias.getSelectedT().getCinPk());
            fi.setOrderBy(new String[]{"indNombre"});
            comboIndicadores = new SofisComboG<>(indicadoresClient.buscar(fi), "indNombre");
            comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});
            comboNombresExtr = new SofisComboG<>(catalogosClient.buscarNombresExtraccion(fc), "nexNombre");
            comboNombresExtr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialCargaExterna = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgCargaExterna ce) {
        this.entidadEnEdicion = ce;
    }

    public void limpiar() {
        filtro = new FiltroCargaExterna();
        comboIndicadores.setSelected(-1);
        comboNombresExtr.setSelected(-1);
        buscar();
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCarPk());
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

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtr() {
        return comboNombresExtr;
    }

    public void setComboNombresExtr(SofisComboG<SgEstNombreExtraccion> comboNombresExtr) {
        this.comboNombresExtr = comboNombresExtr;
    }

    public FiltroCargaExterna getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCargaExterna filtro) {
        this.filtro = filtro;
    }

    public SgCargaExterna getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCargaExterna entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public LazyCargaExternaDataModel getCargasExternasLazyModel() {
        return cargasExternasLazyModel;
    }

    public void setCargasExternasLazyModel(LazyCargaExternaDataModel cargasExternasLazyModel) {
        this.cargasExternasLazyModel = cargasExternasLazyModel;
    }

    public List<RevHistorico> getHistorialCargaExterna() {
        return historialCargaExterna;
    }

    public void setHistorialCargaExterna(List<RevHistorico> historialCargaExterna) {
        this.historialCargaExterna = historialCargaExterna;
    }

    public SofisComboG<SgEstIndicador> getComboIndicadores() {
        return comboIndicadores;
    }

    public void setComboIndicadores(SofisComboG<SgEstIndicador> comboIndicadores) {
        this.comboIndicadores = comboIndicadores;
    }

}

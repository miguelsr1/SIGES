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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgExtraccion;
import sv.gob.mined.siges.web.dto.catalogo.SgEstDatasets;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroExtraccion;
import sv.gob.mined.siges.web.lazymodels.LazyExtraccionesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ExtraccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ExtraccionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ExtraccionesBean.class.getName());

    @Inject
    private ExtraccionRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroExtraccion filtro = new FiltroExtraccion();
    private SgExtraccion entidadEnEdicion = new SgExtraccion();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyExtraccionesDataModel extraccionesLazyModel;
    private List<RevHistorico> historialExtraccion = new ArrayList();
    private Integer anio;

    private SofisComboG<SgEstDatasets> comboDatasets;
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtr;

    public ExtraccionesBean() {
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
            extraccionesLazyModel = new LazyExtraccionesDataModel(restClient, filtro, totalResultados, paginado);
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

            fc.setOrderBy(new String[]{"datNombre"});
            fc.setIncluirCampos(new String[]{"datNombre", "datVersion"});
            comboDatasets = new SofisComboG<>(catalogosClient.buscarDatasets(fc), "datNombre");
            comboDatasets.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});
            comboNombresExtr = new SofisComboG<>(catalogosClient.buscarNombresExtraccion(fc), "nexNombre");
            comboNombresExtr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void extraer() {
        try {
            restClient.procesarExtraccionesPendientes();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialExtraccion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroExtraccion();
        comboDatasets.setSelected(-1);
        comboNombresExtr.setSelected(-1);
        buscar();
    }

    public FiltroExtraccion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroExtraccion filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorialExtraccion() {
        return historialExtraccion;
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public ExtraccionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ExtraccionRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<SgEstDatasets> getComboDatasets() {
        return comboDatasets;
    }

    public void setComboDatasets(SofisComboG<SgEstDatasets> comboDatasets) {
        this.comboDatasets = comboDatasets;
    }

    public SofisComboG<SgEstNombreExtraccion> getComboNombresExtr() {
        return comboNombresExtr;
    }

    public void setComboNombresExtr(SofisComboG<SgEstNombreExtraccion> comboNombresExtr) {
        this.comboNombresExtr = comboNombresExtr;
    }

    public LazyExtraccionesDataModel getExtraccionesLazyModel() {
        return extraccionesLazyModel;
    }

    public void setExtraccionesLazyModel(LazyExtraccionesDataModel extraccionesLazyModel) {
        this.extraccionesLazyModel = extraccionesLazyModel;
    }

}

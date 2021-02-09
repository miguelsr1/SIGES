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
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEstArchivo;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.lazymodels.LazyIndicadorDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
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
public class IndicadoresBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(IndicadoresBean.class.getName());

    @Inject
    private IndicadorRestClient restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;
    
    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroIndicadores filtro = new FiltroIndicadores();
    private SgEstIndicador entidadEnEdicion = new SgEstIndicador();
    private List<RevHistorico> historialIndicador = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyIndicadorDataModel indicadorLazyModel;
    private SofisComboG<SgEstCategoriaIndicador> comboCategorias;

    public IndicadoresBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_INDICADORES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setCategoriaPk(this.comboCategorias.getSelectedT() != null ? this.comboCategorias.getSelectedT().getCinPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            indicadorLazyModel = new LazyIndicadorDataModel(restClient, filtro, totalResultados, paginado);
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

            fc.setOrderBy(new String[]{"cinNombre"});
            fc.setIncluirCampos(new String[]{"cinNombre", "cinVersion"});
            comboCategorias = new SofisComboG<>(catalogosClient.buscarCategoriaIndicador(fc), "cinNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroIndicadores();
        buscar();
    }

    public void actualizar(SgEstIndicador var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEstIndicador) SerializationUtils.clone(var);
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getIndPk());
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

    public void historial(Long id) {
        try {
            historialIndicador = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handlePartidaFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getIndFormula() == null) {
            entidadEnEdicion.setIndFormula(new SgEstArchivo());
        }
        handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getIndFormula());
    }

    public FiltroIndicadores getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroIndicadores filtro) {
        this.filtro = filtro;
    }

    public SgEstIndicador getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstIndicador entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialIndicador() {
        return historialIndicador;
    }

    public void setHistorialIndicador(List<RevHistorico> historialIndicador) {
        this.historialIndicador = historialIndicador;
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

    public LazyIndicadorDataModel getIndicadorLazyModel() {
        return indicadorLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyIndicadorDataModel indicadorLazyModel) {
        this.indicadorLazyModel = indicadorLazyModel;
    }

    public SofisComboG<SgEstCategoriaIndicador> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgEstCategoriaIndicador> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }
    
    

}

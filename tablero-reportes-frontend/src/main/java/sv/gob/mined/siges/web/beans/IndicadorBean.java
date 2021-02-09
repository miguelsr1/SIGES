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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstArchivo;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.lazymodels.LazyIndicadorDataModel;
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
public class IndicadorBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(IndicadorBean.class.getName());

    @Inject
    private IndicadorRestClient restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long indId;

    private FiltroIndicadores filtro = new FiltroIndicadores();
    private SgEstIndicador entidadEnEdicion = new SgEstIndicador();
    private List<SgEstIndicador> historialIndicador = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyIndicadorDataModel indicadorLazyModel;

    private SofisComboG<SgEstCategoriaIndicador> comboCategorias;

    public IndicadorBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (indId != null && indId > 0) {
                actualizar(restClient.obtenerPorId(indId));
            } else {
                agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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

    public List<EnumDesagregacion> completeDesagregacion(String query) {
        try {
            List<EnumDesagregacion> ambitos = new ArrayList<>(Arrays.asList(EnumDesagregacion.values()));
            if (StringUtils.isBlank(query)) {
                return ambitos.stream()
                        .filter(x -> !entidadEnEdicion.getIndDesagregaciones().contains(x))
                        .collect(Collectors.toList());
            } else {
                return ambitos.stream()
                        .filter(x -> !entidadEnEdicion.getIndDesagregaciones().contains(x))
                        .filter(x -> x.getText().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {

    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEstIndicador();
    }

    public void actualizar(SgEstIndicador var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = var;
        this.comboCategorias.setSelectedT(var.getIndCategoria());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setIndCategoria(this.comboCategorias.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String tituloPagina() {
        if (entidadEnEdicion.getIndPk() == null) {
            return "Agregar Indicador";
        } else {
            return "Editar Indicador";
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

    public List<SgEstIndicador> getHistorialIndicador() {
        return historialIndicador;
    }

    public void setHistorialIndicador(List<SgEstIndicador> historialIndicador) {
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

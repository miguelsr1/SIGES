/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCargaExterna;
import sv.gob.mined.siges.web.dto.SgEstArchivo;
import sv.gob.mined.siges.web.dto.SgEstIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgEstCategoriaIndicador;
import sv.gob.mined.siges.web.dto.catalogo.SgEstDatasets;
import sv.gob.mined.siges.web.dto.catalogo.SgEstNombreExtraccion;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIndicadores;
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
public class CargaExternaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargaExternaBean.class.getName());

    @Inject
    private CargaExternaRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private IndicadorRestClient indicadoresClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    @Param(name = "id")
    private Long carId;

    private SofisComboG<SgEstIndicador> comboIndicadores;
    private SofisComboG<SgEstCategoriaIndicador> comboCategorias;
    private SofisComboG<SgEstDatasets> comboDatasets;
    private SofisComboG<SgEstNombreExtraccion> comboNombresExtr;
    private SofisComboG<EnumDesagregacion> comboDesagregaciones;
    private SofisComboG<EnumTipoNumericoValorEstadistica> comboTipoNumerico;

    private SgCargaExterna entidadEnEdicion = new SgCargaExterna();

    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String nombreArchivo;

    public CargaExternaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (carId != null && carId > 0) {
                actualizar(restClient.obtenerPorId(carId));
            } else {
                agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGAS_EXTERNAS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarConfiguracion() {
        try {
            SgConfiguracion conf = catalogosClient.buscarConfiguracionPorCodigo(Constantes.TAMANIO_ARCHIVO_IMPORT);
            if (conf != null) {
                tamanioImportArchivo = conf.getConValor();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"datNombre"});
            fc.setIncluirCampos(new String[]{"datNombre", "datCodigo", "datVersion"});
            comboDatasets = new SofisComboG<>(catalogosClient.buscarDatasets(fc), "datNombre");
            comboDatasets.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"cinNombre"});
            fc.setIncluirCampos(new String[]{"cinNombre", "cinVersion"});
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"cinNombre"});
            comboCategorias = new SofisComboG<>(catalogosClient.buscarCategoriaIndicador(fc), "cinNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"nexNombre"});
            fc.setIncluirCampos(new String[]{"nexNombre", "nexVersion"});
            List<SgEstNombreExtraccion> nombres = catalogosClient.buscarNombresExtraccion(fc);
            comboNombresExtr = new SofisComboG<>(nombres, "nexNombre");
            comboNombresExtr.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDesagregaciones = new SofisComboG<>();
            comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboTipoNumerico = new SofisComboG<>(Arrays.asList(EnumTipoNumericoValorEstadistica.values()), "text");
            comboTipoNumerico.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboIndicadores = new SofisComboG<>();
            comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void selectCategoria() {
        try {
            comboIndicadores = new SofisComboG<>();
            comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (this.comboCategorias.getSelectedT() != null) {
                FiltroIndicadores fc = new FiltroIndicadores();
                fc.setHabilitado(Boolean.TRUE);
                fc.setEsExterno(Boolean.TRUE);
                fc.setAscending(new boolean[]{true});
                fc.setCategoriaPk(this.comboCategorias.getSelectedT().getCinPk());
                fc.setOrderBy(new String[]{"indNombre"});
                //No se utiliza incluirCampos, para que traiga las desagregaciones
                comboIndicadores = new SofisComboG<>(indicadoresClient.buscar(fc), "indNombre");
                comboIndicadores.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void selectIndicador() {
        try {
            comboDesagregaciones = new SofisComboG<>();
            comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (this.comboIndicadores.getSelectedT() != null) {
                comboDesagregaciones = new SofisComboG<>(this.comboIndicadores.getSelectedT().getIndDesagregaciones(), "text");
                comboDesagregaciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            nombreArchivo = event.getFile().getFileName();
            SgEstArchivo arc = new SgEstArchivo();
            handleArchivoBean.subirArchivoTmp(event.getFile(), arc);
            entidadEnEdicion.setCarExcelTmpPath(arc.getAchTmpPath());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");

        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
    }

    public String navegacionEstadisticas() {
        String ret = "?n=" + this.entidadEnEdicion.getCarNombre().getNexPk()
                + "&a=" + this.entidadEnEdicion.getCarAnio()
                + "&c=" + this.entidadEnEdicion.getCarIndicador().getIndCategoria().getCinPk()
                + "&i=" + this.entidadEnEdicion.getCarIndicador().getIndPk()
                + (this.entidadEnEdicion.getCarDesagregacion() != null ? ("&d=" + this.entidadEnEdicion.getCarDesagregacion().name()) : "");
        LOGGER.log(Level.SEVERE, ret);
        return ret;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCargaExterna();
    }

    public void actualizar(SgCargaExterna var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = var;
        this.comboTipoNumerico.setSelectedT(entidadEnEdicion.getCarTipoNumerico());
    }

    public void verificarExistenciaYGuardar() {
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getCarPk() == null) {
                entidadEnEdicion.setCarNombre(this.comboNombresExtr.getSelectedT());
                entidadEnEdicion.setCarIndicador(this.comboIndicadores.getSelectedT());
                entidadEnEdicion.setCarDesagregacion(this.comboDesagregaciones.getSelectedT());
            }
            entidadEnEdicion.setCarTipoNumerico(this.comboTipoNumerico.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            nombreArchivo = null;
            entidadEnEdicion.setCarExcelTmpPath(null);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgCargaExterna getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCargaExterna entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public SofisComboG<SgEstIndicador> getComboIndicadores() {
        return comboIndicadores;
    }

    public void setComboIndicadores(SofisComboG<SgEstIndicador> comboIndicadores) {
        this.comboIndicadores = comboIndicadores;
    }

    public SofisComboG<SgEstCategoriaIndicador> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgEstCategoriaIndicador> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }

    public SofisComboG<EnumDesagregacion> getComboDesagregaciones() {
        return comboDesagregaciones;
    }

    public void setComboDesagregaciones(SofisComboG<EnumDesagregacion> comboDesagregaciones) {
        this.comboDesagregaciones = comboDesagregaciones;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public SofisComboG<EnumTipoNumericoValorEstadistica> getComboTipoNumerico() {
        return comboTipoNumerico;
    }

    public void setComboTipoNumerico(SofisComboG<EnumTipoNumericoValorEstadistica> comboTipoNumerico) {
        this.comboTipoNumerico = comboTipoNumerico;
    }

}

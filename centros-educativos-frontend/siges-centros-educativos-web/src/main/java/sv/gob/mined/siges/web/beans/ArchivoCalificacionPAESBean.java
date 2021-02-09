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
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgArchivoCalificacionPAES;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroArchivoCalificacionPAES;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRangoFecha;
import sv.gob.mined.siges.web.lazymodels.LazyArchivoCalificacionPAESDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.ArchivoCalificacionPAESRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.RangoFechaRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ArchivoCalificacionPAESBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ArchivoCalificacionPAESBean.class.getName());

    @Inject
    private ArchivoCalificacionPAESRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PeriodoCalificacionRestClient restPeriodoCalificacion;

    @Inject
    private RangoFechaRestClient rangoFechaRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private FiltroArchivoCalificacionPAES filtro = new FiltroArchivoCalificacionPAES();
    private SgArchivoCalificacionPAES entidadEnEdicion = new SgArchivoCalificacionPAES();
    private List<RevHistorico> historialCalificacionEstudiante = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyArchivoCalificacionPAESDataModel archivoLazyModel;
    private List<SelectItem> filtroEstado;
    private EnumTiposPeriodosCalificaciones tipoPeriodo;
    private List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList;
    private SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario;
    private List<SelectItem> comboPeriodos;
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private Integer comboPeriodoSeleccionado;

    private String tamanioImportArchivo = "1048576"; //1MB por defecto

    public ArchivoCalificacionPAESBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.TAMANIO_ARCHIVO_IMPORT_PAES);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }

            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_PAES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"acpArchivo.achNombre", "acpArchivo.achPk", "acpArchivo.achExt", "acpArchivo.achContentType", "acpArchivo.achVersion",
                "acpResultado", "acpEstadoArchivo", "acpUltModFecha", "acpVersion","acpAnio"});
            totalResultados = restClient.buscarTotal(filtro);
            archivoLazyModel = new LazyArchivoCalificacionPAESDataModel(restClient, filtro, totalResultados, paginado);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            filtroEstado = new ArrayList(Arrays.asList(EnumEstadoArchivoCalificacionPAES.values()));

            List<EnumTiposPeriodosCalificaciones> tipoPeriodoCalList = new ArrayList();
            tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.ORD);
            tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.EXORD);
            periodoOrdinarioList = new ArrayList(tipoPeriodoCalList);
            tipoPeriodo = EnumTiposPeriodosCalificaciones.ORD;
            cargarPeriodoCalificacion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroArchivoCalificacionPAES();
        buscar();
    }

    public void limpiarCombos() {
        comboPeriodoCalificacion.setSelected(-1);
        comboRangoFecha.setSelected(-1);
    }

    public void agregar() {
        entidadEnEdicion = new SgArchivoCalificacionPAES();
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgArchivoCalificacionPAES var) {
        entidadEnEdicion = (SgArchivoCalificacionPAES) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getAcpPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            this.buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getAcpArchivo() == null) {
            entidadEnEdicion.setAcpArchivo(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getAcpArchivo(), tmpBasePath);
    }

    public void eliminarArchivo() {
        entidadEnEdicion.setAcpArchivo(null);
    }

    public void historial(Long id) {
        try {
            historialCalificacionEstudiante = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarPeriodoCalificacion() {
        try {
            comboTipoPeriodoCalendario = new SofisComboG();
            comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            comboPeriodoCalificacion = new SofisComboG();
            comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            switch (tipoPeriodo) {
                case ORD:
                    FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                    fpc.setIncluirCampos(new String[]{
                        "pcaNumero",
                        "pcaPermiteCalificarSinNie",
                        "pcaEsPrueba",
                        "pcaNombre",
                        "pcaVersion"});
                    fpc.setPcaEsPrueba(Boolean.TRUE);
                    List<SgPeriodoCalificacion> listPeriodoCalif = restPeriodoCalificacion.buscar(fpc);
                    comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "pcaNombre");
                    comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    break;

                case EXORD:
                    List<EnumCalendarioEscolar> listEnumCalendario = new ArrayList();

                    comboTipoPeriodoCalendario = new SofisComboG(listEnumCalendario, "text");
                    comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    comboPeriodos = new ArrayList();
                    comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFecha() {
        try {
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboPeriodoCalificacion.getSelectedT() != null) {
                FiltroRangoFecha fre = new FiltroRangoFecha();
                fre.setPeriodoCalificacionPk(comboPeriodoCalificacion.getSelectedT().getPcaPk());
                fre.setHabilitado(Boolean.TRUE);
                fre.setIncluirCampos(new String[]{
                    "rfeCodigo",
                    "rfeFechaDesde",
                    "rfeFechaDesde",
                    "rfeFechaHasta",
                    "rfeHabilitado",
                    "rfePeriodoCalificacion.pcaPk",
                    "rfePeriodoCalificacion.pcaNombre",
                    "rfePeriodoCalificacion.pcaNumero",
                    "rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                    "rfePeriodoCalificacion.pcaVersion",
                    "rfePeriodoCalificacion.pcaEsPrueba",
                    "rfeVersion"});
                fre.setOrderBy(new String[]{"rfeFechaDesde"});
                fre.setAscending(new boolean[]{true});
                List<SgRangoFecha> listrfe = rangoFechaRestClient.buscarConCache(fre);
                comboRangoFecha = new SofisComboG(listrfe, "rfeCodigoRango");
                comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFechaCalendarioEscolar() {
        try {
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            Integer cantidadPrueba = 0;
            if (comboTipoPeriodoCalendario.getSelectedT() != null) {
                comboPeriodos = new ArrayList();
                comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                for (int i = 1; i <= cantidadPrueba; i++) {
                    comboPeriodos.add(new SelectItem(i, Etiquetas.getValue("calificacion") + " " + i));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void guardar() {
        try {
            entidadEnEdicion.setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ARCHIVO_EXITOSAMENTE_COLA), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void procesarArchivo() {
        try {
            restClient.ejecutarProcesamientoArchivos();
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ARCHIVOS_PENDIENTES_PROCESADOS), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    public FiltroArchivoCalificacionPAES getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroArchivoCalificacionPAES filtro) {
        this.filtro = filtro;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public SofisComboG<EnumCalendarioEscolar> getComboTipoPeriodoCalendario() {
        return comboTipoPeriodoCalendario;
    }

    public void setComboTipoPeriodoCalendario(SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario) {
        this.comboTipoPeriodoCalendario = comboTipoPeriodoCalendario;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

    public SofisComboG<SgPeriodoCalificacion> getComboPeriodoCalificacion() {
        return comboPeriodoCalificacion;
    }

    public void setComboPeriodoCalificacion(SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion) {
        this.comboPeriodoCalificacion = comboPeriodoCalificacion;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public List<EnumTiposPeriodosCalificaciones> getPeriodoOrdinarioList() {
        return periodoOrdinarioList;
    }

    public void setPeriodoOrdinarioList(List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList) {
        this.periodoOrdinarioList = periodoOrdinarioList;
    }

    public EnumTiposPeriodosCalificaciones getTipoPeriodo() {
        return tipoPeriodo;
    }

    public void setTipoPeriodo(EnumTiposPeriodosCalificaciones tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    public List<SelectItem> getFiltroEstado() {
        return filtroEstado;
    }

    public void setFiltroEstado(List<SelectItem> filtroEstado) {
        this.filtroEstado = filtroEstado;
    }

    public SgArchivoCalificacionPAES getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgArchivoCalificacionPAES entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalificacionEstudiante() {
        return historialCalificacionEstudiante;
    }

    public void setHistorialCalificacionEstudiante(List<RevHistorico> historialCalificacionEstudiante) {
        this.historialCalificacionEstudiante = historialCalificacionEstudiante;
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

    public LazyArchivoCalificacionPAESDataModel getArchivoLazyModel() {
        return archivoLazyModel;
    }

    public void setArchivoLazyModel(LazyArchivoCalificacionPAESDataModel archivoLazyModel) {
        this.archivoLazyModel = archivoLazyModel;
    }

    //</editor-fold>
}

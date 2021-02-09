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
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgRecibos;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgTransferenciaACed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRecibos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaACed;
import sv.gob.mined.siges.web.lazymodels.LazyRecibosDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.RecibosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaACedRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente a la gestión de los recibos.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RecibosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RecibosBean.class.getName());

    @Inject
    private RecibosRestClient restClient;

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private TransferenciaACedRestClient transferenciasClient;

    private FiltroRecibos filtroRecibo = new FiltroRecibos();
    private FiltroSedes filtroSede = new FiltroSedes();
    private FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
    private SgRecibos entidadEnEdicion = new SgRecibos();
    private List<SgRecibos> historialRecibos = new ArrayList();
    private SgTransferenciaACed transferencia;
    private SgSede sedeSeleccionada;
    private List<SelectItem> comboTransferencias1 = new ArrayList<>();
    private SofisComboG<SgTransferenciaACed> comboTransferencias = new SofisComboG<>();
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRecibosDataModel recibosLazyModel;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";

    /**
     * Constructor de la clase.
     */
    public RecibosBean() {

    }

    /**
     * Método para incializar filtros, combos y búsqueda.
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
     * Busca los recibos según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtroRecibo.setFirst(new Long(0));
            filtroRecibo.setIncluirCampos(new String[]{
            "recPk","recFecha",
            "recTransferencia.tacPk",
            "recTransferencia.tacVersion",
            "recTransferencia.tacCedFk.sedPk",
            "recTransferencia.tacCedFk.sedCodigo",
            "recTransferencia.tacCedFk.sedNombre",
            "recTransferencia.tacCedFk.sedTipo",
            "recTransferencia.tacCedFk.sedVersion",
            "recTransferencia.tacReqFondoCed.rfcPk",
            "recTransferencia.tacReqFondoCed.rfcVersion",
            "recMonto","recEstado","recUltModFecha",
            "recArchivo.achPk","recArchivo.achNombre","recArchivo.achTmpPath","recArchivo.achContentType",
            "recArchivo.achExt","recArchivo.achVersion"
            });

            if (anioFiscalCombo.getSelectedT() != null) {
                filtroRecibo.setAnioFiscal(anioFiscalCombo.getSelectedT().getAniAnio());
            }

            if (sedeSeleccionada != null) {
                filtroRecibo.setSedePk(sedeSeleccionada.getSedPk());
            }

            totalResultados = restClient.buscarTotal(filtroRecibo);
            recibosLazyModel = new LazyRecibosDataModel(restClient, filtroRecibo, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de anio fiscal.
     */
    public void cargarCombos() {
        try {
            FiltroTransferenciaACed filtroCed = new FiltroTransferenciaACed();
            filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setOrderBy(new String[]{"aniPk"});
            filtroAnioFiscal.setAscending(new boolean[]{false});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (HttpServerException ex) {
            Logger.getLogger(RecibosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpClientException ex) {
            Logger.getLogger(RecibosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HttpServerUnavailableException ex) {
            Logger.getLogger(RecibosBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessException ex) {
            Logger.getLogger(RecibosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Sede para el filtro de pantalla.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSedeFiltro(String query) {
        try {
            filtroSede = new FiltroSedes();
            filtroSede.setSedCodigoONombre(query);
            filtroSede.setSedHabilitado(Boolean.TRUE);
            filtroSede.setMaxResults(11L);
            filtroSede.setOrderBy(new String[]{"sedNombre"});
            filtroSede.setAscending(new boolean[]{false});
            filtroSede.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(filtroSede);
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
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        comboTransferencias.setSelected(-1);
        anioFiscalCombo.setSelected(-1);
        filtroSede = new FiltroSedes();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtroRecibo = new FiltroRecibos();
        limpiarCombos();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo recibo.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgRecibos();
    }

    /**
     * Carga los datos de un recibo para poder ser editados.
     *
     * @param var SgRecibos: Elemento del grid seleccionado de recibos.
     */
    public void actualizar(SgRecibos var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgRecibos) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de recibo.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setRecTransferencia(comboTransferencias.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Sube un archivo al servidor.
     *
     * @param event FileUploadEvent
     */
    public void subirArchivo(FileUploadEvent event) {
        try {
            entidadEnEdicion.setRecArchivo(new SgArchivo());
            handleArchivoBean.subirArchivoTmp(event.getFile(), entidadEnEdicion.getRecArchivo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de recibo.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getRecPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id Long
     */
    public void historial(Long id) {
        try {
            historialRecibos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public SgRecibos getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRecibos entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgRecibos> getHistorialRecibos() {
        return historialRecibos;
    }

    public void setHistorialRecibos(List<SgRecibos> historialRecibos) {
        this.historialRecibos = historialRecibos;
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

    public LazyRecibosDataModel getRecibosLazyModel() {
        return recibosLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyRecibosDataModel recibosLazyModel) {
        this.recibosLazyModel = recibosLazyModel;
    }

    public FiltroRecibos getFiltroRecibo() {
        return filtroRecibo;
    }

    public void setFiltroRecibo(FiltroRecibos filtroRecibo) {
        this.filtroRecibo = filtroRecibo;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public List<SelectItem> getComboTransferencias1() {
        return comboTransferencias1;
    }

    public void setComboTransferencias1(List<SelectItem> comboTransferencias1) {
        this.comboTransferencias1 = comboTransferencias1;
    }

    public SgTransferenciaACed getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(SgTransferenciaACed transferencia) {
        this.transferencia = transferencia;
    }

    public SofisComboG<SgTransferenciaACed> getComboTransferencias() {
        return comboTransferencias;
    }

    public void setComboTransferencias(SofisComboG<SgTransferenciaACed> comboTransferencias) {
        this.comboTransferencias = comboTransferencias;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    // </editor-fold>
}

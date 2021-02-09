/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgDocumentos;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.enumerados.EnumEstadoDocumento;
import sv.gob.mined.siges.web.enumerados.TipoDocumento;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocumentos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.lazymodels.LazyDocumentosDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DocumentosRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaACedRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de los documentos.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DocumentosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocumentosBean.class.getName());

    @Inject
    private DocumentosRestClient restClient;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PresupuestoEscolarRestCliente presupuestoEscolarClient;

    @Inject
    private TransferenciaACedRestClient transferenciasClient;

    @Inject
    private OrganismoAdministracionEscolarRestClient oaeClient;
    
    @Inject 
    private SedeRestClient sedeRestClient;

    @Inject
    @Param(name = "id")
    private Long presupuestoEscolarId;

    private FiltroDocumentos filtro = new FiltroDocumentos();
    private SgDocumentos convenioEnEdicion = new SgDocumentos();
    private SgDocumentos congelacionEnEdicion = new SgDocumentos();
    private List<SgDocumentos> historialDocumentos = new ArrayList();
    private List<SgDocumentos> documentos = new ArrayList();
    private SofisComboG<SgPresupuestoEscolar> comboPresupuesto = new SofisComboG<>();
    private List<SelectItem> tipoDocumentoItems = new ArrayList<>();
    private List<SelectItem> estadoDocumentoItems = new ArrayList<>();
    private TipoDocumento tipoDocumento;
    private SgPresupuestoEscolar presupuesto = new SgPresupuestoEscolar();
    private SgOrganismoAdministracionEscolar oae = new SgOrganismoAdministracionEscolar();
    private SgCentroEducativo ced;

    private EnumEstadoDocumento enumEstadoConvenio;
    private EnumEstadoDocumento enumEstadoCongelacion;

    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDocumentosDataModel documentosLazyModel;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";

    public DocumentosBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            if (presupuestoEscolarId != null && presupuestoEscolarId > 0) {
                presupuesto = new SgPresupuestoEscolar();
                FiltroPresupuestoEscolar filtroPres = new FiltroPresupuestoEscolar();
                filtroPres.setIncluirCampos(new String[]{
                    "pesPk",
                    "pesNombre",
                    "pesVersion",
                    "pesEstado",
                    "pesCodigo",
                    "pesDescripcion",
                    "pesHabilitado",
                    "pesSedeFk.sedPk",
                    "pesSedeFk.sedCodigo",
                    "pesSedeFk.sedNombre",
                    "pesSedeFk.sedTipo",
                    "pesSedeFk.sedVersion",
                    "pesAnioFiscalFk.aniPk",
                    "pesAnioFiscalFk.aniAnio",
                    "pesAnioFiscalFk.aniVersion"
                });
                filtroPres.setPesPk(presupuestoEscolarId);
                List<SgPresupuestoEscolar> list = presupuestoEscolarClient.buscar(filtroPres);
                if (!list.isEmpty()) {
                    presupuesto = list.get(0);
                }

                if (presupuesto != null) {
                    initDatos();
                }
            }
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inicialización de datos en la página correspondiente al bean
     */
    public void initDatos() {
        try {
            if (presupuesto != null) {
                FiltroOrganismoAdministrativoEscolar filtroOAE = new FiltroOrganismoAdministrativoEscolar();
                filtroOAE.setIncluirCampos(new String[]{"oaeVersion", "oaeFechaVencimiento","oaeMiembrosVigentes"});
                filtroOAE.setOaeSedeFk(presupuesto.getPesSedeFk().getSedPk());
                List<SgOrganismoAdministracionEscolar> oaes = oaeClient.buscar(filtroOAE);
                if (!oaes.isEmpty()) {
                    oae = oaes.get(0);
                }

                if (presupuesto.getPesSedeFk().getSedTipo().equals(TipoSede.CED_OFI) || presupuesto.getPesSedeFk().getSedTipo().equals(TipoSede.CED_PRI)) {
                    ced = sedeRestClient.obtenerCentroEducativoPorId(presupuesto.getPesSedeFk().getSedPk());
                    LOGGER.log(Level.SEVERE,"TIPO OAE "+ ced.getCedTipoOrganismoAdministrativo().getToaCodigo());
                }
                filtro.setDocPresupuestoFk(presupuesto);
                documentos = restClient.buscar(filtro);
                if (documentos.size() > 1) {

                    convenioEnEdicion.setDocPresupuestoFk(presupuesto);
                    convenioEnEdicion.setDocTipoDocumento(TipoDocumento.CONVENIO);

                    congelacionEnEdicion.setDocPresupuestoFk(presupuesto);
                    congelacionEnEdicion.setDocTipoDocumento(TipoDocumento.CONGELACION);

                    List<SgDocumentos> subListaConvenio = documentos.stream().filter(d -> d.getDocTipoDocumento().getText().equals(TipoDocumento.CONVENIO.getText())).collect(Collectors.toList());
                    if (subListaConvenio.size() > 0) {
                        convenioEnEdicion = subListaConvenio.get(0);
                    }
                    List<SgDocumentos> subListaCongelacion = documentos.stream().filter(d -> d.getDocTipoDocumento().getText().equals(TipoDocumento.CONGELACION.getText())).collect(Collectors.toList());
                    if (subListaCongelacion.size() > 0) {
                        congelacionEnEdicion = subListaCongelacion.get(0);
                    }

                    enumEstadoCongelacion = congelacionEnEdicion.getDocEstadoDocumento();
                    enumEstadoConvenio = convenioEnEdicion.getDocEstadoDocumento();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    /**
     * Obtiene Path de documento segun tipo oae
     */
    public String obtenerPath(TipoDocumento tipoDoc) {
        String path = new String();
        try{
            if(ced.getCedTipoOrganismoAdministrativo().getToaPk().toString().equals(Constantes.CDE)){
                path=tipoDoc.equals(TipoDocumento.CONVENIO) ? ConstantesPaginas.CONVENIO_CDE : ConstantesPaginas.CONGELACION_CDE;
            }
            else if(ced.getCedTipoOrganismoAdministrativo().getToaPk().toString().equals(Constantes.CECE)){
                path=tipoDoc.equals(TipoDocumento.CONVENIO) ? ConstantesPaginas.CONVENIO_CECE : ConstantesPaginas.CONGELACION_CECE;
            }
            else if(ced.getCedTipoOrganismoAdministrativo().getToaPk().toString().equals(Constantes.CIE)){
                path=tipoDoc.equals(TipoDocumento.CONVENIO) ? ConstantesPaginas.CONVENIO_CIE : ConstantesPaginas.CONGELACION_CIE;
            }
        }
        catch(Exception e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return path;
    }

    /**
     * Carga el combo de estados de documentos.
     */
    public void cargarCombos() {
        Arrays.asList(EnumEstadoDocumento.values()).forEach(e -> {
            estadoDocumentoItems.add(new SelectItem(e, e.getText()));
        });
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros y vuelve a buscar.
     */
    public void limpiar() {
        filtro = new FiltroDocumentos();
        initDatos();
    }

    /**
     * Limpia los combos y crea un nuevo objeto de documento.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        convenioEnEdicion = new SgDocumentos();
    }

    /**
     * Carga los datos de un documento para poder ser editados.
     *
     * @param var
     */
    public void actualizar(SgDocumentos var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        convenioEnEdicion = (SgDocumentos) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de documento.
     */
    public void guardar() {
        try {
            convenioEnEdicion.setDocEstadoDocumento(enumEstadoConvenio);
            convenioEnEdicion.setDocTipoDocumento(TipoDocumento.CONVENIO);
            convenioEnEdicion.setDocPresupuestoFk(presupuesto);
            congelacionEnEdicion.setDocEstadoDocumento(enumEstadoCongelacion);
            congelacionEnEdicion.setDocTipoDocumento(TipoDocumento.CONGELACION);
            congelacionEnEdicion.setDocPresupuestoFk(presupuesto);
            if (convenioEnEdicion.getDocArchivoFk() != null) {
                convenioEnEdicion = restClient.guardar(convenioEnEdicion);
            }
            if (congelacionEnEdicion.getDocArchivoFk() != null) {
                congelacionEnEdicion = restClient.guardar(congelacionEnEdicion);
            }
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            //initDatos();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de documento.
     */
    public void eliminar() {
        try {
            restClient.eliminar(convenioEnEdicion.getDocPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            initDatos();
            convenioEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Sube archivo de convenio a la carpet temp
     *
     * @param event
     */
    public void subirArchivoConvenio(FileUploadEvent event) {
        try {
            convenioEnEdicion.setDocArchivoFk(new SgArchivo());
            handleArchivoBean.subirArchivoTmp(event.getFile(), convenioEnEdicion.getDocArchivoFk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Sube archivo de congelación a la carpet temp
     *
     * @param event
     */
    public void subirArchivoCongelacion(FileUploadEvent event) {
        try {
            congelacionEnEdicion.setDocArchivoFk(new SgArchivo());
            handleArchivoBean.subirArchivoTmp(event.getFile(), congelacionEnEdicion.getDocArchivoFk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Validación del OAE
     *
     * @param event
     */
    public boolean validarOAE() {
        Boolean oaeValidada = Boolean.TRUE;
        LocalDate localDate = LocalDate.now();
        try {
            if (oae != null) {
                if (oae.getOaeMiembrosVigentes() != null && oae.getOaeFechaVencimiento() != null) {
                    if (oae.getOaeMiembrosVigentes().equals(Boolean.FALSE) || oae.getOaeFechaVencimiento().isBefore(localDate)) {
                        oaeValidada = Boolean.FALSE;
                    }
                } else {
                    oaeValidada = Boolean.FALSE;
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return oaeValidada;
    }
    
    /**
     * Cambiar de estado a documento
     * @param event
     */
    public void cambiarEstadoDoc(SgDocumentos var) {
        try {
            convenioEnEdicion = (SgDocumentos) SerializationUtils.clone(var);
            convenioEnEdicion.setDocEstadoDocumento(EnumEstadoDocumento.GENERADO);
            congelacionEnEdicion = restClient.guardar(congelacionEnEdicion);
        }
        catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialDocumentos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroDocumentos getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDocumentos filtro) {
        this.filtro = filtro;
    }

    public SgDocumentos getConvenioEnEdicion() {
        return convenioEnEdicion;
    }

    public void setConvenioEnEdicion(SgDocumentos convenioEnEdicion) {
        this.convenioEnEdicion = convenioEnEdicion;
    }

    public List<SgDocumentos> getHistorialDocumentos() {
        return historialDocumentos;
    }

    public void setHistorialDocumentos(List<SgDocumentos> historialDocumentos) {
        this.historialDocumentos = historialDocumentos;
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

    public LazyDocumentosDataModel getDocumentosLazyModel() {
        return documentosLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDocumentosDataModel documentosLazyModel) {
        this.documentosLazyModel = documentosLazyModel;
    }

    public List<SelectItem> getTipoDocumentoItems() {
        return tipoDocumentoItems;
    }

    public void setTipoDocumentoItems(List<SelectItem> tipoDocumentoItems) {
        this.tipoDocumentoItems = tipoDocumentoItems;
    }

    public List<SelectItem> getEstadoDocumentoItems() {
        return estadoDocumentoItems;
    }

    public void setEstadoDocumentoItems(List<SelectItem> estadoDocumentoItems) {
        this.estadoDocumentoItems = estadoDocumentoItems;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public EnumEstadoDocumento getEnumEstadoConvenio() {
        return enumEstadoConvenio;
    }

    public void setEnumEstadoConvenio(EnumEstadoDocumento enumEstadoConvenio) {
        this.enumEstadoConvenio = enumEstadoConvenio;
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

    public SofisComboG<SgPresupuestoEscolar> getComboPresupuesto() {
        return comboPresupuesto;
    }

    public void setComboPresupuesto(SofisComboG<SgPresupuestoEscolar> comboPresupuesto) {
        this.comboPresupuesto = comboPresupuesto;
    }

    public SgDocumentos getCongelacionEnEdicion() {
        return congelacionEnEdicion;
    }

    public void setCongelacionEnEdicion(SgDocumentos congelacionEnEdicion) {
        this.congelacionEnEdicion = congelacionEnEdicion;
    }

    public EnumEstadoDocumento getEnumEstadoCongelacion() {
        return enumEstadoCongelacion;
    }

    public void setEnumEstadoCongelacion(EnumEstadoDocumento enumEstadoCongelacion) {
        this.enumEstadoCongelacion = enumEstadoCongelacion;
    }

    public SgPresupuestoEscolar getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(SgPresupuestoEscolar presupuesto) {
        this.presupuesto = presupuesto;
    }

    public SgCentroEducativo getCed() {
        return ced;
    }

    public void setCed(SgCentroEducativo ced) {
        this.ced = ced;
    }

    public SgOrganismoAdministracionEscolar getOae() {
        return oae;
    }

    public void setOae(SgOrganismoAdministracionEscolar oae) {
        this.oae = oae;
    }
// </editor-fold>
}

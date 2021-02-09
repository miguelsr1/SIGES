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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleDocumento;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoDocumento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleDocumento;
import sv.gob.mined.siges.web.lazymodels.LazyRelInmuebleDocumentoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleDocumentoRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleDocumentoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleDocumentoBean.class.getName());

    @Inject
    private RelInmuebleDocumentoRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private FiltroRelInmuebleDocumento filtro = new FiltroRelInmuebleDocumento();
    private SgInmueblesSedes entidadEnEdicion;
    private SgRelInmuebleDocumento documentoEnEdicion = new SgRelInmuebleDocumento();
    private Boolean soloLectura = Boolean.FALSE;
    private String securityOperation;
    private List<SgRelInmuebleDocumento> historialRelInmuebleArchivo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRelInmuebleDocumentoDataModel relInmuebleDocumentoLazyModel;
    private Boolean beanIniciado = Boolean.FALSE;
    private SofisComboG<SgInfTipoDocumento> comboTipoDocumento;
    private SofisComboG<SgInfTipoDocumento> comboTipoDocumentoBusqueda;
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";
    private String tamanioImportArchivo;

    public RelInmuebleDocumentoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
     
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarEntidad() {
        try {
            if (!beanIniciado) {
                buscar();
                beanIniciado = Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {
            filtro.setInmuebleFk(entidadEnEdicion.getTisPk());
            filtro.setTipoDocumentoFk(this.comboTipoDocumentoBusqueda.getSelectedT() != null ? this.comboTipoDocumentoBusqueda.getSelectedT().getTidPk() : null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            relInmuebleDocumentoLazyModel = new LazyRelInmuebleDocumentoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"tidNombre"});
            fil.setAscending(new boolean[]{true});
            List<SgInfTipoDocumento> listTipoDocs = catalogosRestClient.buscarTipoDocumento(fil);
            comboTipoDocumento = new SofisComboG(listTipoDocs, "tidNombre");
            comboTipoDocumento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTipoDocumentoBusqueda = new SofisComboG(listTipoDocs, "tidNombre");
            comboTipoDocumentoBusqueda.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigoExacto(Constantes.TAMANIO_ARCHIVO_IMPORT_INFRA);
            List<SgConfiguracion> conf = catalogosRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tamanioImportArchivo = conf.get(0).getConValor();
            }

            fc.setCodigoExacto(Constantes.TIPO_ARCHIVO_IMPORT_INFRA);
            conf = catalogosRestClient.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                tipoImportArchivo = conf.get(0).getConValor();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoDocumento.setSelected(-1);
        comboTipoDocumentoBusqueda.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroRelInmuebleDocumento();
        limpiarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        documentoEnEdicion = new SgRelInmuebleDocumento();
    }

    public void actualizar(SgRelInmuebleDocumento var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        documentoEnEdicion = (SgRelInmuebleDocumento) SerializationUtils.clone(var);
        this.comboTipoDocumento.setSelectedT(documentoEnEdicion.getRidTipoDocumento());
    }

    public void guardar() {
        try {
            documentoEnEdicion.setRidTipoDocumento(comboTipoDocumento.getSelectedT());
            documentoEnEdicion.setRidInmuebleSede(entidadEnEdicion);
            documentoEnEdicion = restClient.guardar(documentoEnEdicion);
            buscar();
            PrimeFaces.current().executeScript("PF('itemCargaDialogDocumentos').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgDocumentosSede", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError("popupmsgDocumentosSede", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(documentoEnEdicion.getRidPk());
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

    public void handleFileUpload(FileUploadEvent event) {
        JSFUtils.limpiarMensajesError();
        try {
            UploadedFile uploadedFile = event.getFile();
            byte[] bytes = IOUtils.toByteArray(uploadedFile.getInputstream());
            if(documentoEnEdicion.getRidDocumento() == null) {
                documentoEnEdicion.setRidDocumento(new SgArchivo());
            }
            FileUploadUtils.handleFileUpload(event.getFile(), documentoEnEdicion.getRidDocumento(), tmpBasePath);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroRelInmuebleDocumento getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRelInmuebleDocumento filtro) {
        this.filtro = filtro;
    }

    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgInfTipoDocumento> getComboTipoDocumentoBusqueda() {
        return comboTipoDocumentoBusqueda;
    }

    public void setComboTipoDocumentoBusqueda(SofisComboG<SgInfTipoDocumento> comboTipoDocumentoBusqueda) {
        this.comboTipoDocumentoBusqueda = comboTipoDocumentoBusqueda;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgRelInmuebleDocumento> getHistorialRelInmuebleArchivo() {
        return historialRelInmuebleArchivo;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }


    public void setHistorialRelInmuebleArchivo(List<SgRelInmuebleDocumento> historialRelInmuebleArchivo) {
        this.historialRelInmuebleArchivo = historialRelInmuebleArchivo;
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

    public LazyRelInmuebleDocumentoDataModel getRelInmuebleDocumentoLazyModel() {
        return relInmuebleDocumentoLazyModel;
    }

    public void setRelInmuebleDocumentoLazyModel(LazyRelInmuebleDocumentoDataModel relInmuebleDocumentoLazyModel) {
        this.relInmuebleDocumentoLazyModel = relInmuebleDocumentoLazyModel;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public SgRelInmuebleDocumento getDocumentoEnEdicion() {
        return documentoEnEdicion;
    }

    public void setDocumentoEnEdicion(SgRelInmuebleDocumento documentoEnEdicion) {
        this.documentoEnEdicion = documentoEnEdicion;
    }

    public SofisComboG<SgInfTipoDocumento> getComboTipoDocumento() {
        return comboTipoDocumento;
    }

    public void setComboTipoDocumento(SofisComboG<SgInfTipoDocumento> comboTipoDocumento) {
        this.comboTipoDocumento = comboTipoDocumento;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.centros_educativos.SgArchivo;
import sv.gob.mined.siges.web.dto.centros_educativos.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.SgDocumentoSistema;
import sv.gob.mined.siges.web.dto.catalogo.SgSiTipoDocumento;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaDocumento;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDocumentoSistema;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DocumentoSistemaRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DocumentosSistemaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocumentosSistemaBean.class.getName());

    @Inject
    private DocumentoSistemaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private FiltroDocumentoSistema filtro = new FiltroDocumentoSistema();
    private SgSistemaIntegrado entidadEnEdicion;
    private SgDocumentoSistema documentoEnEdicion = new SgDocumentoSistema();
    private Boolean soloLectura = Boolean.FALSE;
    private String securityOperation;
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgSiTipoDocumento> comboTipoDocumento;
    private SofisComboG<SgSiTipoDocumento> comboTipoDocumentoBusqueda;
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";
    private String tamanioImportArchivo;
    private List<SgDocumentoSistema> listaDocumentos;
    private EnumCategoriaDocumento categoriaDocumento;

    public DocumentosSistemaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgSistemaIntegrado) request.getAttribute("sistemaIntegrado");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
     
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }


    public void buscar() {
        try {
            filtro.setSistemaIntegrado(entidadEnEdicion.getSinPk());
            filtro.setTipoDocumento(this.comboTipoDocumentoBusqueda.getSelectedT() != null ? this.comboTipoDocumentoBusqueda.getSelectedT().getTidPk() : null);
            filtro.setCategoriaDocumento(categoriaDocumento);
            listaDocumentos = restClient.buscar(filtro);
            totalResultados = Long.valueOf(listaDocumentos.size());
            
            //Actualizar las listas del sistema integrado
            actualizarListas();
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarListas(){
        String expr = "#{controllerParam[actionParam](documentosSistemaBean.listaDocumentos, documentosSistemaBean.filtro)}";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        JSFUtils.executeExpressionInElContext(facesContext.getApplication(), facesContext.getELContext(), expr);
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"tidNombre"});
            fil.setAscending(new boolean[]{true});
            List<SgSiTipoDocumento> listTipoDocs = catalogosRestClient.buscarTipoDocumento(fil);
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
        listaDocumentos = null;
        totalResultados = 0L;
        filtro = new FiltroDocumentoSistema();
        limpiarCombos();
        
        //Actualizar las listas del sistema integrado
        actualizarListas();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        documentoEnEdicion = new SgDocumentoSistema();
    }

    public void actualizar(SgDocumentoSistema var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        documentoEnEdicion = (SgDocumentoSistema) SerializationUtils.clone(var);
        this.comboTipoDocumento.setSelectedT(documentoEnEdicion.getDsiTipoDocumento());
    }

    public void guardar() {
        try {
            documentoEnEdicion.setDsiTipoDocumento(comboTipoDocumento.getSelectedT());
            documentoEnEdicion.setDsiSistemaIntegrado(entidadEnEdicion);
            documentoEnEdicion.setDsiCategoriaDocumento(categoriaDocumento);
            documentoEnEdicion = restClient.guardar(documentoEnEdicion);
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('itemCargaDialogDocumentos').hide()");
            LOGGER.severe("---------------->TERMINO DE GUARDAR");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgDocumentosSede", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError("popupmsgDocumentosSede", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(documentoEnEdicion.getDsiPk());
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
        
        if (documentoEnEdicion.getDsiDocumento() == null) {
            documentoEnEdicion.setDsiDocumento(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), documentoEnEdicion.getDsiDocumento(), tmpBasePath);
    }
    
    public void eliminarArchivo() {
        documentoEnEdicion.setDsiDocumento(null);
    }
    
    public void listasPreCargadas(List<SgDocumentoSistema> l){
        if(l!=null && l.size()>0){
            listaDocumentos = l;
            totalResultados = Long.valueOf(listaDocumentos.size());
        }else{
            listaDocumentos = null;
            totalResultados = 0L;
        }
    }

    public FiltroDocumentoSistema getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDocumentoSistema filtro) {
        this.filtro = filtro;
    }

    public SgSistemaIntegrado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSistemaIntegrado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<SgSiTipoDocumento> getComboTipoDocumentoBusqueda() {
        return comboTipoDocumentoBusqueda;
    }

    public void setComboTipoDocumentoBusqueda(SofisComboG<SgSiTipoDocumento> comboTipoDocumentoBusqueda) {
        this.comboTipoDocumentoBusqueda = comboTipoDocumentoBusqueda;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
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

    public List<SgDocumentoSistema> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<SgDocumentoSistema> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public SgDocumentoSistema getDocumentoEnEdicion() {
        return documentoEnEdicion;
    }

    public void setDocumentoEnEdicion(SgDocumentoSistema documentoEnEdicion) {
        this.documentoEnEdicion = documentoEnEdicion;
    }

    public SofisComboG<SgSiTipoDocumento> getComboTipoDocumento() {
        return comboTipoDocumento;
    }

    public void setComboTipoDocumento(SofisComboG<SgSiTipoDocumento> comboTipoDocumento) {
        this.comboTipoDocumento = comboTipoDocumento;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public EnumCategoriaDocumento getCategoriaDocumento() {
        return categoriaDocumento;
    }

    public void setCategoriaDocumento(EnumCategoriaDocumento categoriaDocumento) {
        this.categoriaDocumento = categoriaDocumento;
    }

}

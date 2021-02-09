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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleArchivo;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoImagen;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleArchivo;
import sv.gob.mined.siges.web.lazymodels.LazyRelInmuebleArchivoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleArchivoRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleArchivoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleArchivoBean.class.getName());

    @Inject
    private RelInmuebleArchivoRestClient restClient;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private FiltroRelInmuebleArchivo filtro = new FiltroRelInmuebleArchivo();
    private SgInmueblesSedes entidadEnEdicion;
    private SgRelInmuebleArchivo archivoEnEdicion=new SgRelInmuebleArchivo();
    private Boolean soloLectura = Boolean.FALSE;
    private String securityOperation;
    private List<SgRelInmuebleArchivo> historialRelInmuebleArchivo = new ArrayList();
    private Integer paginado = 3;
    private Long totalResultados;
    private LazyRelInmuebleArchivoDataModel relInmuebleArchivoLazyModel;
    private Boolean beanIniciado=Boolean.FALSE;
    private SofisComboG<SgInfTipoImagen> comboTipoImagen;

    public RelInmuebleArchivoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean)request.getAttribute("soloLectura")) : soloLectura ;
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void cargarEntidad() {
        try {          
           if(!beanIniciado){
                buscar();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {
            filtro.setInmuebleFk(entidadEnEdicion.getTisPk());
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            relInmuebleArchivoLazyModel = new LazyRelInmuebleArchivoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"tiiNombre"});
            fil.setAscending(new boolean[]{true});
            List<SgInfTipoImagen> listImag=catalogosRestClient.buscarTipoImagen(fil);
            comboTipoImagen= new SofisComboG(listImag, "tiiNombre");
            comboTipoImagen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoImagen.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroRelInmuebleArchivo();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        archivoEnEdicion = new SgRelInmuebleArchivo();
    }

    public void actualizar(SgRelInmuebleArchivo var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        archivoEnEdicion = (SgRelInmuebleArchivo) SerializationUtils.clone(var);
        comboTipoImagen.setSelectedT(archivoEnEdicion.getRiaTipoImagen());
    }

    public void guardar() {
        try {
           
            archivoEnEdicion.setRiaTipoImagen(comboTipoImagen.getSelectedT());
            archivoEnEdicion.setRiaInmuebleSede(entidadEnEdicion);
            archivoEnEdicion = restClient.guardar(archivoEnEdicion);
            PrimeFaces.current().executeScript("PF('itemCargaDialogImagenes').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgMediaSede", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError("popupmsgMediaSede", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(archivoEnEdicion.getRiaPk());
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
            try{
                UploadedFile uploadedFile = event.getFile();
                byte[] bytes = IOUtils.toByteArray(uploadedFile.getInputstream());
                if (archivoEnEdicion.getRiaArchivo()== null) {
                    archivoEnEdicion.setRiaArchivo(new SgArchivo());
                }
                FileUploadUtils.handleFileUpload(event.getFile(), archivoEnEdicion.getRiaArchivo(), tmpBasePath);

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }

    public FiltroRelInmuebleArchivo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRelInmuebleArchivo filtro) {
        this.filtro = filtro;
    }

    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }



    public List<SgRelInmuebleArchivo> getHistorialRelInmuebleArchivo() {
        return historialRelInmuebleArchivo;
    }

    public void setHistorialRelInmuebleArchivo(List<SgRelInmuebleArchivo> historialRelInmuebleArchivo) {
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

    public LazyRelInmuebleArchivoDataModel getRelInmuebleArchivoLazyModel() {
        return relInmuebleArchivoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyRelInmuebleArchivoDataModel relInmuebleArchivoLazyModel) {
        this.relInmuebleArchivoLazyModel = relInmuebleArchivoLazyModel;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public SgRelInmuebleArchivo getArchivoEnEdicion() {
        return archivoEnEdicion;
    }

    public void setArchivoEnEdicion(SgRelInmuebleArchivo archivoEnEdicion) {
        this.archivoEnEdicion = archivoEnEdicion;
    }

    public SofisComboG<SgInfTipoImagen> getComboTipoImagen() {
        return comboTipoImagen;
    }

    public void setComboTipoImagen(SofisComboG<SgInfTipoImagen> comboTipoImagen) {
        this.comboTipoImagen = comboTipoImagen;
    }

}

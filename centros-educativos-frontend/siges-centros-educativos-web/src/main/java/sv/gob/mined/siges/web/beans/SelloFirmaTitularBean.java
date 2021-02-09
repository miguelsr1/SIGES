/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoTitular;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoTitular;
import sv.gob.mined.siges.web.lazymodels.LazySelloFirmaTitularDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaTitularRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;

import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SelloFirmaTitularBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SelloFirmaTitularBean.class.getName());

    @Inject
    private SelloFirmaTitularRestClient restClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    @Param(name = "id")
    private Long selloFirmaTitularId;

    @Inject
    @Param(name = "rev")
    private Long selloFirmaTitularRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    @Param(name = "autentica")
    private Boolean autentica;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private SessionBean sessionBean;

    private FiltroSelloFirmaTitular filtro = new FiltroSelloFirmaTitular();
    private SgSelloFirmaTitular entidadEnEdicion = new SgSelloFirmaTitular();
    private List<SgSelloFirmaTitular> historialSelloFirmaTitular = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySelloFirmaTitularDataModel selloFirmaTitularLazyModel;
    private SofisComboG<SgCargoTitular> comboCargoTitular;
    private Boolean soloLectura = Boolean.FALSE;
    private SgConfiguracion configuracionArchivoAlto;
    private SgConfiguracion configuracionArchivoAncho;
    private SgConfiguracion configuracionArchivoTipo;

    public SelloFirmaTitularBean() {
    }

    @PostConstruct
    public void init() {
        try {

            validarAcceso();
            cargarCombos();
            if (selloFirmaTitularId != null && selloFirmaTitularId > 0) {
                if (selloFirmaTitularRev != null && selloFirmaTitularRev > 0) {
                    //  this.actualizar(restClient.obtenerEnRevision(selloFirmaId, selloFirmaRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(selloFirmaTitularId));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                this.agregar();
            }
            cargarConfiguracionArchivo();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SELLO_FIRMA_TITULAR)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarConfiguracionArchivo() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
                 
            fc.setCodigoExacto(Constantes.ALTO_ARCHIVO_SELLO_FIRMA_IMPORT);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoAlto = !conf.isEmpty() ? conf.get(0) : null;
            
            fc.setCodigoExacto(Constantes.ANCHO_ARCHIVO_SELLO_FIRMA_IMPORT);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoAncho = !conf.isEmpty() ? conf.get(0) : null;

            fc.setCodigoExacto(Constantes.TIPO_ARCHIVO_SELLO_FIRMA_IMPORT);
            conf = catalogoRestClient.buscarConfiguracion(fc);
            configuracionArchivoTipo = !conf.isEmpty() ? conf.get(0) : null;

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (this.selloFirmaTitularId == null) {
            return Etiquetas.getValue("nuevoSelloFirmaTitular");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verSelloFirmaTitular");
        } else {
            return Etiquetas.getValue("edicionSelloFirmaTitular");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCargoTitular fc = new FiltroCargoTitular();
            fc.setHabilitado(Boolean.TRUE);
            fc.setEsTitular(Boolean.TRUE);
            if(BooleanUtils.isTrue(autentica)){
                fc.setEsTitular(Boolean.FALSE);
            }
            fc.setOrderBy(new String[]{"ctiNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgCargoTitular> listCargoTit = catalogoRestClient.buscarCargoTitular(fc);
           
            comboCargoTitular = new SofisComboG(listCargoTit, "ctiNombre");
            comboCargoTitular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
      
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private void limpiarCombos() {
        comboCargoTitular.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroSelloFirmaTitular();
        limpiarCombos();
    }

    
    public void handleFileUploadFirma(FileUploadEvent event) {
        try {
            UploadedFile uploadedFile = event.getFile();
            byte[] bytes = IOUtils.toByteArray(uploadedFile.getInputstream());
            if (validateImageDimensions(bytes)) {
                if (entidadEnEdicion.getSftFirmaSello() == null) {
                    entidadEnEdicion.setSftFirmaSello(new SgArchivo());
                }
                FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getSftFirmaSello(), tmpBasePath);
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE,
                        Mensajes.obtenerMensaje(Mensajes.ERROR_DIMENSIONES_IMAGENES) + " " + configuracionArchivoAncho.getConValor() + "x" + configuracionArchivoAlto.getConValor(), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public boolean validateImageDimensions(byte[] bytes) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            return bufferedImage.getHeight() == Integer.valueOf(configuracionArchivoAlto.getConValor()) && bufferedImage.getWidth() == Integer.valueOf(configuracionArchivoAncho.getConValor());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return false;
    }

    public void eliminarFotoFirma() {
        this.entidadEnEdicion.setSftFirmaSello(null);
    }

    public void agregar() {
       // limpiarCombos();
        entidadEnEdicion = new SgSelloFirmaTitular();
    }

    public void actualizar(SgSelloFirmaTitular var) {
        try {
            entidadEnEdicion = var;
            comboCargoTitular.setSelectedT(entidadEnEdicion.getSftCargoTitular());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setSftCargoTitular(comboCargoTitular.getSelectedT() != null ? comboCargoTitular.getSelectedT() : null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSftPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSelloFirmaTitular getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSelloFirmaTitular filtro) {
        this.filtro = filtro;
    }

    public SgSelloFirmaTitular getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSelloFirmaTitular entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgSelloFirmaTitular> getHistorialSelloFirmaTitular() {
        return historialSelloFirmaTitular;
    }

    public void setHistorialSelloFirmaTitular(List<SgSelloFirmaTitular> historialSelloFirmaTitular) {
        this.historialSelloFirmaTitular = historialSelloFirmaTitular;
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

    public LazySelloFirmaTitularDataModel getSelloFirmaTitularLazyModel() {
        return selloFirmaTitularLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySelloFirmaTitularDataModel selloFirmaTitularLazyModel) {
        this.selloFirmaTitularLazyModel = selloFirmaTitularLazyModel;
    }

    public SofisComboG<SgCargoTitular> getComboCargoTitular() {
        return comboCargoTitular;
    }

    public void setComboCargoTitular(SofisComboG<SgCargoTitular> comboCargoTitular) {
        this.comboCargoTitular = comboCargoTitular;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgConfiguracion getConfiguracionArchivoAlto() {
        return configuracionArchivoAlto;
    }

    public void setConfiguracionArchivoAlto(SgConfiguracion configuracionArchivoAlto) {
        this.configuracionArchivoAlto = configuracionArchivoAlto;
    }

    public SgConfiguracion getConfiguracionArchivoAncho() {
        return configuracionArchivoAncho;
    }

    public void setConfiguracionArchivoAncho(SgConfiguracion configuracionArchivoAncho) {
        this.configuracionArchivoAncho = configuracionArchivoAncho;
    }

    public SgConfiguracion getConfiguracionArchivoTipo() {
        return configuracionArchivoTipo;
    }

    public void setConfiguracionArchivoTipo(SgConfiguracion configuracionArchivoTipo) {
        this.configuracionArchivoTipo = configuracionArchivoTipo;
    }

    public Boolean getAutentica() {
        return autentica;
    }

    public void setAutentica(Boolean autentica) {
        this.autentica = autentica;
    }

}

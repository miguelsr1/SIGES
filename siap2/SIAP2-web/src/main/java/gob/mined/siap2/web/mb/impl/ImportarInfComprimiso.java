/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import com.mined.siap2.to.CompromisoArchivoTO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.InfCompromiso;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.InformacionCompromisoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "importarInfCompromiso")
public class ImportarInfComprimiso implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    InformacionCompromisoDelegate informacionCompromisoDelegate;
    @Inject
    TextMB textMB;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    UsuarioInfo userInfo;
    @Inject
    ArchivoDelegate archivoDelegate;

    private InfCompromiso objeto;
    private String idCompromiso;
    private List<SelectItem> nombresPlanillas = new ArrayList<>();

    private UploadedFile uploadedFile;



 

    @PostConstruct
    public void init() {
        cargarNombresPlanillas();
    }


    /**
     * Se utiliza para levantar la planilla de compromisos
     * @param event 
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {
            objeto = new InfCompromiso();
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                objeto.setTempUploadedFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Trae los nombres de planillas cargadas
     */
    private void cargarNombresPlanillas(){
        nombresPlanillas.clear();
        for(CompromisoArchivoTO to:informacionCompromisoDelegate.obtenerArchivos()){
            nombresPlanillas.add(new SelectItem(to.getIdCompromiso(),to.getNombreArchivo()));
        }
    }

    /**
     * Guarda la planilla
     */
    public void guardarArchivo() {
        try {
            if (objeto == null || objeto.getTempUploadedFile() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_ARCHIVO);
                throw b;
            }

            informacionCompromisoDelegate.saveArchivo(objeto);
            objeto = null;
            cargarNombresPlanillas();
            jSFUtils.agregarInfo("Se levantó correctamente el archivo");

        } catch (GeneralException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    

// <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public String getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(String idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public List<SelectItem> getNombresPlanillas() {
        return nombresPlanillas;
    }

    public void setNombresPlanillas(List<SelectItem> nombresPlanillas) {
        this.nombresPlanillas = nombresPlanillas;
    }
    
    

    public UsuarioInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UsuarioInfo userInfo) {
        this.userInfo = userInfo;
    }

    public InfCompromiso getObjeto() {
        return objeto;
    }

    public void setObjeto(InfCompromiso objeto) {
        this.objeto = objeto;
    }



    public UsuarioDelegate getUsuarioDelegate() {
        return usuarioDelegate;
    }

    public void setUsuarioDelegate(UsuarioDelegate usuarioDelegate) {
        this.usuarioDelegate = usuarioDelegate;
    }


    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    // </editor-fold>
}

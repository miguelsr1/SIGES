/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
@DependsOn("SessionBean")
public class UsuarioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UsuarioBean.class.getName());

    @Inject
    private UsuarioRestClient restClient;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private SessionBean sessionBean;

    private SgUsuario entidadEnEdicion;
    private Boolean soloLectura = Boolean.TRUE;

    public UsuarioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            entidadEnEdicion = restClient.obtenerPorCodigo(sessionBean.getUser().getName());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    @PreDestroy
    public void preDestroy() {
        if (entidadEnEdicion != null && entidadEnEdicion.getUsuFoto() != null && !StringUtils.isBlank(entidadEnEdicion.getUsuFoto().getAchTmpPath())) {
            Path tmpFile = Paths.get(entidadEnEdicion.getUsuFoto().getAchTmpPath());
            if (tmpFile != null && Files.exists(tmpFile)) {
                try {
                    Files.delete(tmpFile);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getUsuFoto() == null) {
            entidadEnEdicion.setUsuFoto(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getUsuFoto(), tmpBasePath);
    }

    public void guardar() {
//        try {
//            SgActualizarPerfil perfil = new SgActualizarPerfil();
//            perfil.setEmail(entidadEnEdicion.getUsuEmail());
//            perfil.setNombre(entidadEnEdicion.getUsuNombre());
//            entidadEnEdicion = restClient.actualizarMiPerfil(perfil);
//            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
//        } catch (BusinessException be) {
//            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//        }
    }

    public SgUsuario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgUsuario entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}

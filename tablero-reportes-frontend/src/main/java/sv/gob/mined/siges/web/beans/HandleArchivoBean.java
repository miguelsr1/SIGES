/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEstArchivo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ArchivoRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ApplicationScoped
public class HandleArchivoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HandleArchivoBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private ArchivoRestClient archivoRestClient;

    public HandleArchivoBean() {
    }

    public void subirArchivoTmp(UploadedFile file, SgEstArchivo archivo) {
        FileUploadUtils.handleFileUpload(file, archivo, tmpBasePath);
    }

    public StreamedContent descargarArchivo(Long pk) throws IOException {
        try {
            SgEstArchivo arch = archivoRestClient.obtenerPorId(pk);
            return FileUploadUtils.handleFileDownload(arch, basePath);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public StreamedContent descargarArchivo(SgEstArchivo arch) throws IOException {
        try {
            if (arch.getAchPk() != null) {
                return FileUploadUtils.handleFileDownload(arch, basePath);
            } else {
                return FileUploadUtils.handleFileDownload(arch, tmpBasePath);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

}

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
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgRhPagina;
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
    @ConfigProperty(name = "files.historico.path")
    private String historicoPath;       

    @Inject
    private ArchivoRestClient archivoRestClient;

    public HandleArchivoBean() {
    }

    public void subirArchivoTmp(UploadedFile file, SgArchivo archivo) {
        FileUploadUtils.handleFileUpload(file, archivo, tmpBasePath);
    }


    public StreamedContent descargarArchivo(SgArchivo arch) throws IOException {
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
    
    //Archivos almacenados en pc usuario
    public String descargarHistorico(SgRhPagina pagina) throws IOException {
        try {
            if (pagina.getPagRuta() != null && pagina.getPagNombreLibro() != null){
                return "file:///" + historicoPath + pagina.getPagRuta().substring(1)+pagina.getPagNombreLibro();
            }
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }   

}

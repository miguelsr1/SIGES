/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.rest.ArchivoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ApplicationScoped
public class DescargaArchivoBean implements Serializable {
  
    private static final Logger LOGGER = Logger.getLogger(PlantillaBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;
    
    @Inject
    private ArchivoRestClient archivoRestClient;

    public DescargaArchivoBean() {
    }

    public StreamedContent descargarArchivo(Long pk) throws IOException {
       try{
            SgArchivo arch=archivoRestClient.obtenerPorId(pk);
        
            if (arch.getAchPk()!= null) {
                InputStream stream;

                stream = getFile(pk);

                if (stream != null) {
                    StreamedContent file = new DefaultStreamedContent(stream, arch.getAchContentType(), arch.getAchNombre() + "." + arch.getAchExt());
                    return file;
                }

                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

        return null;
        
    }
    
     public InputStream getFile(Long pk) throws Exception {
        try {
            return new BufferedInputStream(new FileInputStream(basePath + SofisFileUtils.getPathFromPk(pk)));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}

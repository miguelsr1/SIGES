/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.mensajes.Mensajes;

/**
 *
 * @author USUARIO
 */
public class FileUploadUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUploadUtils.class.getName());

    public static void handleFileUpload(UploadedFile file, SgArchivo archivo, String tmpBasePath) {
        if (file != null) {
            if (file.getFileName() != null && file.getFileName().length() < 2000) {
                try {
                    //Si se vuelve a subir un archivo, se borra el anterior.
                    if (archivo.getAchTmpPath() != null) {
                        Path tmpFile = Paths.get(archivo.getAchTmpPath());
                        if (tmpFile != null && Files.exists(tmpFile)) {
                            try {
                                Files.delete(tmpFile);
                            } catch (Exception ex) {
                                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                            }
                        }
                    }
                    String nombreCompleto = file.getFileName();
                    String nombre = FilenameUtils.getBaseName(nombreCompleto);
                    String extension = FilenameUtils.getExtension(nombreCompleto);
                    String contentT = file.getContentType();
                    archivo.setAchExt(extension);
                    archivo.setAchNombre(nombre);
                    archivo.setAchContentType(contentT);
                    Path folder = Paths.get(tmpBasePath);
                    Path tmpFile = Files.createTempFile(folder, null, "." + extension);
                    try (InputStream input = file.getInputstream()) {
                        Files.copy(input, tmpFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    archivo.setAchTmpPath(tmpFile.getFileName().toString());
                    archivo.setAchShowTmpFile(Boolean.TRUE);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.DATOS_INCORRECTOS), "");
            }
        }
    }

    public static StreamedContent handleFileDownload(SgArchivo archivo, String basePath) throws Exception {
        InputStream stream;
        if (archivo.getAchPk() != null) {
            stream = new BufferedInputStream(new FileInputStream(basePath + SofisFileUtils.getPathFromPk(archivo.getAchPk())));
        } else {
            stream = new BufferedInputStream(new FileInputStream(basePath + archivo.getAchTmpPath()));            
        }
        if (stream != null) {
            StreamedContent file = new DefaultStreamedContent(stream, archivo.getAchContentType(), archivo.getAchNombre() + "." + archivo.getAchExt());
            return file;
        }
        return null;
    }   

}

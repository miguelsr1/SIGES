/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ArchivoRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ApplicationScoped
/**
 * Bean para la gestión de los archivos.
 */
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

    /**
     * Sube Archivo a la carpeta temp
     *
     * @param file
     * @param archivo
     */
    public void subirArchivoTmp(UploadedFile file, SgArchivo archivo) {
        FileUploadUtils.handleFileUpload(file, archivo, tmpBasePath);
    }

    public StreamedContent plantillaCalificacion() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla para calificaciones.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla para calificaciones.xlsx");
    }

    public StreamedContent plantillaAsistencia() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla para control de asistencias.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla para control de asistencias.xlsx");
    }

    public StreamedContent plantillaOtrosIngresos() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla otros ingresos.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla otros ingresos.xlsx");
    }

    public StreamedContent plantillaEgresos() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla egresos.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla egresos.xlsx");
    }

    /**
     * Descarga el archivo por el id
     *
     * @param pk
     * @return
     * @throws IOException
     */
    public StreamedContent descargarArchivo(Long pk) throws IOException {
        try {
            SgArchivo arch = archivoRestClient.obtenerPorId(pk);
            return FileUploadUtils.handleFileDownload(arch, basePath);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Descarga el archivo por un objeto
     *
     * @param arch
     * @return
     * @throws IOException
     */
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

}

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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ArchivoRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
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

    @Inject
    private PlantillaRestClient plantillaRestClient;

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

    public StreamedContent plantillaCalificacion() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla para calificaciones.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla para calificaciones.xlsx");
    }

    public StreamedContent plantillaAsistencia() {
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/plantillas/Plantilla para control de asistencias.xlsx");
        return new DefaultStreamedContent(stream, "application/xls", "Plantilla para control de asistencias.xlsx");
    }

    public StreamedContent plantillaImportacionMatricula() {
        try {
            SgPlantilla plantilla = plantillaRestClient.obtenerPorCodigo(Constantes.PLANTILLA_EXCEL_IMPORTACION_MATRICULAS);
            if (plantilla == null || plantilla.getPlaArchivo() == null) {
                BusinessException be = new BusinessException();
                be.addError("Plantilla " + Constantes.PLANTILLA_EXCEL_IMPORTACION_MATRICULAS + " inexistente.");
                throw be;
            }
            
            return FileUploadUtils.handleFileDownload(plantilla.getPlaArchivo(), basePath);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajesError(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

}

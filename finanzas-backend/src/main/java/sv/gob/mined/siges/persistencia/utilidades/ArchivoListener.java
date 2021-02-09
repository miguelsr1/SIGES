/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.utils.SofisFileUtils;

/**
 * Listener de la entidad de archivo, para la actualización del sistema de
 * archivos.
 *
 * @author jgiron
 */
public class ArchivoListener {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    //TODO: investigar mutuo exclusión
    @PostPersist
    @PostUpdate
    /**
     * Acción que actualiza el sistema de archivos al guardar la entidad.
     */
    public void postSave(SgArchivo arch) throws Exception {
        Path tmpPath = Paths.get(tmpBasePath + arch.getAchTmpPath());
        if (Files.exists(tmpPath)) {
            Path newPath = Paths.get(basePath + SofisFileUtils.getPathFromPk(arch.getAchPk()));
            if (!Files.exists(newPath.getParent())) {
                Files.createDirectories(newPath.getParent());
            }
            Files.move(tmpPath, newPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @PostRemove
    /**
     * Acción que actualiza el sistema de archivo al eliminar un archivo.
     */
    public void postRemove(SgArchivo arch) throws Exception {
        Path file = Paths.get(basePath + arch.getAchPk());
        if (Files.exists(file)) {
            Files.delete(file);
        }
    }
}

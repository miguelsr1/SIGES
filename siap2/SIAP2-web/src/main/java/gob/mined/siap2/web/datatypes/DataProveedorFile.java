/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sofis Solutions
 */
public class DataProveedorFile {

    private ProcesoAdquisicionProveedor adquisicionProveedor;
    private UploadedFile uploadFile;
    private String archivoNombre;
    private boolean invitado;
    private Archivo archivo;
    
    private int index;

    public ProcesoAdquisicionProveedor getAdquisicionProveedor() {
        return adquisicionProveedor;
    }

    public void setAdquisicionProveedor(ProcesoAdquisicionProveedor proveedor) {
        this.adquisicionProveedor = proveedor;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getArchivoNombre() {
        return archivoNombre;
    }

    public void setArchivoNombre(String archivoNombre) {
        this.archivoNombre = archivoNombre;
    }

    public boolean isInvitado() {
        return invitado;
    }

    public void setInvitado(boolean invitado) {
        this.invitado = invitado;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }
    
}

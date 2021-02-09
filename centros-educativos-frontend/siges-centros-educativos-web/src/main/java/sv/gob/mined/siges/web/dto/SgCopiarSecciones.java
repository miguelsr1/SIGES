/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Sofis Solutions
 */
public class SgCopiarSecciones implements Serializable {
    
    private List<SgSeccion> secciones;
    private SgAnioLectivo nuevoAnio;

    public List<SgSeccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<SgSeccion> secciones) {
        this.secciones = secciones;
    }

    public SgAnioLectivo getNuevoAnio() {
        return nuevoAnio;
    }

    public void setNuevoAnio(SgAnioLectivo nuevoAnio) {
        this.nuevoAnio = nuevoAnio;
    }

  
    
    
}

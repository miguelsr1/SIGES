/*
 * SIGES
 * Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.caux;

import java.util.List;
import sv.gob.mined.siges.web.dto.SgSeccion;

/**
 *
 * @author Sofis S
 */
public class AsistenciaSeccion {

    private SgSeccion seccion;
    private List<String> asistencias;

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    public List<String> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<String> asistencias) {
        this.asistencias = asistencias;
    }

}

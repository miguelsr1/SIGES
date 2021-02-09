package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;

/**
 *
 * @author fabricio
 */
public class SgPeriodoCalificacionEstCal implements Serializable {
    
    
    private List<SgEstudiante> pecEstudiantes;
    private List<SgCalificacionEstudiante> pecCalificaciones;
    private List<SgRangoFecha> pecPeriodoRangosFecha;

    public List<SgEstudiante> getPecEstudiantes() {
        return pecEstudiantes;
    }

    public void setPecEstudiantes(List<SgEstudiante> pecEstudiantes) {
        this.pecEstudiantes = pecEstudiantes;
    }

    public List<SgCalificacionEstudiante> getPecCalificaciones() {
        return pecCalificaciones;
    }

    public void setPecCalificaciones(List<SgCalificacionEstudiante> pecCalificaciones) {
        this.pecCalificaciones = pecCalificaciones;
    }

    public List<SgRangoFecha> getPecPeriodoRangosFecha() {
        return pecPeriodoRangosFecha;
    }

    public void setPecPeriodoRangosFecha(List<SgRangoFecha> pecPeriodoRangosFecha) {
        this.pecPeriodoRangosFecha = pecPeriodoRangosFecha;
    }
    
    
}

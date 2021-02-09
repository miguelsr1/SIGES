/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.caux;

import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

/**
 *
 * @author Sofis Solutions
 */
public class FilaArchivoPAES {
    
    private Integer cpeCodigoExterno;
    private Long nie;
    private String calificacion;
    
    private SgCalificacion calificacionConceptual;
    
    private SgComponentePlanGrado componentePlanGrado;
    private SgEstudiante estudiante;

    public FilaArchivoPAES() {
        
    }

    public Integer getCpeCodigoExterno() {
        return cpeCodigoExterno;
    }

    public void setCpeCodigoExterno(Integer cpeCodigoExterno) {
        this.cpeCodigoExterno = cpeCodigoExterno;
    }

    

    public Long getNie() {
        return nie;
    }

    public void setNie(Long nie) {
        this.nie = nie;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public SgCalificacion getCalificacionConceptual() {
        return calificacionConceptual;
    }

    public void setCalificacionConceptual(SgCalificacion calificacionConceptual) {
        this.calificacionConceptual = calificacionConceptual;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public SgEstudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(SgEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    
    
    
}


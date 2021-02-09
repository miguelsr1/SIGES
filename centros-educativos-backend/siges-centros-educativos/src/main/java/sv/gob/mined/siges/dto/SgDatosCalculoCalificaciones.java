/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;



public class SgDatosCalculoCalificaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    private SgSeccion seccion; 
    
    private SgComponentePlanGrado componentePlanGrado;
    
    private Boolean esCalculoPormocion;
    
    private Long periodoCalificacionPk;
    
    private Long estudiantePk; //Cuando el cálculo es para un único estudiante

    public SgDatosCalculoCalificaciones() {
    }

    public SgDatosCalculoCalificaciones(Long conPk) {
      
    }

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public Long getPeriodoCalificacionPk() {
        return periodoCalificacionPk;
    }

    public void setPeriodoCalificacionPk(Long periodoCalificacionPk) {
        this.periodoCalificacionPk = periodoCalificacionPk;
    }
    public Boolean getEsCalculoPormocion() {
        return esCalculoPormocion;
    }

    public void setEsCalculoPormocion(Boolean esCalculoPormocion) {
        this.esCalculoPormocion = esCalculoPormocion;
    }

    public Long getEstudiantePk() {
        return estudiantePk;
    }

    public void setEstudiantePk(Long estudiantePk) {
        this.estudiantePk = estudiantePk;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.seccion);
        hash = 41 * hash + Objects.hashCode(this.componentePlanGrado);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgDatosCalculoCalificaciones other = (SgDatosCalculoCalificaciones) obj;
        if (!Objects.equals(this.seccion, other.seccion)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanGrado, other.componentePlanGrado)) {
            return false;
        }
        return true;
    }
    
    

}

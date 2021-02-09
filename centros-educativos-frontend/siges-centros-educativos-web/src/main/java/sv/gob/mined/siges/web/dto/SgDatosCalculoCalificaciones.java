/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.Objects;



public class SgDatosCalculoCalificaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private SgSeccion seccion; 
    
    private SgComponentePlanGrado componentePlanGrado;
    
    private Long periodoCalificacionPk;

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

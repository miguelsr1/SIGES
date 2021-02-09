/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.caux;

import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
public class KeyCalificacion {
    
    public final Long componentePlanEstudio;
    public final Long seccion;
    public final Long rangoFecha;

    public KeyCalificacion(Long componentePlanEstudio,Long seccion, Long rangoFecha) {
        this.componentePlanEstudio = componentePlanEstudio;
        this.seccion = seccion;
        this.rangoFecha = rangoFecha;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.componentePlanEstudio);
        hash = 59 * hash + Objects.hashCode(this.seccion);
        hash = 59 * hash + Objects.hashCode(this.rangoFecha);
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
        final KeyCalificacion other = (KeyCalificacion) obj;
        if (!Objects.equals(this.componentePlanEstudio, other.componentePlanEstudio)) {
            return false;
        }
        if (!Objects.equals(this.seccion, other.seccion)) {
            return false;
        }
        if (!Objects.equals(this.rangoFecha, other.rangoFecha)) {
            return false;
        }
        return true;
    }

   
    
}

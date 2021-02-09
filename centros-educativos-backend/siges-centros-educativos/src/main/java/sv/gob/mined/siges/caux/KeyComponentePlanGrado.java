/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.caux;

import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
public class KeyComponentePlanGrado {
    
    public final Long componentePlanEstudio;
    public final Long grado;
    public final Long planEstudio;

    public KeyComponentePlanGrado(Long componentePlanEstudio,Long grado, Long planEstudio) {
        this.componentePlanEstudio = componentePlanEstudio;
        this.grado = grado;
        this.planEstudio = planEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.componentePlanEstudio);
        hash = 67 * hash + Objects.hashCode(this.grado);
        hash = 67 * hash + Objects.hashCode(this.planEstudio);
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
        final KeyComponentePlanGrado other = (KeyComponentePlanGrado) obj;
        if (!Objects.equals(this.componentePlanEstudio, other.componentePlanEstudio)) {
            return false;
        }
        if (!Objects.equals(this.grado, other.grado)) {
            return false;
        }
        if (!Objects.equals(this.planEstudio, other.planEstudio)) {
            return false;
        }
        return true;
    }

    

   
    
}

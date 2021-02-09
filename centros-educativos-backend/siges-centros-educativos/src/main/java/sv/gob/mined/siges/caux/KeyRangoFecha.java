/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.caux;

import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
public class KeyRangoFecha {
    
    public final String codigo;
    public final Integer cantidadPeriodo;
    public final Long modalidadAtencionPk;
    public final Long modalidadEducativaPk;

    public KeyRangoFecha(String codigo,Integer cantidadPeriodo,Long modalidadAtencionPk,Long modalidadEducativaPk) {
        this.codigo = codigo;
        this.cantidadPeriodo = cantidadPeriodo;
        this.modalidadAtencionPk = modalidadAtencionPk;
        this.modalidadEducativaPk = modalidadEducativaPk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.codigo);
        hash = 47 * hash + Objects.hashCode(this.cantidadPeriodo);
        hash = 47 * hash + Objects.hashCode(this.modalidadAtencionPk);
        hash = 47 * hash + Objects.hashCode(this.modalidadEducativaPk);
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
        final KeyRangoFecha other = (KeyRangoFecha) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.cantidadPeriodo, other.cantidadPeriodo)) {
            return false;
        }
        if (!Objects.equals(this.modalidadAtencionPk, other.modalidadAtencionPk)) {
            return false;
        }
        if (!Objects.equals(this.modalidadEducativaPk, other.modalidadEducativaPk)) {
            return false;
        }
        return true;
    }   

   
    
}

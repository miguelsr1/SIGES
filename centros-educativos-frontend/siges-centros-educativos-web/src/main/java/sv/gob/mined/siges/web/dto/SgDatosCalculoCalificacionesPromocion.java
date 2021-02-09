/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.Objects;



public class SgDatosCalculoCalificacionesPromocion implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private SgSeccion seccion; 
    
    private Boolean esAsnicronica;
    
   
    public SgDatosCalculoCalificacionesPromocion() {
    }

    public SgDatosCalculoCalificacionesPromocion(Long conPk) {
      
    }

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    public Boolean getEsAsnicronica() {
        return esAsnicronica;
    }

    public void setEsAsnicronica(Boolean esAsnicronica) {
        this.esAsnicronica = esAsnicronica;
    }


    @Override
    public int hashCode() {
        int hash = 7;
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
        final SgDatosCalculoCalificacionesPromocion other = (SgDatosCalculoCalificacionesPromocion) obj;
        if (!Objects.equals(this.seccion, other.seccion)) {
            return false;
        }
        if (!Objects.equals(this.esAsnicronica, other.esAsnicronica)) {
            return false;
        }
        return true;
    }

    
    
    

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;




public class SgEstudianteErrorCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    
    private SgEstudiante eecEstudiante;
    
    private String eecError;
          
  
    public SgEstudianteErrorCalificacion() {
    }

    public SgEstudiante getEecEstudiante() {
        return eecEstudiante;
    }

    public void setEecEstudiante(SgEstudiante eecEstudiante) {
        this.eecEstudiante = eecEstudiante;
    }

    public String getEecError() {
        return eecError;
    }

    public void setEecError(String eecError) {
        this.eecError = eecError;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.eecEstudiante);
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
        final SgEstudianteErrorCalificacion other = (SgEstudianteErrorCalificacion) obj;
        if (!Objects.equals(this.eecEstudiante, other.eecEstudiante)) {
            return false;
        }
        return true;
    }
    
        

    
    

    

}

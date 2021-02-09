/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;

/**
 *
 * @author usuario
 */
public class SgCalificacionPSNG implements Serializable {
    
    
    private Long gradoFk;
    private SgEstudiante estudianteFk;
    private Long planEstudioFk;
    private List<SgCalificacionEstudiantePSNG> calificacionesEstudiante;
    
    

    public SgCalificacionPSNG() {
    }

    public Long getGradoFk() {
        return gradoFk;
    }

    public void setGradoFk(Long gradoFk) {
        this.gradoFk = gradoFk;
    }

    public SgEstudiante getEstudianteFk() {
        return estudianteFk;
    }

    public void setEstudianteFk(SgEstudiante estudianteFk) {
        this.estudianteFk = estudianteFk;
    }

    public List<SgCalificacionEstudiantePSNG> getCalificacionesEstudiante() {
        return calificacionesEstudiante;
    }

    public void setCalificacionesEstudiante(List<SgCalificacionEstudiantePSNG> calificacionesEstudiante) {
        this.calificacionesEstudiante = calificacionesEstudiante;
    }

    public Long getPlanEstudioFk() {
        return planEstudioFk;
    }

    public void setPlanEstudioFk(Long planEstudioFk) {
        this.planEstudioFk = planEstudioFk;
    }

  

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.gradoFk);
        hash = 97 * hash + Objects.hashCode(this.estudianteFk);
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
        final SgCalificacionPSNG other = (SgCalificacionPSNG) obj;      
        if (!Objects.equals(this.gradoFk, other.gradoFk)) {
            return false;
        }
        if (!Objects.equals(this.estudianteFk, other.estudianteFk)) {
            return false;
        }
        return true;
    }

    
   
    

   
}

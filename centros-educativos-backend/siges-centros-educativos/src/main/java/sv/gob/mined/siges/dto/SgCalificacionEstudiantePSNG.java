/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author usuario
 */
public class SgCalificacionEstudiantePSNG implements Serializable {    
    
    
    private Long componetePlanGradoPk;
    private String  calificacion;
    private LocalDate fechaCalificacion;
    
    

    public SgCalificacionEstudiantePSNG() {
    }

    public Long getComponetePlanGradoPk() {
        return componetePlanGradoPk;
    }

    public void setComponetePlanGradoPk(Long componetePlanGradoPk) {
        this.componetePlanGradoPk = componetePlanGradoPk;
    }   

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDate getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDate fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.calificacion);
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
        final SgCalificacionEstudiantePSNG other = (SgCalificacionEstudiantePSNG) obj;
        if (!Objects.equals(this.calificacion, other.calificacion)) {
            return false;
        }
        return true;
    }

    
   
    

   
}

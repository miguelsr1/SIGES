/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author usuario
 */
public class SgGradoPlan implements Serializable {
    
    
    private Long grado;
    private Long planEstudio;
    private Long opcion;
    private Long programaEducativo;
    
    

    public SgGradoPlan() {
    }

    public Long getGrado() {
        return grado;
    }

    public void setGrado(Long grado) {
        this.grado = grado;
    }

    public Long getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(Long planEstudio) {
        this.planEstudio = planEstudio;
    }

    public Long getOpcion() {
        return opcion;
    }

    public void setOpcion(Long opcion) {
        this.opcion = opcion;
    }

    public Long getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(Long programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.grado);
        hash = 23 * hash + Objects.hashCode(this.planEstudio);
        hash = 23 * hash + Objects.hashCode(this.opcion);
        hash = 23 * hash + Objects.hashCode(this.programaEducativo);
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
        final SgGradoPlan other = (SgGradoPlan) obj;
        if (!Objects.equals(this.grado, other.grado)) {
            return false;
        }
        if (!Objects.equals(this.planEstudio, other.planEstudio)) {
            return false;
        }
        if (!Objects.equals(this.opcion, other.opcion)) {
            return false;
        }
        if (!Objects.equals(this.programaEducativo, other.programaEducativo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgGradoPlan{" + "grado=" + grado + ", planEstudio=" + planEstudio + ", opcion=" + opcion + ", programaEducativo=" + programaEducativo + '}';
    }

   
    
    
    

   
}

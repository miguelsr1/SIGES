/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author usuario
 */
public class SgCalificacionDiplomadoAux implements Serializable {
    
    
    private Long sedeFk;
    private Long moduloDiplomadoFk;
    private Long diplomadoFk;
    private Long anioLectivoFk;
    
    

    public SgCalificacionDiplomadoAux() {
    }

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Long getModuloDiplomadoFk() {
        return moduloDiplomadoFk;
    }

    public void setModuloDiplomadoFk(Long moduloDiplomadoFk) {
        this.moduloDiplomadoFk = moduloDiplomadoFk;
    }

    public Long getAnioLectivoFk() {
        return anioLectivoFk;
    }

    public void setAnioLectivoFk(Long anioLectivoFk) {
        this.anioLectivoFk = anioLectivoFk;
    }

    public Long getDiplomadoFk() {
        return diplomadoFk;
    }

    public void setDiplomadoFk(Long diplomadoFk) {
        this.diplomadoFk = diplomadoFk;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.sedeFk);
        hash = 41 * hash + Objects.hashCode(this.moduloDiplomadoFk);
        hash = 41 * hash + Objects.hashCode(this.anioLectivoFk);
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
        final SgCalificacionDiplomadoAux other = (SgCalificacionDiplomadoAux) obj;
        if (!Objects.equals(this.sedeFk, other.sedeFk)) {
            return false;
        }
        if (!Objects.equals(this.moduloDiplomadoFk, other.moduloDiplomadoFk)) {
            return false;
        }
        if (!Objects.equals(this.anioLectivoFk, other.anioLectivoFk)) {
            return false;
        }
        return true;
    }

 
   
   
    
    
    

   
}

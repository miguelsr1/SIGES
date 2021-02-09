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
public class SgAreaCombo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String acCodigo;
    
    private String acNombre;
    
    private Boolean esAsignaturaModulo;
    
    public SgAreaCombo(){
        
    }

    public String getAcCodigo() {
        return acCodigo;
    }

    public void setAcCodigo(String acCodigo) {
        this.acCodigo = acCodigo;
    }

    public String getAcNombre() {
        return acNombre;
    }

    public void setAcNombre(String acNombre) {
        this.acNombre = acNombre;
    }

    public Boolean getEsAsignaturaModulo() {
        return esAsignaturaModulo;
    }

    public void setEsAsignaturaModulo(Boolean esAsignaturaModulo) {
        this.esAsignaturaModulo = esAsignaturaModulo;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.acCodigo);
        hash = 67 * hash + Objects.hashCode(this.acNombre);
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
        final SgAreaCombo other = (SgAreaCombo) obj;
        if (!Objects.equals(this.acCodigo, other.acCodigo)) {
            return false;
        }
        if (!Objects.equals(this.acNombre, other.acNombre)) {
            return false;
        }
        return true;
    }
    
    
}

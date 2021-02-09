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
public class SgDatoDashboard implements Serializable {    
    
    
    private Long elementoPk;
    private Long  cantidad;
    
    

    public SgDatoDashboard() {
    }

    public Long getElementoPk() {
        return elementoPk;
    }

    public void setElementoPk(Long elementoPk) {
        this.elementoPk = elementoPk;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.elementoPk);
        hash = 53 * hash + Objects.hashCode(this.cantidad);
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
        final SgDatoDashboard other = (SgDatoDashboard) obj;
        if (!Objects.equals(this.elementoPk, other.elementoPk)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        return true;
    }



    
   
    

   
}

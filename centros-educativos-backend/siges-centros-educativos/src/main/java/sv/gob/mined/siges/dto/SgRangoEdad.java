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
public class SgRangoEdad implements Serializable {    
    
    private Long pk;
    private Integer edadDesde;
    private Integer  edadHasta;
    private Long cantidad;
    

    public SgRangoEdad() {
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Integer getEdadDesde() {
        return edadDesde;
    }

    public void setEdadDesde(Integer edadDesde) {
        this.edadDesde = edadDesde;
    }

    public Integer getEdadHasta() {
        return edadHasta;
    }

    public void setEdadHasta(Integer edadHasta) {
        this.edadHasta = edadHasta;
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
        hash = 59 * hash + Objects.hashCode(this.pk);
        hash = 59 * hash + Objects.hashCode(this.edadDesde);
        hash = 59 * hash + Objects.hashCode(this.edadHasta);
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
        final SgRangoEdad other = (SgRangoEdad) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        if (!Objects.equals(this.edadDesde, other.edadDesde)) {
            return false;
        }
        if (!Objects.equals(this.edadHasta, other.edadHasta)) {
            return false;
        }
        return true;
    }

  

    
   
    

   
}

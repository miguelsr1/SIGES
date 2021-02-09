/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 * Clase auxiliar para el reporte de Cuentas de ingresos y egresos
 *
 * @author sofis-jgiron
 */
public class LibroCajaChica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cajaPk;
    private Integer anio;
    private Long subcomponetePk;

    public Long getCajaPk() {
        return cajaPk;
    }

    public void setCajaPk(Long cajaPk) {
        this.cajaPk = cajaPk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getSubcomponetePk() {
        return subcomponetePk;
    }

    public void setSubcomponetePk(Long subcomponetePk) {
        this.subcomponetePk = subcomponetePk;
    }

}

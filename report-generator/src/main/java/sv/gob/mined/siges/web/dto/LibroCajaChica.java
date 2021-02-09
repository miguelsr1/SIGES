/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;

/**
 *
 * @author sofis-iquezada
 */
public class LibroCajaChica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cajaPk;
    private Integer anio;

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

}

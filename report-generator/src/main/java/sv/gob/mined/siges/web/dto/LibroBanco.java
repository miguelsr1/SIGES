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
public class LibroBanco implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cuentaPk;
    private Integer anio;
    private Long componentePk;
    private Long subComponentePk;

    public Long getCuentaPk() {
        return cuentaPk;
    }

    public void setCuentaPk(Long cuentaPk) {
        this.cuentaPk = cuentaPk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(Long componentePk) {
        this.componentePk = componentePk;
    }

    public Long getSubComponentePk() {
        return subComponentePk;
    }

    public void setSubComponentePk(Long subComponentePk) {
        this.subComponentePk = subComponentePk;
    }

}

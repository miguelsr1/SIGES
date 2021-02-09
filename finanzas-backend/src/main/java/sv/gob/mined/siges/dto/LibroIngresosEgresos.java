/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author sofis-iquezada
 */
public class LibroIngresosEgresos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sedePk;
    private Integer anio;
    private Long movTransferenciaPk;
    private Long componentePk;
    private Long subComponentePk;

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getMovTransferenciaPk() {
        return movTransferenciaPk;
    }

    public void setMovTransferenciaPk(Long movTransferenciaPk) {
        this.movTransferenciaPk = movTransferenciaPk;
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

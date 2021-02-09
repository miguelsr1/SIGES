/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;



/**
 * Clase auxiliar para guardar los desembolsos de Dirección Dep.
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EgresosArchivo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long detallePaePk;
    private String descripcion;
    private Long movFuenteIngresoPk;
    private Long areaInversionPk;
    private Long subAreaInversionPk;
    private BigDecimal montoFormulado;

    public Long getDetallePaePk() {
        return detallePaePk;
    }

    public void setDetallePaePk(Long detallePaePk) {
        this.detallePaePk = detallePaePk;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getMovFuenteIngresoPk() {
        return movFuenteIngresoPk;
    }

    public void setMovFuenteIngresoPk(Long movFuenteIngresoPk) {
        this.movFuenteIngresoPk = movFuenteIngresoPk;
    }

    public Long getAreaInversionPk() {
        return areaInversionPk;
    }

    public void setAreaInversionPk(Long areaInversionPk) {
        this.areaInversionPk = areaInversionPk;
    }

    public Long getSubAreaInversionPk() {
        return subAreaInversionPk;
    }

    public void setSubAreaInversionPk(Long subAreaInversionPk) {
        this.subAreaInversionPk = subAreaInversionPk;
    }

    public BigDecimal getMontoFormulado() {
        return montoFormulado;
    }

    public void setMontoFormulado(BigDecimal montoFormulado) {
        this.montoFormulado = montoFormulado;
    }
    
    
    
   
    
    
    
    
}

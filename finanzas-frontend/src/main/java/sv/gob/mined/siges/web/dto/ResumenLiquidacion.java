/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Clase auxiliar para guardar los desembolsos de Dirección Dep.
 *
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumenLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
   
    private String detalle;
    private BigDecimal monto;

    public ResumenLiquidacion(String detalle, BigDecimal monto) {
        this.detalle = detalle;
        this.monto = monto;
    }
    
        
    
    
    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    

    // </editor-fold>

    
}

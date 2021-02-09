/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase auxiliar para guardar los movimientos desembolsos de Dirección Dep.
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesembolsoMovimiento implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Long desembolsoPk;
    private LocalDateTime fechaMov;

    public Long getDesembolsoPk() {
        return desembolsoPk;
    }

    public void setDesembolsoPk(Long desembolsoPk) {
        this.desembolsoPk = desembolsoPk;
    }

    public LocalDateTime getFechaMov() {
        return fechaMov;
    }

    public void setFechaMov(LocalDateTime fechaMov) {
        this.fechaMov = fechaMov;
    }
    
    
    
}

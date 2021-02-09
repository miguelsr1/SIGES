/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class SgCountAlertas implements Serializable  {
    
    private Long riesgoMuyAlto;
    private Long riesgoAlto;
    private Long riesgoMedio;
    private Long total;

    public SgCountAlertas() {
        this.riesgoMuyAlto = 0L;
        this.riesgoAlto = 0L;
        this.riesgoMedio = 0L;
        this.total = 0L;
    }
    
    

    public Long getRiesgoMuyAlto() {
        return riesgoMuyAlto;
    }

    public void setRiesgoMuyAlto(Long riesgoMuyAlto) {
        this.riesgoMuyAlto = riesgoMuyAlto;
    }

    public Long getRiesgoAlto() {
        return riesgoAlto;
    }

    public void setRiesgoAlto(Long riesgoAlto) {
        this.riesgoAlto = riesgoAlto;
    }

    public Long getRiesgoMedio() {
        return riesgoMedio;
    }

    public void setRiesgoMedio(Long riesgoMedio) {
        this.riesgoMedio = riesgoMedio;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    
    
    
    
    
    
}

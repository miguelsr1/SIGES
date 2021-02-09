package sv.gob.mined.siges.dto;

import java.io.Serializable;


public class SgGradoPlanOrigenDestino implements Serializable {
    
    private SgGradoPlan origen;
    private SgGradoPlan destino;
    private Boolean validarPlanEstudioDestino;
   

    public SgGradoPlanOrigenDestino() {
        this.origen = new SgGradoPlan();
        this.destino = new SgGradoPlan();
        this.validarPlanEstudioDestino = Boolean.TRUE;
    }

    public SgGradoPlan getOrigen() {
        return origen;
    }

    public void setOrigen(SgGradoPlan origen) {
        this.origen = origen;
    }

    public SgGradoPlan getDestino() {
        return destino;
    }

    public void setDestino(SgGradoPlan destino) {
        this.destino = destino;
    }

    public Boolean getValidarPlanEstudioDestino() {
        return validarPlanEstudioDestino;
    }

    public void setValidarPlanEstudioDestino(Boolean validarPlanEstudioDestino) {
        this.validarPlanEstudioDestino = validarPlanEstudioDestino;
    }
    
    


   
}

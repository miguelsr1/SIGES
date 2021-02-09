package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;

public class SgRegistroDiplomadosSede implements Serializable  {
    
    private Long sedePk;
    private String numTramite;
    private List<Long> diplomadosPk;

    
    public SgRegistroDiplomadosSede() {
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public List<Long> getDiplomadosPk() {
        return diplomadosPk;
    }

    public void setDiplomadosPk(List<Long> diplomadosPk) {
        this.diplomadosPk = diplomadosPk;
    }
 
    

    
}

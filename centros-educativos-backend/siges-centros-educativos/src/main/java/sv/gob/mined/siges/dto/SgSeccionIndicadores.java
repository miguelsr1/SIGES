package sv.gob.mined.siges.dto;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fabricio
 */
public class SgSeccionIndicadores {
    
    private Long seccionId;
    private Set<SgIndicador> indicadores;


    public SgSeccionIndicadores(Long seccionId) {
        this.seccionId = seccionId;
        this.indicadores = new HashSet<>();
    }
    
    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }

    public Set<SgIndicador> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<SgIndicador> indicadores) {
        this.indicadores = indicadores;
    }

    
      
}

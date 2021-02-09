package sv.gob.mined.siges.dto;

import java.io.Serializable;

/**
 *
 * @author fabricio
 */
public class SgAsistenciasInasistenciasEstudianteResponse implements Serializable {

    private Integer asistencias;
    private Integer inasistenciasJustificadas;
    private Integer inasistenciasInjustificadas;

    public SgAsistenciasInasistenciasEstudianteResponse() {
        this.asistencias = 0;
        this.inasistenciasJustificadas = 0;
        this.inasistenciasInjustificadas = 0;
    }
    
    

    public Integer getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Integer asistencias) {
        this.asistencias = asistencias;
    }

    public Integer getInasistenciasJustificadas() {
        return inasistenciasJustificadas;
    }

    public void setInasistenciasJustificadas(Integer inasistenciasJustificadas) {
        this.inasistenciasJustificadas = inasistenciasJustificadas;
    }

    public Integer getInasistenciasInjustificadas() {
        return inasistenciasInjustificadas;
    }

    public void setInasistenciasInjustificadas(Integer inasistenciasInjustificadas) {
        this.inasistenciasInjustificadas = inasistenciasInjustificadas;
    }
    
    

}

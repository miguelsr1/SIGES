/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.time.LocalDate;


/**
 *
 * @author Sofis Solutions
 */
public class SgModificarFechasMatricula implements Serializable {
    
    private LocalDate fechaIngreso;
    private LocalDate fechaHasta;
    private Long matPk;
    private Long estudiantePk;

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public Long getEstudiantePk() {
        return estudiantePk;
    }

    public void setEstudiantePk(Long estudiantePk) {
        this.estudiantePk = estudiantePk;
    }
    
    
    
     
}

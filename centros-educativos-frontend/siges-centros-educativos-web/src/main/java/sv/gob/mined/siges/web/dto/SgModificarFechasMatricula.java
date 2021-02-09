/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    //Auxiliar
    @JsonIgnore
    private Boolean renderFechaHasta;

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

    public Boolean getRenderFechaHasta() {
        return renderFechaHasta;
    }

    public void setRenderFechaHasta(Boolean renderFechaHasta) {
        this.renderFechaHasta = renderFechaHasta;
    }

    @Override
    public String toString() {
        return "SgModificarFechasMatricula{" + "fechaIngreso=" + fechaIngreso + ", fechaHasta=" + fechaHasta + ", matPk=" + matPk + ", estudiantePk=" + estudiantePk + ", renderFechaHasta=" + renderFechaHasta + '}';
    }
    
     
    
}

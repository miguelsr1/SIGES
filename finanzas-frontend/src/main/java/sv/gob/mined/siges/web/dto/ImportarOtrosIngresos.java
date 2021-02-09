/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportarOtrosIngresos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long movPresupuestoFk;

    private String movFuenteRecursos;

    private BigDecimal movMonto;

    private Long movRubroPk;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getMovFuenteRecursos() {
        return movFuenteRecursos;
    }

    public void setMovFuenteRecursos(String movFuenteRecursos) {
        this.movFuenteRecursos = movFuenteRecursos;
    }

    public Long getMovPresupuestoFk() {
        return movPresupuestoFk;
    }

    public void setMovPresupuestoFk(Long movPresupuestoFk) {
        this.movPresupuestoFk = movPresupuestoFk;
    }

    public Long getMovRubroPk() {
        return movRubroPk;
    }

    public void setMovRubroPk(Long movRubroPk) {
        this.movRubroPk = movRubroPk;
    }

    public BigDecimal getMovMonto() {
        return movMonto;
    }

    public void setMovMonto(BigDecimal movMonto) {
        this.movMonto = movMonto;
    }

// </editor-fold>
}

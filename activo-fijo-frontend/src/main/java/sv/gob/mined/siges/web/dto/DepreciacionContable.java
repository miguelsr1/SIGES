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
public class DepreciacionContable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String catNombre;
    private BigDecimal totalValor;
    private BigDecimal totalValorResidual;
    private BigDecimal depreciacionAcumulada;
    private BigDecimal pendienteDepreciar;

    public DepreciacionContable() {
    }

    public String getCatNombre() {
        return catNombre;
    }

    public void setCatNombre(String catNombre) {
        this.catNombre = catNombre;
    }

    public BigDecimal getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(BigDecimal totalValor) {
        this.totalValor = totalValor;
    }

    public BigDecimal getTotalValorResidual() {
        return totalValorResidual;
    }

    public void setTotalValorResidual(BigDecimal totalValorResidual) {
        this.totalValorResidual = totalValorResidual;
    }

    public BigDecimal getDepreciacionAcumulada() {
        return depreciacionAcumulada;
    }

    public void setDepreciacionAcumulada(BigDecimal depreciacionAcumulada) {
        this.depreciacionAcumulada = depreciacionAcumulada;
    }

    public BigDecimal getPendienteDepreciar() {
        return pendienteDepreciar;
    }

    public void setPendienteDepreciar(BigDecimal pendienteDepreciar) {
        this.pendienteDepreciar = pendienteDepreciar;
    }

}

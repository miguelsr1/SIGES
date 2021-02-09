
package sv.gob.mined.siges.dto;

import java.math.BigDecimal;

public class CantidadesDTO {
    private Integer cantidad;
    private BigDecimal montoTotal;
    private BigDecimal montoDepreciado;
    private BigDecimal valorActual;
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoDepreciado() {
        return montoDepreciado;
    }

    public void setMontoDepreciado(BigDecimal montoDepreciado) {
        this.montoDepreciado = montoDepreciado;
    }

    public BigDecimal getValorActual() {
        return valorActual;
    }

    public void setValorActual(BigDecimal valorActual) {
        this.valorActual = valorActual;
    }
    
    
}

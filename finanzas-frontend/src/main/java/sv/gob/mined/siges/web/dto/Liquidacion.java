/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Clase auxiliar para los montos de liquidación
 *
 * @author sofis-iquezada
 */
public class Liquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String detalle;
    private List<SgMovimientoCuentaBancaria> movimientos;
    private String footer;
    private BigDecimal total;

    public Liquidacion(String detalle, String footer) {
        this.detalle = detalle;
        this.footer = footer;
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<SgMovimientoCuentaBancaria> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<SgMovimientoCuentaBancaria> movimientos) {
        this.movimientos = movimientos;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // </editor-fold>
}

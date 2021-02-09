/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author sofis-jgiron
 */
public class CajaChicaReporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cajaPk;
    private LocalDateTime fecha;
    private String detalle;
    private String aNombreDe;
    private String tipo;
    private BigDecimal monto;
    private BigDecimal saldo;
    private Long movCajaPk;
    private LocalDateTime ultModFecha;

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getaNombreDe() {
        return aNombreDe;
    }

    public void setaNombreDe(String aNombreDe) {
        this.aNombreDe = aNombreDe;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getCajaPk() {
        return cajaPk;
    }

    public void setCajaPk(Long cajaPk) {
        this.cajaPk = cajaPk;
    }

    public Long getMovCajaPk() {
        return movCajaPk;
    }

    public void setMovCajaPk(Long movCajaPk) {
        this.movCajaPk = movCajaPk;
    }

    public LocalDateTime getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(LocalDateTime ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

}

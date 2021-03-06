/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase auxiliar para el reporte de Libro Banco
 *
 * @author sofis-iquezada
 */
public class CuentaBancariaReporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cuentaPk;
    private Integer anio;
    private LocalDateTime fecha;
    private String noCheque;
    private String detalle;
    private String aNombreDe;
    private String tipo;
    private BigDecimal monto;
    private BigDecimal saldo;
    private Long movCuentaPk;
    private LocalDateTime ultModFecha;

    private String componente;
    private String subComponente;
    private BigDecimal ingreso;
    private BigDecimal gasto;

    public Long getCuentaPk() {
        return cuentaPk;
    }

    public void setCuentaPk(Long cuentaPk) {
        this.cuentaPk = cuentaPk;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNoCheque() {
        return noCheque;
    }

    public void setNoCheque(String noCheque) {
        this.noCheque = noCheque;
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

    public Long getMovCuentaPk() {
        return movCuentaPk;
    }

    public void setMovCuentaPk(Long movCuentaPk) {
        this.movCuentaPk = movCuentaPk;
    }

    public LocalDateTime getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(LocalDateTime ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }

    public String getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(String subComponente) {
        this.subComponente = subComponente;
    }

    public BigDecimal getIngreso() {
        return ingreso;
    }

    public void setIngreso(BigDecimal ingreso) {
        this.ingreso = ingreso;
    }

    public BigDecimal getGasto() {
        return gasto;
    }

    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.math.BigDecimal;

/**
 * Clase auxiliar para la obtención del detalle de un requerimiento de fondo
 * @author sofis-iquezada
 */
public class DetalleRequerimientoFondo {
    
    private String correlativo;
    private String codSede;
    private String nomSede;
    private BigDecimal montoFondo;
    private BigDecimal montoPrestamo;
    private BigDecimal montoTotal;

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
    }

    public BigDecimal getMontoFondo() {
        return montoFondo;
    }

    public void setMontoFondo(BigDecimal montoFondo) {
        this.montoFondo = montoFondo;
    }

    public BigDecimal getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigDecimal montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    
    
    
}

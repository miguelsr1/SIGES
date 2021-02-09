package gob.mined.siap2.entities.data.impl;

import java.io.Serializable;
import java.math.BigDecimal;


public class TopePresupuestalGroup implements Serializable{
    
    
    private String codigoCuenta;
    private String codigoSubCuenta;
    private String nombreCuenta;
    private String nombreSubCuenta;
    private Integer idFuente;
    private Integer idFuenteRecursos;
    private BigDecimal montoFormulacion;
    private BigDecimal montoAprobado;

    public TopePresupuestalGroup() {
    }

    public TopePresupuestalGroup(String codigoCuenta, String codigoSubCuenta, String nombreCuenta, String nombreSubCuenta, BigDecimal montoFormulacion, BigDecimal montoAprobado) {
        this.codigoCuenta = codigoCuenta;
        this.codigoSubCuenta = codigoSubCuenta;
        this.nombreCuenta = nombreCuenta;
        this.nombreSubCuenta = nombreSubCuenta;
        this.montoFormulacion = montoFormulacion;
        this.montoAprobado = montoAprobado;
    }

    
    
    
    
    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getNombreSubCuenta() {
        return nombreSubCuenta;
    }

    public void setNombreSubCuenta(String nombreSubCuenta) {
        this.nombreSubCuenta = nombreSubCuenta;
    }

    public BigDecimal getMontoFormulacion() {
        return montoFormulacion;
    }

    public void setMontoFormulacion(BigDecimal montoFormulacion) {
        this.montoFormulacion = montoFormulacion;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public String getCodigoCuenta() {
        return codigoCuenta;
    }

    public void setCodigoCuenta(String codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }

    public String getCodigoSubCuenta() {
        return codigoSubCuenta;
    }

    public void setCodigoSubCuenta(String codigoSubCuenta) {
        this.codigoSubCuenta = codigoSubCuenta;
    }

    public Integer getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(Integer idFuente) {
        this.idFuente = idFuente;
    }

    public Integer getIdFuenteRecursos() {
        return idFuenteRecursos;
    }

    public void setIdFuenteRecursos(Integer idFuenteRecursos) {
        this.idFuenteRecursos = idFuenteRecursos;
    }
    
    
    
}

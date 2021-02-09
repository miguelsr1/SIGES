/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataPOAProyectoView {
    
    private String producto;
    private String actividad;
    private String insumoCodigo;
    private String insumoNombre;
    private Integer insumoId;
    private BigDecimal monto;
    private BigDecimal montoCertificado;
    private BigDecimal montoAdjudicado;
    private String uaci;
    private BigDecimal montoComprometido;    
    private BigDecimal montoDisponibleCertificado;
    private BigDecimal montoDisponibleComprometido;

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getInsumoCodigo() {
        return insumoCodigo;
    }

    public void setInsumoCodigo(String insumoCodigo) {
        this.insumoCodigo = insumoCodigo;
    }

    public String getInsumoNombre() {
        return insumoNombre;
    }

    public void setInsumoNombre(String insumoNombre) {
        this.insumoNombre = insumoNombre;
    }

    public Integer getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(Integer insumoId) {
        this.insumoId = insumoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getUaci() {
        return uaci;
    }

    public void setUaci(String UACI) {
        this.uaci = UACI;
    }

    public BigDecimal getMontoCertificado() {
        return montoCertificado;
    }

    public void setMontoCertificado(BigDecimal montoCertificado) {
        this.montoCertificado = montoCertificado;
    }

    public BigDecimal getMontoAdjudicado() {
        return montoAdjudicado;
    }

    public void setMontoAdjudicado(BigDecimal montoAdjudicado) {
        this.montoAdjudicado = montoAdjudicado;
    }
    
    public BigDecimal getMontoComprometido() {
        return montoComprometido;
    }

    public void setMontoComprometido(BigDecimal montoComprometido) {
        this.montoComprometido = montoComprometido;
    } 
    
    public BigDecimal getMontoDisponibleCertificado() {
        return montoDisponibleCertificado;
    }

    public void setMontoDisponibleCertificado(BigDecimal montoDisponibleCertificado) {
        this.montoDisponibleCertificado = montoDisponibleCertificado;
    }

    public BigDecimal getMontoDisponibleComprometido() {
        return montoDisponibleComprometido;
    }

    public void setMontoDisponibleComprometido(BigDecimal montoDisponibleComprometido) {
        this.montoDisponibleComprometido = montoDisponibleComprometido;
    }
    
    
}

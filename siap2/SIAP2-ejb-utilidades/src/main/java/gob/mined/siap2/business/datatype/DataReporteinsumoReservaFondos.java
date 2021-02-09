/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;

/**
 * Datos del reporte de insumos
 * @author Sofis Solutions
 */
public class DataReporteinsumoReservaFondos {
    private String idInsumo;
    private String idItem;
    private BigDecimal porctjBCO;
    private BigDecimal porctjGOES;
    private String iva;
    private String nombreDelInsumo;
    private String descripcionDelInsumo;
    private String oeg;
    private BigDecimal aporteExterno;
    private BigDecimal aporteGOES;
    private BigDecimal total;    

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public BigDecimal getPorctjBCO() {
        return porctjBCO;
    }

    public void setPorctjBCO(BigDecimal porctjBCO) {
        this.porctjBCO = porctjBCO;
    }

    public BigDecimal getPorctjGOES() {
        return porctjGOES;
    }

    public void setPorctjGOES(BigDecimal porctjGOES) {
        this.porctjGOES = porctjGOES;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }


    public String getNombreDelInsumo() {
        return nombreDelInsumo;
    }

    public void setNombreDelInsumo(String nombreDelInsumo) {
        this.nombreDelInsumo = nombreDelInsumo;
    }

    public String getDescripcionDelInsumo() {
        return descripcionDelInsumo;
    }

    public void setDescripcionDelInsumo(String descripcionDelInsumo) {
        this.descripcionDelInsumo = descripcionDelInsumo;
    }

    public String getOeg() {
        return oeg;
    }

    public void setOeg(String oeg) {
        this.oeg = oeg;
    }

    public BigDecimal getAporteExterno() {
        return aporteExterno;
    }

    public void setAporteExterno(BigDecimal aporteExterno) {
        this.aporteExterno = aporteExterno;
    }

    public BigDecimal getAporteGOES() {
        return aporteGOES;
    }

    public void setAporteGOES(BigDecimal aporteGOES) {
        this.aporteGOES = aporteGOES;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

     // </editor-fold>
    
    
}

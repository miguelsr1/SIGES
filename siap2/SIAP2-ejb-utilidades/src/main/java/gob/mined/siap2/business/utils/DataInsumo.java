/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class DataInsumo implements Serializable{
    private Integer insumoId;
    private String codigoUACI;
    private Integer cantidad;
    private String descripcionInsumo;
    private String nombreUT;
    private String financiacionExt;
    private String financiacionGOES;
    private String total;
    private String codigoProgramaACANP;
    private String codigoSubprograma;
    private String codigoProyecto;

    public Integer getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(Integer insumoId) {
        this.insumoId = insumoId;
    }

    public String getCodigoUACI() {
        return codigoUACI;
    }

    public void setCodigoUACI(String codigoUACI) {
        this.codigoUACI = codigoUACI;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcionInsumo() {
        return descripcionInsumo;
    }

    public void setDescripcionInsumo(String descripcionInsumo) {
        this.descripcionInsumo = descripcionInsumo;
    }

    public String getNombreUT() {
        return nombreUT;
    }

    public void setNombreUT(String nombreUT) {
        this.nombreUT = nombreUT;
    }

    public String getFinanciacionExt() {
        return financiacionExt;
    }

    public void setFinanciacionExt(String financiacionExt) {
        this.financiacionExt = financiacionExt;
    }

    public String getFinanciacionGOES() {
        return financiacionGOES;
    }

    public void setFinanciacionGOES(String financiacionGOES) {
        this.financiacionGOES = financiacionGOES;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCodigoProgramaACANP() {
        return codigoProgramaACANP;
    }

    public void setCodigoProgramaACANP(String codigoProgramaACANP) {
        this.codigoProgramaACANP = codigoProgramaACANP;
    }  

    public String getCodigoSubprograma() {
        return codigoSubprograma;
    }
    
    public void setCodigoSubprograma(String codigoSubprograma) {
        this.codigoSubprograma = codigoSubprograma;
    }

    public String getCodigoProyecto() {
        return codigoProyecto;
    }
    
    public void setCodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }
            
    
}

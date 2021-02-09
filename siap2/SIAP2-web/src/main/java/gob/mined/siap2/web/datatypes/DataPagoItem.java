/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.RelActaItem;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataPagoItem {

    private Integer cantidad;
    private BigDecimal montoUnitAdjudicado;
    private BigDecimal montoTotal;
    private Integer cantRecibida;
    private BigDecimal importe;
    private String descripcion;
    private BigDecimal importeUnit;
    private RelActaItem relActaItem;
    
    ProcesoAdquisicionItem item;

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

    public Integer getCantRecibida() {
        return cantRecibida;
    }

    public void setCantRecibida(Integer cantRecibida) {
        this.cantRecibida = cantRecibida;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public ProcesoAdquisicionItem getItem() {
        return item;
    }

    public void setItem(ProcesoAdquisicionItem item) {
        this.item = item;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public BigDecimal getImporteUnit() {
        return importeUnit;
    }

    public void setImporteUnit(BigDecimal importeUnit) {
        this.importeUnit = importeUnit;
    }
    
    public BigDecimal getMontoUnitAdjudicado() {
        return montoUnitAdjudicado;
    }

    public void setMontoUnitAdjudicado(BigDecimal montoUnitAdjudicado) {
        this.montoUnitAdjudicado = montoUnitAdjudicado;
    }

    public RelActaItem getRelActaItem() {
        return relActaItem;
    }

    public void setRelActaItem(RelActaItem relActaItem) {
        this.relActaItem = relActaItem;
    }
    
}

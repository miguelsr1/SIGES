/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;
import java.util.List;

/**
 * Datos para el reporte de reserva de fondos.
 * @author Sofis Solutions
 */
public class DataReporteReservaFondos extends DataReporteTemplate{

    private String fecha; 
    private List<DataReporteProvedoor> proveedores;
    private BigDecimal totalAportesExterno;
    private BigDecimal totalAportesGoes;
    private BigDecimal totalProveedores;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<DataReporteProvedoor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<DataReporteProvedoor> proveedores) {
        this.proveedores = proveedores;
    }

    public BigDecimal getTotalAportesExterno() {
        return totalAportesExterno;
    }

    public void setTotalAportesExterno(BigDecimal totalAportesExterno) {
        this.totalAportesExterno = totalAportesExterno;
    }

    public BigDecimal getTotalAportesGoes() {
        return totalAportesGoes;
    }

    public void setTotalAportesGoes(BigDecimal totalAportesGoes) {
        this.totalAportesGoes = totalAportesGoes;
    }

    public BigDecimal getTotalProveedores() {
        return totalProveedores;
    }

    public void setTotalProveedores(BigDecimal totalProveedores) {
        this.totalProveedores = totalProveedores;
    }

 // </editor-fold>
}

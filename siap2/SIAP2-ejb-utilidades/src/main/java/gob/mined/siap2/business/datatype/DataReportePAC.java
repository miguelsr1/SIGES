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
 * Datos para el reporte PAC.
 *
 * @author Sofis Solutions
 */
public class DataReportePAC extends DataReporteTemplate{

    private String nombrePAC;
    private String fechaActual;
    private List<DataReportePACInsumo> insumos;
    private String cifradoPresupuestario;
    private BigDecimal totalCompra;
    private String codigoSegunUACI;
    private String entreFechas;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNombrePAC() {
        return nombrePAC;
    }

    public void setNombrePAC(String nombrePAC) {
        this.nombrePAC = nombrePAC;
    }

    public List<DataReportePACInsumo> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<DataReportePACInsumo> insumos) {
        this.insumos = insumos;
    }

    public String getCifradoPresupuestario() {
        return cifradoPresupuestario;
    }

    public void setCifradoPresupuestario(String cifradoPresupuestario) {
        this.cifradoPresupuestario = cifradoPresupuestario;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getCodigoSegunUACI() {
        return codigoSegunUACI;
    }

    public void setCodigoSegunUACI(String codigoSegunUACI) {
        this.codigoSegunUACI = codigoSegunUACI;
    }

    public String getEntreFechas() {
        return entreFechas;
    }

    public void setEntreFechas(String entreFechas) {
        this.entreFechas = entreFechas;
    }

    public String getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }

        // </editor-fold>
}

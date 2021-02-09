/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos del reporte de retención de impuestos por proveedor.
 * @author rgonzalez
 */
public class DataReporteRetencionImpuestoPorProveedor extends DataReporteTemplate{
        
    private String anio;
    private String mes;
    private String impuesto;
    private String totalIngresosSujetosDeRetencionAnual;
    private String totalImpuestoRetenidoAnual;
    
    private List<DataReporteRetencionImpuestoPorProveedorItem> proveedores;


    // <editor-fold defaultstate="collapsed" desc="getter-setter">     
    
    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public List<DataReporteRetencionImpuestoPorProveedorItem> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<DataReporteRetencionImpuestoPorProveedorItem> proveedores) {
        this.proveedores = proveedores;
    }

    public String getTotalIngresosSujetosDeRetencionAnual() {
        return totalIngresosSujetosDeRetencionAnual;
    }

    public void setTotalIngresosSujetosDeRetencionAnual(String totalIngresosSujetosDeRetencionAnual) {
        this.totalIngresosSujetosDeRetencionAnual = totalIngresosSujetosDeRetencionAnual;
    }

    public String getTotalImpuestoRetenidoAnual() {
        return totalImpuestoRetenidoAnual;
    }

    public void setTotalImpuestoRetenidoAnual(String totalImpuestoRetenidoAnual) {
        this.totalImpuestoRetenidoAnual = totalImpuestoRetenidoAnual;
    }
    
    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    
    
    // </editor-fold>

    
}

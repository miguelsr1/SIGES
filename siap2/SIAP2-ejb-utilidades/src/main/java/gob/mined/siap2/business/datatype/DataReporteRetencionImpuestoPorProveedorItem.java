/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de los ítems a incluir en el reporte de rentención de impuestos por proveedor.
 * @author rgonzalez
 */
public class DataReporteRetencionImpuestoPorProveedorItem {
    private String numero;
    private String nombreProveedor;
    private String nit;
    private String ingresoSujetoDeRetencionAnual;
    private String impuestoRetenidoAnual;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getIngresoSujetoDeRetencionAnual() {
        return ingresoSujetoDeRetencionAnual;
    }

    public void setIngresoSujetoDeRetencionAnual(String ingresoSujetoDeRetencionAnual) {
        this.ingresoSujetoDeRetencionAnual = ingresoSujetoDeRetencionAnual;
    }

    public String getImpuestoRetenidoAnual() {
        return impuestoRetenidoAnual;
    }

    public void setImpuestoRetenidoAnual(String impuestoRetenidoAnual) {
        this.impuestoRetenidoAnual = impuestoRetenidoAnual;
    }
    
     // </editor-fold>
    
    
}

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos correspondientes a los ítem en el acta de recepción.
 * @author Sofis Solutions
 */
public class DataReporteActaDeRecepcionItem {

    private String descripcion;
    private String monto;
    private String cantidad;
    private String precioUnit;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
    
    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(String precioUnit) {
        this.precioUnit = precioUnit;
    }
    
    
}

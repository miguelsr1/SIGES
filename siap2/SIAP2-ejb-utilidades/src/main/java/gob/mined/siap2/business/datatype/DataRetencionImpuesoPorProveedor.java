/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos del reporte de retención de impuesto por proveedor.
 * @author rgonzalez
 */
public class DataRetencionImpuesoPorProveedor extends DataReporteTemplate{

    private String firmante;
    private String contenido;
    private String cargoFirmante;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getFirmante() {
        return firmante;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCargoFirmante() {
        return cargoFirmante;
    }

    public void setCargoFirmante(String cargoFirmante) {
        this.cargoFirmante = cargoFirmante;
    }

     // </editor-fold>
}

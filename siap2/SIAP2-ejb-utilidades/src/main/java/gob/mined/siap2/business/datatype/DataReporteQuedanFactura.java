/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de las facturas para ser incluidos en el Quedan
 * @author rgonzalez
 */
public class DataReporteQuedanFactura {
    String numeroFactura;
    String total;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
      // </editor-fold>
    
    
    
}

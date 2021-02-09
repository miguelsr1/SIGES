/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import gob.mined.siap2.entities.data.impl.Proveedor;
import java.math.BigDecimal;

/**
 * Clase auxiliar para los items del reporte de retención de impuestos por proveedor.
 * @author rgonzalez
 */
public class DataReporteRetencionImpuestoPorProveedorItemTemporal {

    private Proveedor proveedor;
    private BigDecimal ingresoSujetoDeRetencionAnual;
    private BigDecimal impuestoRetenidoAnual;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public BigDecimal getIngresoSujetoDeRetencionAnual() {
        return ingresoSujetoDeRetencionAnual;
    }

    public void setIngresoSujetoDeRetencionAnual(BigDecimal ingresoSujetoDeRetencionAnual) {
        this.ingresoSujetoDeRetencionAnual = ingresoSujetoDeRetencionAnual;
    }

    public BigDecimal getImpuestoRetenidoAnual() {
        return impuestoRetenidoAnual;
    }

    public void setImpuestoRetenidoAnual(BigDecimal impuestoRetenidoAnual) {
        this.impuestoRetenidoAnual = impuestoRetenidoAnual;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    // </editor-fold>
}

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte de Proveedor.
 *
 * @author Sofis Solutions
 */
public class DataReporteProvedoor {

    private List<DataReporteinsumoReservaFondos> insumos;
    private String nombrDelProveedor;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public List<DataReporteinsumoReservaFondos> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<DataReporteinsumoReservaFondos> insumos) {
        this.insumos = insumos;
    }

    public String getNombrDelProveedor() {
        return nombrDelProveedor;
    }

    public void setNombrDelProveedor(String nombrDelProveedor) {
        this.nombrDelProveedor = nombrDelProveedor;
    }

    // </editor-fold>
}

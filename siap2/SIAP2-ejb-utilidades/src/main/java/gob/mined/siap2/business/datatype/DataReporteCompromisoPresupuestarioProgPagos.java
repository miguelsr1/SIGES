/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte PEP
 * @author Sofis Solutions
 */
public class DataReporteCompromisoPresupuestarioProgPagos extends DataReporteTemplate {
    private String fecha;
    private String anio;
    private List<DataReporteCompromisoPresupuestarioProgPagosItem> filas;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
    
    
    public List<DataReporteCompromisoPresupuestarioProgPagosItem> getFilas() {
        return filas;
    }

    public void setFilas(List<DataReporteCompromisoPresupuestarioProgPagosItem> filas) {
        this.filas = filas;
    }
    
    

    
    // </editor-fold>


    
}

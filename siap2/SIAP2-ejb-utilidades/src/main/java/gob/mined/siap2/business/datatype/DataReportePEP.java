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
public class DataReportePEP {
    private String fecha;
    private String anio;
    private List<DataFilaReportePEP> filas;

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

    public List<DataFilaReportePEP> getFilas() {
        return filas;
    }

    public void setFilas(List<DataFilaReportePEP> filas) {
        this.filas = filas;
    }
    
    // </editor-fold>
    
    
}

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte PEP global.
 *
 * @author Sofis Solutions
 */
public class DataReportePEPGlobal extends DataReporteTemplate{

    String fecha;
    String anio;
    List<DataFilaGlobalReportePEP> filas;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<DataFilaGlobalReportePEP> getFilas() {
        return filas;
    }

    public void setFilas(List<DataFilaGlobalReportePEP> filas) {
        this.filas = filas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    // </editor-fold>

}

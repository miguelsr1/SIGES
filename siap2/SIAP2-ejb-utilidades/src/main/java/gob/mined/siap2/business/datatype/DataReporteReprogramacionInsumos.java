/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos de los insumos en el reporte de reprogramación.
 * @author Eduardo
 */
public class DataReporteReprogramacionInsumos {

    private String insumo;
    private String OEG;
    private String fuenteRecurso;
    private String descripcion;
    private String actividad; 
    private String monto;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public String getOEG() {
        return OEG;
    }

    public void setOEG(String OEG) {
        this.OEG = OEG;
    }

    public String getFuenteRecurso() {
        return fuenteRecurso;
    }

    public void setFuenteRecurso(String fuenteRecurso) {
        this.fuenteRecurso = fuenteRecurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
     // </editor-fold>
}

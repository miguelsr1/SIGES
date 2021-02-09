/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

/**
 * Datos para el reporte de disponibilidad presupuestaria
 * @author Sofis Solutions
 */
public class DataReporteDisponibilidadPresupuestaria extends DataReporteTemplate{

    private String descripcionContratacion;
    private String nombreUT;
    private String presupuesto;
    private String recurso;
    private String oeg;
    private String nombreFuente;
    private String programa;
    private String subPrograma;
    private String proyecto;
    private String actividadAccionCentral;
    private String actividadAsignacionNoProgramable;
    private String poa;
    private String numero;
    private String fechaEmision;
    private String fechaAprobacion;
    private String estado;


  // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    public String getDescripcionContratacion() {
        return descripcionContratacion;
    }

    public void setDescripcionContratacion(String descripcionContratacion) {
        this.descripcionContratacion = descripcionContratacion;
    }

    public String getNombreUT() {
        return nombreUT;
    }

    public void setNombreUT(String nombreUT) {
        this.nombreUT = nombreUT;
    }

    public String getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(String presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getNombreFuente() {
        return nombreFuente;
    }

    public void setNombreFuente(String nombreFuente) {
        this.nombreFuente = nombreFuente;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getSubPrograma() {
        return subPrograma;
    }

    public void setSubPrograma(String subPrograma) {
        this.subPrograma = subPrograma;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getActividadAccionCentral() {
        return actividadAccionCentral;
    }

    public void setActividadAccionCentral(String actividadAccionCentral) {
        this.actividadAccionCentral = actividadAccionCentral;
    }

    public String getActividadAsignacionNoProgramable() {
        return actividadAsignacionNoProgramable;
    }

    public void setActividadAsignacionNoProgramable(String actividadAsignacionNoProgramable) {
        this.actividadAsignacionNoProgramable = actividadAsignacionNoProgramable;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public String getOeg() {
        return oeg;
    }

    public void setOeg(String oeg) {
        this.oeg = oeg;
    }
    
    public String getPoa() {
        return poa;
    }

    public void setPoa(String poa) {
        this.poa = poa;
    }
    
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

        // </editor-fold>
}

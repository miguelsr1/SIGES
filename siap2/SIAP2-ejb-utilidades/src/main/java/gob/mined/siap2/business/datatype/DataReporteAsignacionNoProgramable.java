/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype;

import java.util.List;

/**
 * Datos para el reporte de una asignación no programable.
 * @author Sofis Solutions
 */
public class DataReporteAsignacionNoProgramable {
    private String nombre="";
    private String planificacion="";
    
    private List<DataReporteAsignacionNoProgramableLineas> lineas;
    
    private String unidadTecnicaResponsable="";
    private String calificador="";
    private String descripcion="";
    
    private String usuario="";
    private String fechaActual="";
        
    private List<DataReporteAsignacionNoProgramableSubActividades> subActividadeses;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(String planificacion) {
        this.planificacion = planificacion;
    }

    public List<DataReporteAsignacionNoProgramableLineas> getLineas() {
        return lineas;
    }

    public void setLineas(List<DataReporteAsignacionNoProgramableLineas> lineas) {
        this.lineas = lineas;
    }

    public String getUnidadTecnicaResponsable() {
        return unidadTecnicaResponsable;
    }

    public void setUnidadTecnicaResponsable(String unidadTecnicaResponsable) {
        this.unidadTecnicaResponsable = unidadTecnicaResponsable;
    }

    public String getCalificador() {
        return calificador;
    }

    public void setCalificador(String calificador) {
        this.calificador = calificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DataReporteAsignacionNoProgramableSubActividades> getSubActividadeses() {
        return subActividadeses;
    }

    public void setSubActividadeses(List<DataReporteAsignacionNoProgramableSubActividades> subActividadeses) {
        this.subActividadeses = subActividadeses;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(String fechaActual) {
        this.fechaActual = fechaActual;
    }
    
    
    
    
    
    
    
}

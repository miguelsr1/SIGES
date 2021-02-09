/*
 * SIGES
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 *
 * @author Usuario
 */
public class FiltroCalendarioUI implements Serializable {

    private Boolean actividades = Boolean.TRUE;
    private Boolean calificaciones = Boolean.TRUE;
    private Boolean periodosAcademicos = Boolean.TRUE;

    public Boolean getActividades() {
        return actividades;
    }

    public void setActividades(Boolean actividades) {
        this.actividades = actividades;
    }

    public Boolean getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Boolean calificaciones) {
        this.calificaciones = calificaciones;
    }

    public Boolean getPeriodosAcademicos() {
        return periodosAcademicos;
    }

    public void setPeriodosAcademicos(Boolean periodosAcademicos) {
        this.periodosAcademicos = periodosAcademicos;
    }

}

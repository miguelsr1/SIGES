/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;

public class FiltroRelPeriodosHabilitacionPeriodo implements Serializable {
    
    private Long rangoFechaFk;
    private EnumCalendarioEscolar rphTipoCalendarioEscolar;
    private Long rphHabilitacionPeriodoCalificacionFk;
    private Long rphEstudianteFk;
    private LocalDate fechaHabilitada;
    private EnumEstadoHabilitacionPeriodoCalificacion hpcEstado;
    private List<Long> estudiantesFk;
    private Long seccionFk;
    private Long periodoCalificacionFk;
    private List<Long> ignorarRangoFechaFk;
    private List<Long> rangoFechaFkList;
    private EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion;
    private List<EnumCalendarioEscolar> rphTipoCalendarioEscolarList;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRelPeriodosHabilitacionPeriodo() {
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getRangoFechaFk() {
        return rangoFechaFk;
    }

    public void setRangoFechaFk(Long rangoFechaFk) {
        this.rangoFechaFk = rangoFechaFk;
    }

    public EnumCalendarioEscolar getRphTipoCalendarioEscolar() {
        return rphTipoCalendarioEscolar;
    }

    public void setRphTipoCalendarioEscolar(EnumCalendarioEscolar rphTipoCalendarioEscolar) {
        this.rphTipoCalendarioEscolar = rphTipoCalendarioEscolar;
    }

    public Long getRphHabilitacionPeriodoCalificacionFk() {
        return rphHabilitacionPeriodoCalificacionFk;
    }

    public void setRphHabilitacionPeriodoCalificacionFk(Long rphHabilitacionPeriodoCalificacionFk) {
        this.rphHabilitacionPeriodoCalificacionFk = rphHabilitacionPeriodoCalificacionFk;
    }

    public Long getRphEstudianteFk() {
        return rphEstudianteFk;
    }

    public void setRphEstudianteFk(Long rphEstudianteFk) {
        this.rphEstudianteFk = rphEstudianteFk;
    }

    public LocalDate getFechaHabilitada() {
        return fechaHabilitada;
    }

    public void setFechaHabilitada(LocalDate fechaHabilitada) {
        this.fechaHabilitada = fechaHabilitada;
    }

    public EnumEstadoHabilitacionPeriodoCalificacion getHpcEstado() {
        return hpcEstado;
    }

    public void setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion hpcEstado) {
        this.hpcEstado = hpcEstado;
    }

    public List<Long> getEstudiantesFk() {
        return estudiantesFk;
    }

    public void setEstudiantesFk(List<Long> estudiantesFk) {
        this.estudiantesFk = estudiantesFk;
    }

    public Long getSeccionFk() {
        return seccionFk;
    }

    public void setSeccionFk(Long seccionFk) {
        this.seccionFk = seccionFk;
    }

    public Long getPeriodoCalificacionFk() {
        return periodoCalificacionFk;
    }

    public void setPeriodoCalificacionFk(Long periodoCalificacionFk) {
        this.periodoCalificacionFk = periodoCalificacionFk;
    }

    public List<Long> getIgnorarRangoFechaFk() {
        return ignorarRangoFechaFk;
    }

    public void setIgnorarRangoFechaFk(List<Long> ignorarRangoFechaFk) {
        this.ignorarRangoFechaFk = ignorarRangoFechaFk;
    }

    public List<Long> getRangoFechaFkList() {
        return rangoFechaFkList;
    }

    public void setRangoFechaFkList(List<Long> rangoFechaFkList) {
        this.rangoFechaFkList = rangoFechaFkList;
    }

    public EnumTiposPeriodosCalificaciones getRphTipoPeriodoCalificacion() {
        return rphTipoPeriodoCalificacion;
    }

    public void setRphTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones rphTipoPeriodoCalificacion) {
        this.rphTipoPeriodoCalificacion = rphTipoPeriodoCalificacion;
    }

    public List<EnumCalendarioEscolar> getRphTipoCalendarioEscolarList() {
        return rphTipoCalendarioEscolarList;
    }

    public void setRphTipoCalendarioEscolarList(List<EnumCalendarioEscolar> rphTipoCalendarioEscolarList) {
        this.rphTipoCalendarioEscolarList = rphTipoCalendarioEscolarList;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.rangoFechaFk);
        hash = 19 * hash + Objects.hashCode(this.rphTipoCalendarioEscolar);
        hash = 19 * hash + Objects.hashCode(this.rphHabilitacionPeriodoCalificacionFk);
        hash = 19 * hash + Objects.hashCode(this.fechaHabilitada);
        hash = 19 * hash + Objects.hashCode(this.hpcEstado);
        hash = 19 * hash + Objects.hashCode(this.estudiantesFk);
        hash = 19 * hash + Objects.hashCode(this.first);
        hash = 19 * hash + Objects.hashCode(this.maxResults);
        hash = 19 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 19 * hash + Arrays.hashCode(this.ascending);
        hash = 19 * hash + Arrays.deepHashCode(this.incluirCampos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroRelPeriodosHabilitacionPeriodo other = (FiltroRelPeriodosHabilitacionPeriodo) obj;
        if (!Objects.equals(this.rangoFechaFk, other.rangoFechaFk)) {
            return false;
        }
        if (this.rphTipoCalendarioEscolar != other.rphTipoCalendarioEscolar) {
            return false;
        }
        if (!Objects.equals(this.rphHabilitacionPeriodoCalificacionFk, other.rphHabilitacionPeriodoCalificacionFk)) {
            return false;
        }
        if (!Objects.equals(this.fechaHabilitada, other.fechaHabilitada)) {
            return false;
        }
        if (this.hpcEstado != other.hpcEstado) {
            return false;
        }
        if (!Objects.equals(this.estudiantesFk, other.estudiantesFk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }


    
    
    
}

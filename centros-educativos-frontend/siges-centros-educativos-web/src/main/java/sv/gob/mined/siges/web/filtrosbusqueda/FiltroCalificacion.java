/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;

public class FiltroCalificacion extends FiltroSeccion implements Serializable {

    private Long calPk;
    private Long calComponentePlanEstudio;
    private LocalDate calFecha;
    private Long calRangoFecha;

    private EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion;
    private List<EnumTiposPeriodosCalificaciones> calTiposPeriodoCalificacion;
    private EnumCalendarioEscolar calTipoCalendarioEscolar;
    private Integer calNumero;
    private Boolean calTieneEstudiantePendiente;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean incluirCalificacionesEstudiantes;
    private Boolean inicializarLazyToOne;

    public FiltroCalificacion() {
        this.inicializarLazyToOne = Boolean.TRUE;
        this.first = 0L;
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public Long getCalComponentePlanEstudio() {
        return calComponentePlanEstudio;
    }

    public void setCalComponentePlanEstudio(Long calComponentePlanEstudio) {
        this.calComponentePlanEstudio = calComponentePlanEstudio;
    }

    public LocalDate getCalFecha() {
        return calFecha;
    }

    public void setCalFecha(LocalDate calFecha) {
        this.calFecha = calFecha;
    }

    public Long getCalRangoFecha() {
        return calRangoFecha;
    }

    public void setCalRangoFecha(Long calRangoFecha) {
        this.calRangoFecha = calRangoFecha;
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

    public EnumTiposPeriodosCalificaciones getCalTipoPeriodoCalificacion() {
        return calTipoPeriodoCalificacion;
    }

    public void setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones calTipoPeriodoCalificacion) {
        this.calTipoPeriodoCalificacion = calTipoPeriodoCalificacion;
    }

    public List<EnumTiposPeriodosCalificaciones> getCalTiposPeriodoCalificacion() {
        return calTiposPeriodoCalificacion;
    }

    public void setCalTiposPeriodoCalificacion(List<EnumTiposPeriodosCalificaciones> calTiposPeriodoCalificacion) {
        this.calTiposPeriodoCalificacion = calTiposPeriodoCalificacion;
    }    

    public EnumCalendarioEscolar getCalTipoCalendarioEscolar() {
        return calTipoCalendarioEscolar;
    }

    public void setCalTipoCalendarioEscolar(EnumCalendarioEscolar calTipoCalendarioEscolar) {
        this.calTipoCalendarioEscolar = calTipoCalendarioEscolar;
    }

    public Integer getCalNumero() {
        return calNumero;
    }

    public void setCalNumero(Integer calNumero) {
        this.calNumero = calNumero;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getIncluirCalificacionesEstudiantes() {
        return incluirCalificacionesEstudiantes;
    }

    public void setIncluirCalificacionesEstudiantes(Boolean incluirCalificacionesEstudiantes) {
        this.incluirCalificacionesEstudiantes = incluirCalificacionesEstudiantes;
    }

    public Boolean getInicializarLazyToOne() {
        return inicializarLazyToOne;
    }

    public void setInicializarLazyToOne(Boolean inicializarLazyToOne) {
        this.inicializarLazyToOne = inicializarLazyToOne;
    }

    public Boolean getCalTieneEstudiantePendiente() {
        return calTieneEstudiantePendiente;
    }

    public void setCalTieneEstudiantePendiente(Boolean calTieneEstudiantePendiente) {
        this.calTieneEstudiantePendiente = calTieneEstudiantePendiente;
    }


       
}

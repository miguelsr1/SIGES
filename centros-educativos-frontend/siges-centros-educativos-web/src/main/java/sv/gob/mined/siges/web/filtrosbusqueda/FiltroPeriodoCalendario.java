/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;

public class FiltroPeriodoCalendario implements Serializable {

    private Long calPk;
    private Long cesModalidadAtencionFk;
    private Long cesNivelFk;
    private EnumCalendarioEscolar cesTipo;
    private LocalDate fechaCalificacion;
    private Boolean cesHabilitado;
    private Long cesSubModalidadAtencionFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroPeriodoCalendario() {
        this.first = 0L;
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public Long getCesModalidadAtencionFk() {
        return cesModalidadAtencionFk;
    }

    public void setCesModalidadAtencionFk(Long cesModalidadAtencionFk) {
        this.cesModalidadAtencionFk = cesModalidadAtencionFk;
    }

    public Long getCesNivelFk() {
        return cesNivelFk;
    }

    public void setCesNivelFk(Long cesNivelFk) {
        this.cesNivelFk = cesNivelFk;
    }

    public EnumCalendarioEscolar getCesTipo() {
        return cesTipo;
    }

    public void setCesTipo(EnumCalendarioEscolar cesTipo) {
        this.cesTipo = cesTipo;
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

    public LocalDate getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDate fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Boolean getCesHabilitado() {
        return cesHabilitado;
    }

    public void setCesHabilitado(Boolean cesHabilitado) {
        this.cesHabilitado = cesHabilitado;
    }

    public Long getCesSubModalidadAtencionFk() {
        return cesSubModalidadAtencionFk;
    }

    public void setCesSubModalidadAtencionFk(Long cesSubModalidadAtencionFk) {
        this.cesSubModalidadAtencionFk = cesSubModalidadAtencionFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    
}

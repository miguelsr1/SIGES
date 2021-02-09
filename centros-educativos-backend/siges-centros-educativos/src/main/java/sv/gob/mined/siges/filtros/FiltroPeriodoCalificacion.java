/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;

public class FiltroPeriodoCalificacion implements Serializable {
    
    private Long calPk;
    
    private Long pcaModalidadEducativa;
    private Long pcaModalidadAtencion;
    private Long pcaSubModalidadAtencion;
    private Long pcaAnioLectivo;
    private Integer pcaNumero;
    private Long pcaTipoCalendario;
    private Boolean pcaEsPrueba;
    private List<Integer> pcaNumeros;
    private EnumTipoPeriodoSeccion pcaTipoPeriodo;
    private Integer pcaNumeroPeriodo;

    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private Boolean inicializarRangoFecha;
    
    public FiltroPeriodoCalificacion(){
        inicializarRangoFecha=Boolean.FALSE;
    }

    public Long getCalPk() {
        return calPk;
    }

    public void setCalPk(Long calPk) {
        this.calPk = calPk;
    }

    public Long getPcaModalidadEducativa() {
        return pcaModalidadEducativa;
    }

    public void setPcaModalidadEducativa(Long pcaModalidadEducativa) {
        this.pcaModalidadEducativa = pcaModalidadEducativa;
    }

    public Long getPcaModalidadAtencion() {
        return pcaModalidadAtencion;
    }

    public void setPcaModalidadAtencion(Long pcaModalidadAtencion) {
        this.pcaModalidadAtencion = pcaModalidadAtencion;
    }

    public Integer getPcaNumero() {
        return pcaNumero;
    }

    public void setPcaNumero(Integer pcaNumero) {
        this.pcaNumero = pcaNumero;
    }

    public Long getPcaAnioLectivo() {
        return pcaAnioLectivo;
    }

    public void setPcaAnioLectivo(Long pcaAnioLectivo) {
        this.pcaAnioLectivo = pcaAnioLectivo;
    }
    

    public Boolean getInicializarRangoFecha() {
        return inicializarRangoFecha;
    }

    public void setInicializarRangoFecha(Boolean inicializarRangoFecha) {
        this.inicializarRangoFecha = inicializarRangoFecha;
    }

    public Long getPcaTipoCalendario() {
        return pcaTipoCalendario;
    }

    public void setPcaTipoCalendario(Long pcaTipoCalendario) {
        this.pcaTipoCalendario = pcaTipoCalendario;
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

    public Long getPcaSubModalidadAtencion() {
        return pcaSubModalidadAtencion;
    }

    public void setPcaSubModalidadAtencion(Long pcaSubModalidadAtencion) {
        this.pcaSubModalidadAtencion = pcaSubModalidadAtencion;
    }

    public Boolean getPcaEsPrueba() {
        return pcaEsPrueba;
    }

    public void setPcaEsPrueba(Boolean pcaEsPrueba) {
        this.pcaEsPrueba = pcaEsPrueba;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public List<Integer> getPcaNumeros() {
        return pcaNumeros;
    }

    public void setPcaNumeros(List<Integer> pcaNumeros) {
        this.pcaNumeros = pcaNumeros;
    }

    public EnumTipoPeriodoSeccion getPcaTipoPeriodo() {
        return pcaTipoPeriodo;
    }

    public void setPcaTipoPeriodo(EnumTipoPeriodoSeccion pcaTipoPeriodo) {
        this.pcaTipoPeriodo = pcaTipoPeriodo;
    }

    public Integer getPcaNumeroPeriodo() {
        return pcaNumeroPeriodo;
    }

    public void setPcaNumeroPeriodo(Integer pcaNumeroPeriodo) {
        this.pcaNumeroPeriodo = pcaNumeroPeriodo;
    }

    

    @Override
    public String toString() {
        return "FiltroPeriodoCalificacion{" + "calPk=" + calPk + ", pcaModalidadEducativa=" + pcaModalidadEducativa + ", pcaModalidadAtencion=" + pcaModalidadAtencion + ", pcaSubModalidadAtencion=" + pcaSubModalidadAtencion + ", pcaAnioLectivo=" + pcaAnioLectivo + ", pcaNumero=" + pcaNumero + ", pcaTipoCalendario=" + pcaTipoCalendario + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", inicializarRangoFecha=" + inicializarRangoFecha + '}';
    }
    
    
    
}

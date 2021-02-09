package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;

/**
 *
 * @author usuario
 */
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
    
    //Por defecto cuando pcaTipoPeriodo es null se busca periodo nulo y ANUAL
    //agregando busquedaTodoTipoPeriodo se busca todo
    private Boolean busquedaTodoTipoPeriodo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    private Boolean inicializarRangoFecha;

    public FiltroPeriodoCalificacion() {
        inicializarRangoFecha = Boolean.FALSE;
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

    public Boolean getBusquedaTodoTipoPeriodo() {
        return busquedaTodoTipoPeriodo;
    }

    public void setBusquedaTodoTipoPeriodo(Boolean busquedaTodoTipoPeriodo) {
        this.busquedaTodoTipoPeriodo = busquedaTodoTipoPeriodo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.calPk);
        hash = 83 * hash + Objects.hashCode(this.pcaModalidadEducativa);
        hash = 83 * hash + Objects.hashCode(this.pcaModalidadAtencion);
        hash = 83 * hash + Objects.hashCode(this.pcaSubModalidadAtencion);
        hash = 83 * hash + Objects.hashCode(this.pcaAnioLectivo);
        hash = 83 * hash + Objects.hashCode(this.pcaNumero);
        hash = 83 * hash + Objects.hashCode(this.pcaTipoCalendario);
        hash = 83 * hash + Objects.hashCode(this.pcaEsPrueba);
        hash = 83 * hash + Objects.hashCode(this.pcaNumeros);
        hash = 83 * hash + Objects.hashCode(this.first);
        hash = 83 * hash + Objects.hashCode(this.maxResults);
        hash = 83 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 83 * hash + Arrays.hashCode(this.ascending);
        hash = 83 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 83 * hash + Objects.hashCode(this.inicializarRangoFecha);
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
        final FiltroPeriodoCalificacion other = (FiltroPeriodoCalificacion) obj;
        if (!Objects.equals(this.calPk, other.calPk)) {
            return false;
        }
        if (!Objects.equals(this.pcaModalidadEducativa, other.pcaModalidadEducativa)) {
            return false;
        }
        if (!Objects.equals(this.pcaModalidadAtencion, other.pcaModalidadAtencion)) {
            return false;
        }
        if (!Objects.equals(this.pcaSubModalidadAtencion, other.pcaSubModalidadAtencion)) {
            return false;
        }
        if (!Objects.equals(this.pcaAnioLectivo, other.pcaAnioLectivo)) {
            return false;
        }
        if (!Objects.equals(this.pcaNumero, other.pcaNumero)) {
            return false;
        }
        if (!Objects.equals(this.pcaTipoCalendario, other.pcaTipoCalendario)) {
            return false;
        }
        if (!Objects.equals(this.pcaEsPrueba, other.pcaEsPrueba)) {
            return false;
        }
        if (!Objects.equals(this.pcaNumeros, other.pcaNumeros)) {
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
        if (!Objects.equals(this.inicializarRangoFecha, other.inicializarRangoFecha)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltroPeriodoCalificacion{" + "calPk=" + calPk + ", pcaModalidadEducativa=" + pcaModalidadEducativa + ", pcaModalidadAtencion=" + pcaModalidadAtencion + ", pcaSubModalidadAtencion=" + pcaSubModalidadAtencion + ", pcaAnioLectivo=" + pcaAnioLectivo + ", pcaNumero=" + pcaNumero + ", pcaTipoCalendario=" + pcaTipoCalendario + ", pcaEsPrueba=" + pcaEsPrueba + ", pcaNumeros=" + pcaNumeros + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", incluirCampos=" + incluirCampos + ", inicializarRangoFecha=" + inicializarRangoFecha + '}';
    }
    
    
    
    
    
}

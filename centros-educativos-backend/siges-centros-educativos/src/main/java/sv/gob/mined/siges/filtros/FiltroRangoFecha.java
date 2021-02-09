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
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;

public class FiltroRangoFecha implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private Long periodoCalificacionPk;
    private Boolean habilitado;
    private LocalDate desde;
    private LocalDate hasta;
    private Long excluirRangoPk;
    private Integer cantidadPeriodoCalificacion;
    private Long modalidadEducativaPk;
    private Long modalidadAtencionPk;
    private Long subModalidadAtencionPk;
    private List<Integer> pcaNumeros;
    private EnumTipoPeriodoSeccion pcaTipoPeriodo;
    private Integer pcaNumeroPeriodo;
    private Boolean pcaEsPrueba;
    
    
    private Long pcaAnioLectivo;
    private Long pcaTipoCalendario;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRangoFecha() {
    }
     

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public Long getPeriodoCalificacionPk() {
        return periodoCalificacionPk;
    }

    public void setPeriodoCalificacionPk(Long periodoCalificacionPk) {
        this.periodoCalificacionPk = periodoCalificacionPk;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    public Long getExcluirRangoPk() {
        return excluirRangoPk;
    }

    public void setExcluirRangoPk(Long excluirRangoPk) {
        this.excluirRangoPk = excluirRangoPk;
    }

    public Integer getCantidadPeriodoCalificacion() {
        return cantidadPeriodoCalificacion;
    }

    public void setCantidadPeriodoCalificacion(Integer cantidadPeriodoCalificacion) {
        this.cantidadPeriodoCalificacion = cantidadPeriodoCalificacion;
    }

    public Long getModalidadEducativaPk() {
        return modalidadEducativaPk;
    }

    public void setModalidadEducativaPk(Long modalidadEducativaPk) {
        this.modalidadEducativaPk = modalidadEducativaPk;
    }

    public Long getModalidadAtencionPk() {
        return modalidadAtencionPk;
    }

    public void setModalidadAtencionPk(Long modalidadAtencionPk) {
        this.modalidadAtencionPk = modalidadAtencionPk;
    }

    public Long getSubModalidadAtencionPk() {
        return subModalidadAtencionPk;
    }

    public void setSubModalidadAtencionPk(Long subModalidadAtencionPk) {
        this.subModalidadAtencionPk = subModalidadAtencionPk;
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

    public Long getPcaAnioLectivo() {
        return pcaAnioLectivo;
    }

    public void setPcaAnioLectivo(Long pcaAnioLectivo) {
        this.pcaAnioLectivo = pcaAnioLectivo;
    }

    public Long getPcaTipoCalendario() {
        return pcaTipoCalendario;
    }

    public void setPcaTipoCalendario(Long pcaTipoCalendario) {
        this.pcaTipoCalendario = pcaTipoCalendario;
    }

    public Boolean getPcaEsPrueba() {
        return pcaEsPrueba;
    }

    public void setPcaEsPrueba(Boolean pcaEsPrueba) {
        this.pcaEsPrueba = pcaEsPrueba;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.codigo);
        hash = 11 * hash + Objects.hashCode(this.codigoExacto);
        hash = 11 * hash + Objects.hashCode(this.periodoCalificacionPk);
        hash = 11 * hash + Objects.hashCode(this.habilitado);
        hash = 11 * hash + Objects.hashCode(this.desde);
        hash = 11 * hash + Objects.hashCode(this.hasta);
        hash = 11 * hash + Objects.hashCode(this.excluirRangoPk);
        hash = 11 * hash + Objects.hashCode(this.cantidadPeriodoCalificacion);
        hash = 11 * hash + Objects.hashCode(this.modalidadEducativaPk);
        hash = 11 * hash + Objects.hashCode(this.modalidadAtencionPk);
        hash = 11 * hash + Objects.hashCode(this.first);
        hash = 11 * hash + Objects.hashCode(this.maxResults);
        hash = 11 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 11 * hash + Arrays.hashCode(this.ascending);
        hash = 11 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRangoFecha other = (FiltroRangoFecha) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.codigoExacto, other.codigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.periodoCalificacionPk, other.periodoCalificacionPk)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.desde, other.desde)) {
            return false;
        }
        if (!Objects.equals(this.hasta, other.hasta)) {
            return false;
        }
        if (!Objects.equals(this.excluirRangoPk, other.excluirRangoPk)) {
            return false;
        }
        if (!Objects.equals(this.cantidadPeriodoCalificacion, other.cantidadPeriodoCalificacion)) {
            return false;
        }
        if (!Objects.equals(this.modalidadEducativaPk, other.modalidadEducativaPk)) {
            return false;
        }
        if (!Objects.equals(this.modalidadAtencionPk, other.modalidadAtencionPk)) {
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

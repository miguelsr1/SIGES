package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.EstadosPOA;
import java.io.Serializable;

public class FiltroPOA implements Serializable {
    private Integer id;
    private String nombre;
    private String codigo;
    private Integer programaId;
    private Integer anioFiscalId;
    private Integer unidadTecnicaId;
    private EstadosPOA estado;
    private Integer ultPeriodoHabilitado;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Integer programaId) {
        this.programaId = programaId;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Integer getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public void setUnidadTecnicaId(Integer unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }

    public EstadosPOA getEstado() {
        return estado;
    }

    public void setEstado(EstadosPOA estado) {
        this.estado = estado;
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

    public Integer getUltPeriodoHabilitado() {
        return ultPeriodoHabilitado;
    }

    public void setUltPeriodoHabilitado(Integer ultPeriodoHabilitado) {
        this.ultPeriodoHabilitado = ultPeriodoHabilitado;
    }
}

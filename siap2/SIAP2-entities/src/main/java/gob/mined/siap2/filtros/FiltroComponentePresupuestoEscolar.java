package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.TipoSubcomponente;
import java.io.Serializable;

public class FiltroComponentePresupuestoEscolar implements Serializable {
    private Integer id;
    private Integer categoriaComponenteId;
    private TipoSubcomponente tipoSubcomponente;
    private Boolean habilitado;
    private String nombre;
    private String codigoNombre;
    private Boolean unidadOperativa;
    private Integer padreId;
    private String codigo;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public Integer getCategoriaComponenteId() {
        return categoriaComponenteId;
    }

    public void setCategoriaComponenteId(Integer categoriaComponenteId) {
        this.categoriaComponenteId = categoriaComponenteId;
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

    public TipoSubcomponente getTipoSubcomponente() {
        return tipoSubcomponente;
    }

    public void setTipoSubcomponente(TipoSubcomponente tipoSubcomponente) {
        this.tipoSubcomponente = tipoSubcomponente;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getUnidadOperativa() {
        return unidadOperativa;
    }

    public void setUnidadOperativa(Boolean unidadOperativa) {
        this.unidadOperativa = unidadOperativa;
    }

    public Integer getPadreId() {
        return padreId;
    }

    public void setPadreId(Integer padreId) {
        this.padreId = padreId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoNombre() {
        return codigoNombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }

    
    
}

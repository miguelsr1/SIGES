package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.TipoSede;
import java.io.Serializable;
import java.util.List;

public class FiltroSede implements Serializable {

    private Integer id;
    private String codigo;
    private List<Long> clasificacionesId;
    private Boolean oaeYMiembrosVigente;
    private List<TipoSede> tipos;
    private List<String> tiposOrganismos;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getOaeYMiembrosVigente() {
        return oaeYMiembrosVigente;
    }

    public void setOaeYMiembrosVigente(Boolean oaeYMiembrosVigente) {
        this.oaeYMiembrosVigente = oaeYMiembrosVigente;
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

    public List<Long> getClasificacionesId() {
        return clasificacionesId;
    }

    public void setClasificacionesId(List<Long> clasificacionesId) {
        this.clasificacionesId = clasificacionesId;
    }

    public List<TipoSede> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoSede> tipos) {
        this.tipos = tipos;
    }

    public List<String> getTiposOrganismos() {
        return tiposOrganismos;
    }

    public void setTiposOrganismos(List<String> tiposOrganismos) {
        this.tiposOrganismos = tiposOrganismos;
    }

    
    
    
}

package gob.mined.siap2.filtros;

import java.io.Serializable;
import java.util.List;

public class FiltroUnidadTecnica implements Serializable {
    private Integer id;
    private String nombre;
    private Boolean unidadOperativa;
    private Integer padreId;
    private String operacion;
    private String codigo;
    private Integer uniUsuarioId;
    private String codUsuario;
    private List<Integer> listaIds;
    
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

    public Integer getUniUsuarioId() {
        return uniUsuarioId;
    }

    public void setUniUsuarioId(Integer uniUsuarioId) {
        this.uniUsuarioId = uniUsuarioId;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
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

    public List<Integer> getListaIds() {
        return listaIds;
    }

    public void setListaIds(List<Integer> listaIds) {
        this.listaIds = listaIds;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
    
    
}

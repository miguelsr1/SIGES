package gob.mined.siap2.filtros;


public class FiltroTransferenciaDireccionDepartamental {
    
    private Integer idTransferenciaComponente;
    private Integer idDireccionDepartamental;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    
    
    
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

    public Integer getIdTransferenciaComponente() {
        return idTransferenciaComponente;
    }

    public void setIdTransferenciaComponente(Integer idTransferenciaComponente) {
        this.idTransferenciaComponente = idTransferenciaComponente;
    }

    public Integer getIdDireccionDepartamental() {
        return idDireccionDepartamental;
    }

    public void setIdDireccionDepartamental(Integer idDireccionDepartamental) {
        this.idDireccionDepartamental = idDireccionDepartamental;
    }
    
    
}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroCasoViolacion implements Serializable {
    
    private Long cviSede;
    private Long cviAnio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroCasoViolacion() {
    }

    public Long getCviSede() {
        return cviSede;
    }

    public void setCviSede(Long cviSede) {
        this.cviSede = cviSede;
    }

    public Long getCviAnio() {
        return cviAnio;
    }

    public void setCviAnio(Long cviAnio) {
        this.cviAnio = cviAnio;
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
       
}
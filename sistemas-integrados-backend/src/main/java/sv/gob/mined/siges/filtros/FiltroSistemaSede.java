/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;


public class FiltroSistemaSede implements Serializable {
    
    private Long sistema;
    private Long sede;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    private String[] incluirCampos;
    
    public FiltroSistemaSede() {
        
    }

    public Long getSistema() {
        return sistema;
    }

    public void setSistema(Long sistema) {
        this.sistema = sistema;
    }

    public Long getSede() {
        return sede;
    }

    public void setSede(Long sede) {
        this.sede = sede;
    }

    
    


    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
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

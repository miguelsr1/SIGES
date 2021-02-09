/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroTitulo implements Serializable {
    
    private String titHash;
    private Long titNie;
    private Boolean titNoAnulada;
   
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroTitulo() {
    }

    public String getTitHash() {
        return titHash;
    }

    public void setTitHash(String titHash) {
        this.titHash = titHash;
    }

    public Long getTitNie() {
        return titNie;
    }

    public void setTitNie(Long titNie) {
        this.titNie = titNie;
    }       

    public Boolean getTitNoAnulada() {
        return titNoAnulada;
    }

    public void setTitNoAnulada(Boolean titNoAnulada) {
        this.titNoAnulada = titNoAnulada;
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
}

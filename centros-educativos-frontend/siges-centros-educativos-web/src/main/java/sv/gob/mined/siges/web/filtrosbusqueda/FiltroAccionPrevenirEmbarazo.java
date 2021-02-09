/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroAccionPrevenirEmbarazo implements Serializable {
    
    private Long apeSede;
    private Long apeAnio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroAccionPrevenirEmbarazo() {
    }

    public Long getApeSede() {
        return apeSede;
    }

    public void setApeSede(Long apeSede) {
        this.apeSede = apeSede;
    }

    public Long getApeAnio() {
        return apeAnio;
    }

    public void setApeAnio(Long apeAnio) {
        this.apeAnio = apeAnio;
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

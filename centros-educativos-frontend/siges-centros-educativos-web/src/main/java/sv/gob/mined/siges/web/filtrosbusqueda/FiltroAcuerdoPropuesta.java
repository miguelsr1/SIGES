/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroAcuerdoPropuesta implements Serializable {

    private Long rapPropuestaPedagogica;
    private Long rapAcuerdoSede;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroAcuerdoPropuesta() {
    }

    public Long getRapPropuestaPedagogica() {
        return rapPropuestaPedagogica;
    }

    public void setRapPropuestaPedagogica(Long rapPropuestaPedagogica) {
        this.rapPropuestaPedagogica = rapPropuestaPedagogica;
    }

    public Long getRapAcuerdoSede() {
        return rapAcuerdoSede;
    }

    public void setRapAcuerdoSede(Long rapAcuerdoSede) {
        this.rapAcuerdoSede = rapAcuerdoSede;
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

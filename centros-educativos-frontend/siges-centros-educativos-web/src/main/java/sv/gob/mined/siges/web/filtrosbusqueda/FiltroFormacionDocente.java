/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroFormacionDocente implements Serializable {

    private Long fmdPk;
    private Long fmdPersonalSede;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroFormacionDocente() {
    }

    public Long getFmdPk() {
        return fmdPk;
    }

    public void setFmdPk(Long fmdPk) {
        this.fmdPk = fmdPk;
    }

    public Long getFmdPersonalSede() {
        return fmdPersonalSede;
    }

    public void setFmdPersonalSede(Long fmdPersonalSede) {
        this.fmdPersonalSede = fmdPersonalSede;
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

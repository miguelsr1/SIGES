/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroEstudioRealizado implements Serializable {

    private Long erePk;
    private Long erePersonalSede;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroEstudioRealizado() {
    }

    public Long getErePk() {
        return erePk;
    }

    public void setErePk(Long erePk) {
        this.erePk = erePk;
    }

    public Long getErePersonalSede() {
        return erePersonalSede;
    }

    public void setErePersonalSede(Long erePersonalSede) {
        this.erePersonalSede = erePersonalSede;
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

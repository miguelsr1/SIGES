/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroComponenteDocente implements Serializable {

    private Long cdoHorarioEscolar;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroComponenteDocente() {
    }

    public Long getCdoHorarioEscolar() {
        return cdoHorarioEscolar;
    }

    public void setCdoHorarioEscolar(Long cdoHorarioEscolar) {
        this.cdoHorarioEscolar = cdoHorarioEscolar;
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

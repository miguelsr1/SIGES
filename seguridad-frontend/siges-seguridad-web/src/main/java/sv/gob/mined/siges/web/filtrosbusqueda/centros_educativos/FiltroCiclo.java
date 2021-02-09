/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos;

import java.io.Serializable;

public class FiltroCiclo implements Serializable {

    private Long nivPk;
    private Boolean cicHabilitado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCiclo() {
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
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

    public Boolean getCicHabilitado() {
        return cicHabilitado;
    }

    public void setCicHabilitado(Boolean cicHabilitado) {
        this.cicHabilitado = cicHabilitado;
    }
    

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;




/**
 * Filtro del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroEvaluacionItemLiquidacion implements Serializable {

    private Long eilPk;
    private Long eilLiqFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroEvaluacionItemLiquidacion() {
    }

    public Long getEilPk() {
        return eilPk;
    }

    public void setEilPk(Long eilPk) {
        this.eilPk = eilPk;
    }

    public Long getEilLiqFk() {
        return eilLiqFk;
    }

    public void setEilLiqFk(Long eilLiqFk) {
        this.eilLiqFk = eilLiqFk;
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

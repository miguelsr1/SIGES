/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

public class FiltroRelOpcionProgEd {

    private Long roeOpcionPk;
    private Long roeProgramaEducativoPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRelOpcionProgEd() {
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

    public Long getRoeOpcionPk() {
        return roeOpcionPk;
    }

    public void setRoeOpcionPk(Long roeOpcionPk) {
        this.roeOpcionPk = roeOpcionPk;
    }

    public Long getRoeProgramaEducativoPk() {
        return roeProgramaEducativoPk;
    }

    public void setRoeProgramaEducativoPk(Long roeProgramaEducativoPk) {
        this.roeProgramaEducativoPk = roeProgramaEducativoPk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    
    

    
    
    
}

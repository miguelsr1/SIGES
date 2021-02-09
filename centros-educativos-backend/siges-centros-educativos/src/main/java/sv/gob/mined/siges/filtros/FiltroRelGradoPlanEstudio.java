/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

public class FiltroRelGradoPlanEstudio {

    private Long rgpGrado;
    private Long rgpPlanEstudio;
    private Long rgpFormula;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRelGradoPlanEstudio() {
    }

    public Long getRgpGrado() {
        return rgpGrado;
    }

    public void setRgpGrado(Long rgpGrado) {
        this.rgpGrado = rgpGrado;
    }

    public Long getRgpPlanEstudio() {
        return rgpPlanEstudio;
    }

    public void setRgpPlanEstudio(Long rgpPlanEstudio) {
        this.rgpPlanEstudio = rgpPlanEstudio;
    }

    public Long getRgpFormula() {
        return rgpFormula;
    }

    public void setRgpFormula(Long rgpFormula) {
        this.rgpFormula = rgpFormula;
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

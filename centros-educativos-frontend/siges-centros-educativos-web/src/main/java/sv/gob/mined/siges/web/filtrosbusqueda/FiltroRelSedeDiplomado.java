package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroRelSedeDiplomado implements Serializable {

    private Long rsdSedePk;
    private Long rsdDiplomadoPk;
    private Boolean rsdHabilitado;
   

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRelSedeDiplomado() {

    }

    public Long getRsdSedePk() {
        return rsdSedePk;
    }

    public void setRsdSedePk(Long rsdSedePk) {
        this.rsdSedePk = rsdSedePk;
    }

    public Long getRsdDiplomadoPk() {
        return rsdDiplomadoPk;
    }

    public void setRsdDiplomadoPk(Long rsdDiplomadoPk) {
        this.rsdDiplomadoPk = rsdDiplomadoPk;
    }

    public Boolean getRsdHabilitado() {
        return rsdHabilitado;
    }

    public void setRsdHabilitado(Boolean rsdHabilitado) {
        this.rsdHabilitado = rsdHabilitado;
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

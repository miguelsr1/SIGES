package sv.gob.mined.siges.filtros;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroModuloDiplomado implements Serializable {

    private Long mdiPk;
    private Long diplomadoFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroModuloDiplomado() {

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

    public Long getMdiPk() {
        return mdiPk;
    }

    public void setMdiPk(Long mdiPk) {
        this.mdiPk = mdiPk;
    }

    public Long getDiplomadoFk() {
        return diplomadoFk;
    }

    public void setDiplomadoFk(Long diplomadoFk) {
        this.diplomadoFk = diplomadoFk;
    }

   
   
    
    

}

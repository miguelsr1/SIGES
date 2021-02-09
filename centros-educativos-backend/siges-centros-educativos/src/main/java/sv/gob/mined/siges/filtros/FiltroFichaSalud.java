package sv.gob.mined.siges.filtros;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroFichaSalud implements Serializable {
    
    private Long fichaEstudianteFk;
    private Boolean incluirENT;
    
    
    private String[] incluirCampos;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    
    public FiltroFichaSalud(){
         this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getFichaEstudianteFk() {
        return fichaEstudianteFk;
    }

    public void setFichaEstudianteFk(Long fichaEstudianteFk) {
        this.fichaEstudianteFk = fichaEstudianteFk;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }


    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getIncluirENT() {
        return incluirENT;
    }

    public void setIncluirENT(Boolean incluirENT) {
        this.incluirENT = incluirENT;
    }
    
    
}

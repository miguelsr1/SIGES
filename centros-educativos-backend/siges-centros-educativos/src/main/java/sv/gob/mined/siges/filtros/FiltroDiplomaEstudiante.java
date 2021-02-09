/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

/**
 *
 * @author Sofis Solutions
 */
import java.io.Serializable;

public class FiltroDiplomaEstudiante  implements Serializable {
    
    private Long estudianteFk;
    private Long diplomaFk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

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

    public Long getEstudianteFk() {
        return estudianteFk;
    }

    public void setEstudianteFk(Long estudianteFk) {
        this.estudianteFk = estudianteFk;
    }

    public Long getDiplomaFk() {
        return diplomaFk;
    }

    public void setDiplomaFk(Long diplomaFk) {
        this.diplomaFk = diplomaFk;
    }


    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

   
}

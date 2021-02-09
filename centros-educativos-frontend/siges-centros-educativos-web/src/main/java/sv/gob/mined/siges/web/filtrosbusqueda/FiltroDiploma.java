/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

/**
 *
 * @author Sofis Solutions
 */
import java.io.Serializable;

public class FiltroDiploma  implements Serializable {
    
    private Long diplomadoFk;
    private Long anioLectivoId;
    private Long departamentoPk;
    private Long sedePk;
    private Long municipioPk;
    private Boolean incluirDiplomadoEstudiante;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

   
    public Long getAnioLectivoId() {
        return anioLectivoId;
    }

    public void setAnioLectivoId(Long anioLectivoId) {
        this.anioLectivoId = anioLectivoId;
    }

    public Long getDiplomadoFk() {
        return diplomadoFk;
    }

    public void setDiplomadoFk(Long diplomadoFk) {
        this.diplomadoFk = diplomadoFk;
    }

    public Long getMunicipioPk() {
        return municipioPk;
    }

    public void setMunicipioPk(Long municipioPk) {
        this.municipioPk = municipioPk;
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

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Boolean getIncluirDiplomadoEstudiante() {
        return incluirDiplomadoEstudiante;
    }

    public void setIncluirDiplomadoEstudiante(Boolean incluirDiplomadoEstudiante) {
        this.incluirDiplomadoEstudiante = incluirDiplomadoEstudiante;
    }

   
}

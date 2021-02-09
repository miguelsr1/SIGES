package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroEscolaridadEstudiante implements Serializable {
    
    private List<Long> estudiantesPk;
    private Long anioPk;
    private Long servicioEducativoPk;
    private Long estudiantePk;
    private Long matriculaPk;
    private Boolean escGeneradaPorEquivalencia;
    private EnumPromovidoCalificacion resultado;
 
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    private String[] incluirCampos;
    
    public FiltroEscolaridadEstudiante(){
         this.seNecesitaDistinct = Boolean.FALSE;
    }

    public List<Long> getEstudiantesPk() {
        return estudiantesPk;
    }

    public void setEstudiantesPk(List<Long> estudiantesPk) {
        this.estudiantesPk = estudiantesPk;
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

    public Long getAnioPk() {
        return anioPk;
    }

    public void setAnioPk(Long anioPk) {
        this.anioPk = anioPk;
    }

    public Long getServicioEducativoPk() {
        return servicioEducativoPk;
    }

    public void setServicioEducativoPk(Long servicioEducativoPk) {
        this.servicioEducativoPk = servicioEducativoPk;
    }

    public Long getEstudiantePk() {
        return estudiantePk;
    }

    public void setEstudiantePk(Long estudiantePk) {
        this.estudiantePk = estudiantePk;
    }

    public Long getMatriculaPk() {
        return matriculaPk;
    }

    public void setMatriculaPk(Long matriculaPk) {
        this.matriculaPk = matriculaPk;
    }

    public Boolean getEscGeneradaPorEquivalencia() {
        return escGeneradaPorEquivalencia;
    }

    public void setEscGeneradaPorEquivalencia(Boolean escGeneradaPorEquivalencia) {
        this.escGeneradaPorEquivalencia = escGeneradaPorEquivalencia;
    }

    public EnumPromovidoCalificacion getResultado() {
        return resultado;
    }

    public void setResultado(EnumPromovidoCalificacion resultado) {
        this.resultado = resultado;
    }
    
    
    
}

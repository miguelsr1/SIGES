package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroEscolaridadEstudiante implements Serializable {
    
    private List<Long> estudiantesPk;
    private Long anioPk;
    private Long servicioEducativoPk;
    private Long estudiantePk;
 
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroEscolaridadEstudiante(){
         this.first = 0L;
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
    
}

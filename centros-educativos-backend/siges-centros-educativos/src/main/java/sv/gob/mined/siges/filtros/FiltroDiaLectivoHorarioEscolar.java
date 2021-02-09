package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumCeldaDiaHora;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroDiaLectivoHorarioEscolar implements Serializable {
    

    private Long horarioEscolarFk;
    private Long seccionFk;
    private EnumCeldaDiaHora celdaDiaHora;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    private String[] incluirCampos;
    
    public FiltroDiaLectivoHorarioEscolar(){
         this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getHorarioEscolarFk() {
        return horarioEscolarFk;
    }

    public void setHorarioEscolarFk(Long horarioEscolarFk) {
        this.horarioEscolarFk = horarioEscolarFk;
    }

    public Long getSeccionFk() {
        return seccionFk;
    }

    public void setSeccionFk(Long seccionFk) {
        this.seccionFk = seccionFk;
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

    public EnumCeldaDiaHora getCeldaDiaHora() {
        return celdaDiaHora;
    }

    public void setCeldaDiaHora(EnumCeldaDiaHora celdaDiaHora) {
        this.celdaDiaHora = celdaDiaHora;
    }

}

package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroAnioLectivo implements Serializable {
    
    private Long alePk;
    private EnumAnioLectivoEstado aleEstado;
    private Long aleGradoFk;
    private Long aleSedeFk;
    private Integer aleAnio;
    private EnumSeccionEstado aleSeccionEstado;
    private Long aleServicioEducativo;
    private Boolean aleCorriente;
    
    private String[] incluirCampos;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    
    public FiltroAnioLectivo(){
         this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getAlePk() {
        return alePk;
    }

    public void setAlePk(Long alePk) {
        this.alePk = alePk;
    }

    public EnumAnioLectivoEstado getAleEstado() {
        return aleEstado;
    }

    public void setAleEstado(EnumAnioLectivoEstado aleEstado) {
        this.aleEstado = aleEstado;
    }

    public Long getAleGradoFk() {
        return aleGradoFk;
    }

    public void setAleGradoFk(Long aleGradoFk) {
        this.aleGradoFk = aleGradoFk;
    }

    public Long getAleSedeFk() {
        return aleSedeFk;
    }

    public void setAleSedeFk(Long aleSedeFk) {
        this.aleSedeFk = aleSedeFk;
    }

    public Integer getAleAnio() {
        return aleAnio;
    }

    public void setAleAnio(Integer aleAnio) {
        this.aleAnio = aleAnio;
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

    public EnumSeccionEstado getAleSeccionEstado() {
        return aleSeccionEstado;
    }

    public void setAleSeccionEstado(EnumSeccionEstado aleSeccionEstado) {
        this.aleSeccionEstado = aleSeccionEstado;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getAleServicioEducativo() {
        return aleServicioEducativo;
    }

    public void setAleServicioEducativo(Long aleServicioEducativo) {
        this.aleServicioEducativo = aleServicioEducativo;
    }

    public Boolean getAleCorriente() {
        return aleCorriente;
    }

    public void setAleCorriente(Boolean aleCorriente) {
        this.aleCorriente = aleCorriente;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    
    
}

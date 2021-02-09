package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroAcuerdo implements Serializable {

    private Long acuPropuestaPedagogica;
    private Long sistemaIntegradoPk;
    private EnumEstadoAcuerdo estadoAcuerdo;
    private Long sedePk;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean seNecesitaDistinct;

    public FiltroAcuerdo() {
        seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public EnumEstadoAcuerdo getEstadoAcuerdo() {
        return estadoAcuerdo;
    }

    public void setEstadoAcuerdo(EnumEstadoAcuerdo estadoAcuerdo) {
        this.estadoAcuerdo = estadoAcuerdo;
    }

    public Long getAcuPropuestaPedagogica() {
        return acuPropuestaPedagogica;
    }

    public void setAcuPropuestaPedagogica(Long acuPropuestaPedagogica) {
        this.acuPropuestaPedagogica = acuPropuestaPedagogica;
    }

    public Long getSistemaIntegradoPk() {
        return sistemaIntegradoPk;
    }

    public void setSistemaIntegradoPk(Long sistemaIntegradoPk) {
        this.sistemaIntegradoPk = sistemaIntegradoPk;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

}

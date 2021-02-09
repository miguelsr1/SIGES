/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

package sv.gob.mined.siges.web.filtrosbusqueda;
import java.io.Serializable;

public class FiltroCalificacionesPendientesSedeEnNivel implements Serializable {
    
    private Long sedPk;
    private Long sedDepartamentoId;
    private Long sedMunicipioId;
    private Long sedNivel;
    private Long calNacionalAnioLectivoPk;
    private Long calInternacionalAnioLectivoPk;
    private Boolean incluirAdscritas;

    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
        

    public FiltroCalificacionesPendientesSedeEnNivel() {
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

   

    public Long getSedDepartamentoId() {
        return sedDepartamentoId;
    }

    public void setSedDepartamentoId(Long sedDepartamentoId) {
        this.sedDepartamentoId = sedDepartamentoId;
    }

    public Long getSedMunicipioId() {
        return sedMunicipioId;
    }

    public void setSedMunicipioId(Long sedMunicipioId) {
        this.sedMunicipioId = sedMunicipioId;
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


    public Long getSedNivel() {
        return sedNivel;
    }

    public void setSedNivel(Long sedNivel) {
        this.sedNivel = sedNivel;
    }

    public Long getCalNacionalAnioLectivoPk() {
        return calNacionalAnioLectivoPk;
    }

    public void setCalNacionalAnioLectivoPk(Long calNacionalAnioLectivoPk) {
        this.calNacionalAnioLectivoPk = calNacionalAnioLectivoPk;
    }

    public Long getCalInternacionalAnioLectivoPk() {
        return calInternacionalAnioLectivoPk;
    }

    public void setCalInternacionalAnioLectivoPk(Long calInternacionalAnioLectivoPk) {
        this.calInternacionalAnioLectivoPk = calInternacionalAnioLectivoPk;
    }

    public Boolean getIncluirAdscritas() {
        return incluirAdscritas;
    }

    public void setIncluirAdscritas(Boolean incluirAdscritas) {
        this.incluirAdscritas = incluirAdscritas;
    }
    
    
}


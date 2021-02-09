/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroDetallePlanEscolar implements Serializable {

    private Long dpePk;
    private Boolean planEscolarHabilitado;
    private Long sistemaPk;
    private Long sedePk;
    private Long fuenteFinanciamientoPk;
    private Long anioFk;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDetallePlanEscolar() {
    }

    public Long getDpePk() {
        return dpePk;
    }

    public void setDpePk(Long dpePk) {
        this.dpePk = dpePk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getFuenteFinanciamientoPk() {
        return fuenteFinanciamientoPk;
    }

    public void setFuenteFinanciamientoPk(Long fuenteFinanciamientoPk) {
        this.fuenteFinanciamientoPk = fuenteFinanciamientoPk;
    }

    public Boolean getPlanEscolarHabilitado() {
        return planEscolarHabilitado;
    }

    public void setPlanEscolarHabilitado(Boolean planEscolarHabilitado) {
        this.planEscolarHabilitado = planEscolarHabilitado;
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

    public Long getSistemaPk() {
        return sistemaPk;
    }

    public void setSistemaPk(Long sistemaPk) {
        this.sistemaPk = sistemaPk;
    }

    public Long getAnioFk() {
        return anioFk;
    }

    public void setAnioFk(Long anioFk) {
        this.anioFk = anioFk;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.dto.SgPlanEscolarAnual;

public class FiltroDetallePlanEscolar implements Serializable {

    private Long dpePk;
    private Boolean planEscolarHabilitado;
    private Long sistemaPk;
    private Long sedePk;
    private Long fuenteFinanciamientoPk;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private String[] dpeActividad;
    private SgPlanEscolarAnual dpePlanEscolarAnual;
    private Integer dpeVersion;

    public FiltroDetallePlanEscolar() {
    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

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

    public String[] getDpeActividad() {
        return dpeActividad;
    }

    public void setDpeActividad(String[] dpeActividad) {
        this.dpeActividad = dpeActividad;
    }

    public SgPlanEscolarAnual getDpePlanEscolarAnual() {
        return dpePlanEscolarAnual;
    }

    public void setDpePlanEscolarAnual(SgPlanEscolarAnual dpePlanEscolarAnual) {
        this.dpePlanEscolarAnual = dpePlanEscolarAnual;
    }

    public Integer getDpeVersion() {
        return dpeVersion;
    }

    public void setDpeVersion(Integer dpeVersion) {
        this.dpeVersion = dpeVersion;
    }
    // </editor-fold>
}

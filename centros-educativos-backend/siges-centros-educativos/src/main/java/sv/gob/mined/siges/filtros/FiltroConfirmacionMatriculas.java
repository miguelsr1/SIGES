/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroConfirmacionMatriculas implements Serializable {
    
    private Long sedeFk;
    private Long anioLectivoFk;
    private Long departamentoFk;
    private Long municipioFk;
    private Long cmaPk;
    private Boolean firmada;
    private String firmaTransactionId;
    private Boolean confirmada; //Quiere decir que tiene fechaConfirmacion distinto de null
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroConfirmacionMatriculas() {
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

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Long getAnioLectivoFk() {
        return anioLectivoFk;
    }

    public void setAnioLectivoFk(Long anioLectivoFk) {
        this.anioLectivoFk = anioLectivoFk;
    }

    public Long getDepartamentoFk() {
        return departamentoFk;
    }

    public void setDepartamentoFk(Long departamentoFk) {
        this.departamentoFk = departamentoFk;
    }

    public Long getMunicipioFk() {
        return municipioFk;
    }

    public void setMunicipioFk(Long municipioFk) {
        this.municipioFk = municipioFk;
    }

    public Long getCmaPk() {
        return cmaPk;
    }

    public void setCmaPk(Long cmaPk) {
        this.cmaPk = cmaPk;
    }

    public Boolean getFirmada() {
        return firmada;
    }

    public void setFirmada(Boolean firmada) {
        this.firmada = firmada;
    }

    public String getFirmaTransactionId() {
        return firmaTransactionId;
    }

    public void setFirmaTransactionId(String firmaTransactionId) {
        this.firmaTransactionId = firmaTransactionId;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
    
    
    
    
    
    
}

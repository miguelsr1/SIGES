/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroConfirmacionPromocion implements Serializable {
    
    private Long seccionFk;
    private Long cprPk;
    private Boolean firmada;
    private String firmaTransactionId;
    private Boolean confirmada; //Quiere decir que tiene fechaConfirmacion distinto de null
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroConfirmacionPromocion() {
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

    public Long getSeccionFk() {
        return seccionFk;
    }

    public void setSeccionFk(Long seccionFk) {
        this.seccionFk = seccionFk;
    }

    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
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

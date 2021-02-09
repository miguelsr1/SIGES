/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroConfirmacionSolicitudTraslado implements Serializable {
    
    private Long sotPk;
    private Long solicitudTrasladoFk;
    private String firmaTransactionId;  
    private Boolean firmada;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroConfirmacionSolicitudTraslado() {
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

    public String getFirmaTransactionId() {
        return firmaTransactionId;
    }

    public void setFirmaTransactionId(String firmaTransactionId) {
        this.firmaTransactionId = firmaTransactionId;
    }

    public Long getSolicitudTrasladoFk() {
        return solicitudTrasladoFk;
    }

    public void setSolicitudTrasladoFk(Long solicitudTrasladoFk) {
        this.solicitudTrasladoFk = solicitudTrasladoFk;
    }

    public Long getSotPk() {
        return sotPk;
    }

    public void setSotPk(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Boolean getFirmada() {
        return firmada;
    }

    public void setFirmada(Boolean firmada) {
        this.firmada = firmada;
    }
    
    
}

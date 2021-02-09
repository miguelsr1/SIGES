/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroFactorRiesgoSede implements Serializable {
    
    private Long friSede;
    private Boolean friRiesgoAmbiental;
    private Boolean friRiesgoSocial;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroFactorRiesgoSede() {
    }

    public Long getFriSede() {
        return friSede;
    }

    public void setFriSede(Long friSede) {
        this.friSede = friSede;
    }

    public Boolean getFriRiesgoAmbiental() {
        return friRiesgoAmbiental;
    }

    public void setFriRiesgoAmbiental(Boolean friRiesgoAmbiental) {
        this.friRiesgoAmbiental = friRiesgoAmbiental;
    }

    public Boolean getFriRiesgoSocial() {
        return friRiesgoSocial;
    }

    public void setFriRiesgoSocial(Boolean friRiesgoSocial) {
        this.friRiesgoSocial = friRiesgoSocial;
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
       
}
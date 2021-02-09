/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;


public class FiltroReqFondoCed implements Serializable {
    
  
    private long rfcPk;
    private Long rfcTacFk;
    private Long rfcStrFk;
    private EnumEstadoOrganismoAdministrativo estadoOae;
        
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroReqFondoCed() {
    }

    public long getRfcPk() {
        return rfcPk;
    }

    public void setRfcPk(long rfcPk) {
        this.rfcPk = rfcPk;
    }

    public Long getRfcTacFk() {
        return rfcTacFk;
    }

    public void setRfcTacFk(Long rfcTacFk) {
        this.rfcTacFk = rfcTacFk;
    }

    public Long getRfcStrFk() {
        return rfcStrFk;
    }

    public void setRfcStrFk(Long rfcStrFk) {
        this.rfcStrFk = rfcStrFk;
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

    public EnumEstadoOrganismoAdministrativo getEstadoOae() {
        return estadoOae;
    }

    public void setEstadoOae(EnumEstadoOrganismoAdministrativo estadoOae) {
        this.estadoOae = estadoOae;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.rfcPk ^ (this.rfcPk >>> 32));
        hash = 11 * hash + Objects.hashCode(this.rfcTacFk);
        hash = 11 * hash + Objects.hashCode(this.rfcStrFk);
        hash = 11 * hash + Objects.hashCode(this.estadoOae);
        hash = 11 * hash + Objects.hashCode(this.first);
        hash = 11 * hash + Objects.hashCode(this.maxResults);
        hash = 11 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 11 * hash + Arrays.hashCode(this.ascending);
        hash = 11 * hash + Arrays.deepHashCode(this.incluirCampos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroReqFondoCed other = (FiltroReqFondoCed) obj;
        if (this.rfcPk != other.rfcPk) {
            return false;
        }
        return true;
    }

    
}

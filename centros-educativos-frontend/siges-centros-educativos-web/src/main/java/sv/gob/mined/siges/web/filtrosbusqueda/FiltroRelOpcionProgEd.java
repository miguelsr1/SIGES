/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroRelOpcionProgEd implements Serializable {

    private Long roeOpcionPk;
    private Long roeProgramaEducativoPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroRelOpcionProgEd() {
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

    public Long getRoeOpcionPk() {
        return roeOpcionPk;
    }

    public void setRoeOpcionPk(Long roeOpcionPk) {
        this.roeOpcionPk = roeOpcionPk;
    }

    public Long getRoeProgramaEducativoPk() {
        return roeProgramaEducativoPk;
    }

    public void setRoeProgramaEducativoPk(Long roeProgramaEducativoPk) {
        this.roeProgramaEducativoPk = roeProgramaEducativoPk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.roeOpcionPk);
        hash = 59 * hash + Objects.hashCode(this.roeProgramaEducativoPk);
        hash = 59 * hash + Objects.hashCode(this.first);
        hash = 59 * hash + Objects.hashCode(this.maxResults);
        hash = 59 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 59 * hash + Arrays.hashCode(this.ascending);
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
        final FiltroRelOpcionProgEd other = (FiltroRelOpcionProgEd) obj;
        if (!Objects.equals(this.roeOpcionPk, other.roeOpcionPk)) {
            return false;
        }
        if (!Objects.equals(this.roeProgramaEducativoPk, other.roeProgramaEducativoPk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        return true;
    }
    
    

}

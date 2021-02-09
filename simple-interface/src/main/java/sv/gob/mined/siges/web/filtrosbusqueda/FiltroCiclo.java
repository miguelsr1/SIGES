/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroCiclo implements Serializable {
    
    private Long nivPk;
    private Boolean cicHabilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    public FiltroCiclo() {
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
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

    public Boolean getCicHabilitado() {
        return cicHabilitado;
    }

    public void setCicHabilitado(Boolean cicHabilitado) {
        this.cicHabilitado = cicHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.nivPk);
        hash = 83 * hash + Objects.hashCode(this.cicHabilitado);
        hash = 83 * hash + Objects.hashCode(this.first);
        hash = 83 * hash + Objects.hashCode(this.maxResults);
        hash = 83 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 83 * hash + Arrays.hashCode(this.ascending);
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
        final FiltroCiclo other = (FiltroCiclo) obj;
        if (!Objects.equals(this.nivPk, other.nivPk)) {
            return false;
        }
        if (!Objects.equals(this.cicHabilitado, other.cicHabilitado)) {
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

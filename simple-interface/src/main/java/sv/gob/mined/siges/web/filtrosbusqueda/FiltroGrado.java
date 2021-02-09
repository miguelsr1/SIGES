/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroGrado implements Serializable {
    
    private Long modPk;
    private Long modAtencionPk;
    private Long subModAtencionPk;
    private Long sedePk;
    private Boolean habilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public Long getModPk() {
        return modPk;
    }

    public void setModPk(Long modPk) {
        this.modPk = modPk;
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

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public Long getModAtencionPk() {
        return modAtencionPk;
    }

    public void setModAtencionPk(Long modAtencionPk) {
        this.modAtencionPk = modAtencionPk;
    }

    public Long getSubModAtencionPk() {
        return subModAtencionPk;
    }

    public void setSubModAtencionPk(Long subModAtencionPk) {
        this.subModAtencionPk = subModAtencionPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.modPk);
        hash = 41 * hash + Objects.hashCode(this.modAtencionPk);
        hash = 41 * hash + Objects.hashCode(this.subModAtencionPk);
        hash = 41 * hash + Objects.hashCode(this.sedePk);
        hash = 41 * hash + Objects.hashCode(this.habilitado);
        hash = 41 * hash + Objects.hashCode(this.first);
        hash = 41 * hash + Objects.hashCode(this.maxResults);
        hash = 41 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 41 * hash + Arrays.hashCode(this.ascending);
        hash = 41 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroGrado other = (FiltroGrado) obj;
        if (!Objects.equals(this.modPk, other.modPk)) {
            return false;
        }
        if (!Objects.equals(this.modAtencionPk, other.modAtencionPk)) {
            return false;
        }
        if (!Objects.equals(this.subModAtencionPk, other.subModAtencionPk)) {
            return false;
        }
        if (!Objects.equals(this.sedePk, other.sedePk)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
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
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }
    
    
}

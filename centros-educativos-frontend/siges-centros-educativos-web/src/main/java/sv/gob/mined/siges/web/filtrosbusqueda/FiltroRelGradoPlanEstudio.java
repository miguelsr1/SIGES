/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroRelGradoPlanEstudio implements Serializable {

    private Long rgpGrado;
    private Long rgpPlanEstudio;
    private Long rgpFormula;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRelGradoPlanEstudio() {
    }

    public Long getRgpGrado() {
        return rgpGrado;
    }

    public void setRgpGrado(Long rgpGrado) {
        this.rgpGrado = rgpGrado;
    }

    public Long getRgpPlanEstudio() {
        return rgpPlanEstudio;
    }

    public void setRgpPlanEstudio(Long rgpPlanEstudio) {
        this.rgpPlanEstudio = rgpPlanEstudio;
    }

    public Long getRgpFormula() {
        return rgpFormula;
    }

    public void setRgpFormula(Long rgpFormula) {
        this.rgpFormula = rgpFormula;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.rgpGrado);
        hash = 97 * hash + Objects.hashCode(this.rgpPlanEstudio);
        hash = 97 * hash + Objects.hashCode(this.rgpFormula);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRelGradoPlanEstudio other = (FiltroRelGradoPlanEstudio) obj;
        if (!Objects.equals(this.rgpGrado, other.rgpGrado)) {
            return false;
        }
        if (!Objects.equals(this.rgpPlanEstudio, other.rgpPlanEstudio)) {
            return false;
        }
        if (!Objects.equals(this.rgpFormula, other.rgpFormula)) {
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

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroRelOAEIdentificacionOAE implements Serializable {
    
    
    private Long rioOrganismoAdministracionEscolarFk;
    private Long rioIdentificacionOAEfk;
    private Boolean obligatorio;
    private Boolean identificacionHabilitada;
      
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    private Boolean seNecesitaDistinct;

    public FiltroRelOAEIdentificacionOAE() {
    }

    public Long getRioOrganismoAdministracionEscolarFk() {
        return rioOrganismoAdministracionEscolarFk;
    }

    public void setRioOrganismoAdministracionEscolarFk(Long rioOrganismoAdministracionEscolarFk) {
        this.rioOrganismoAdministracionEscolarFk = rioOrganismoAdministracionEscolarFk;
    }
    
    public Long getRioIdentificacionOAEfk() {
        return rioIdentificacionOAEfk;
    }

    public void setRioIdentificacionOAEfk(Long rioIdentificacionOAEfk) {
        this.rioIdentificacionOAEfk = rioIdentificacionOAEfk;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Boolean getIdentificacionHabilitada() {
        return identificacionHabilitada;
    }

    public void setIdentificacionHabilitada(Boolean identificacionHabilitada) {
        this.identificacionHabilitada = identificacionHabilitada;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.first);
        hash = 71 * hash + Objects.hashCode(this.maxResults);
        hash = 71 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 71 * hash + Arrays.hashCode(this.ascending);
        hash = 71 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRelOAEIdentificacionOAE other = (FiltroRelOAEIdentificacionOAE) obj;
      
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

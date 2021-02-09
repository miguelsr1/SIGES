/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroRelModEdModAten implements Serializable {

    private Long reaModalidadEducativa;
    private Long reaModalidadAtencion;
    private Long reaSubModalidad;
    private Long reaNivel;

    private Boolean inicializarGrados = Boolean.FALSE;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRelModEdModAten() {
    }

    public Long getReaModalidadEducativa() {
        return reaModalidadEducativa;
    }

    public void setReaModalidadEducativa(Long reaModalidadEducativa) {
        this.reaModalidadEducativa = reaModalidadEducativa;
    }

    public Boolean getInicializarGrados() {
        return inicializarGrados;
    }

    public void setInicializarGrados(Boolean inicializarGrados) {
        this.inicializarGrados = inicializarGrados;
    }

    public Long getReaModalidadAtencion() {
        return reaModalidadAtencion;
    }

    public void setReaModalidadAtencion(Long reaModalidadAtencion) {
        this.reaModalidadAtencion = reaModalidadAtencion;
    }

    public Long getReaSubModalidad() {
        return reaSubModalidad;
    }

    public void setReaSubModalidad(Long reaSubModalidad) {
        this.reaSubModalidad = reaSubModalidad;
    }

    public Long getReaNivel() {
        return reaNivel;
    }

    public void setReaNivel(Long reaNivel) {
        this.reaNivel = reaNivel;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.reaModalidadEducativa);
        hash = 37 * hash + Objects.hashCode(this.reaModalidadAtencion);
        hash = 37 * hash + Objects.hashCode(this.reaSubModalidad);
        hash = 37 * hash + Objects.hashCode(this.reaNivel);
        hash = 37 * hash + Objects.hashCode(this.inicializarGrados);
        hash = 37 * hash + Objects.hashCode(this.first);
        hash = 37 * hash + Objects.hashCode(this.maxResults);
        hash = 37 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 37 * hash + Arrays.hashCode(this.ascending);
        hash = 37 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRelModEdModAten other = (FiltroRelModEdModAten) obj;
        if (!Objects.equals(this.reaModalidadEducativa, other.reaModalidadEducativa)) {
            return false;
        }
        if (!Objects.equals(this.reaModalidadAtencion, other.reaModalidadAtencion)) {
            return false;
        }
        if (!Objects.equals(this.reaSubModalidad, other.reaSubModalidad)) {
            return false;
        }
        if (!Objects.equals(this.reaNivel, other.reaNivel)) {
            return false;
        }
        if (!Objects.equals(this.inicializarGrados, other.inicializarGrados)) {
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

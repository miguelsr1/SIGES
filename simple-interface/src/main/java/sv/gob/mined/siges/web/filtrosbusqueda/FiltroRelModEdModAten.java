/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;

public class FiltroRelModEdModAten implements Serializable {

    private Long reaModalidadEducativa;
    private Long reaModalidadAtencion;
    private Long reaSubModalidad;
    private Long reaNivel;

    private Boolean inicializarGrados = Boolean.FALSE;
    private Long first;
    private Long maxResults;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.reaModalidadEducativa);
        hash = 19 * hash + Objects.hashCode(this.reaModalidadAtencion);
        hash = 19 * hash + Objects.hashCode(this.reaSubModalidad);
        hash = 19 * hash + Objects.hashCode(this.reaNivel);
        hash = 19 * hash + Objects.hashCode(this.inicializarGrados);
        hash = 19 * hash + Objects.hashCode(this.first);
        hash = 19 * hash + Objects.hashCode(this.maxResults);
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
        return true;
    }
    
    
}

/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoSede;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSedeLite.class)
public class SgSedeLite implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long sedPk;
    private String sedCodigo;
    private String sedNombre;
    private String sedNombreBusqueda;
    private Boolean sedHabilitado;
    private TipoSede sedTipo;

    public SgSedeLite() {
        this.sedHabilitado = Boolean.TRUE;
    }

    public String getSedCodigoNombre() {
        return this.sedCodigo + " - " + this.sedNombre;
    }
    
    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public String getSedNombreBusqueda() {
        return sedNombreBusqueda;
    }

    public void setSedNombreBusqueda(String sedNombreBusqueda) {
        this.sedNombreBusqueda = sedNombreBusqueda;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.sedPk);
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
        final SgSedeLite other = (SgSedeLite) obj;
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgSedeLite[ sedPk=" + sedPk + " ]";
    }
    
}

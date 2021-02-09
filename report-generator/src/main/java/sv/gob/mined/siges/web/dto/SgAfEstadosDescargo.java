/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "edePk", scope = SgAfEstadosDescargo.class)
public class SgAfEstadosDescargo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long edePk;
    private String edeCodigo;
    private String edeNombre;
    private String edeNombreBusqueda;
    private Boolean edeHabilitado;
    private LocalDateTime edeUltModFecha;
    private String edeUltModUsuario;
    private Integer edeVersion;

    public SgAfEstadosDescargo() {
        edeHabilitado = Boolean.TRUE;
    }

    public Long getEdePk() {
        return edePk;
    }

    public void setEdePk(Long edePk) {
        this.edePk = edePk;
    }

    public String getEdeCodigo() {
        return edeCodigo;
    }

    public void setEdeCodigo(String edeCodigo) {
        this.edeCodigo = edeCodigo;
    }

    public String getEdeNombre() {
        return edeNombre;
    }

    public void setEdeNombre(String edeNombre) {
        this.edeNombre = edeNombre;
    }

    public String getEdeNombreBusqueda() {
        return edeNombreBusqueda;
    }

    public void setEdeNombreBusqueda(String edeNombreBusqueda) {
        this.edeNombreBusqueda = edeNombreBusqueda;
    }

    public Boolean getEdeHabilitado() {
        return edeHabilitado;
    }

    public void setEdeHabilitado(Boolean edeHabilitado) {
        this.edeHabilitado = edeHabilitado;
    }

    public LocalDateTime getEdeUltModFecha() {
        return edeUltModFecha;
    }

    public void setEdeUltModFecha(LocalDateTime edeUltModFecha) {
        this.edeUltModFecha = edeUltModFecha;
    }

    public String getEdeUltModUsuario() {
        return edeUltModUsuario;
    }

    public void setEdeUltModUsuario(String edeUltModUsuario) {
        this.edeUltModUsuario = edeUltModUsuario;
    }

    public Integer getEdeVersion() {
        return edeVersion;
    }

    public void setEdeVersion(Integer edeVersion) {
        this.edeVersion = edeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.edePk);
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
        final SgAfEstadosDescargo other = (SgAfEstadosDescargo) obj;
        if (!Objects.equals(this.edePk, other.edePk)) {
            return false;
        }
        return true;
    }

    
    
}

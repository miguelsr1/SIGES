/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tunPk", scope = SgAfTiposUnidades.class)
public class SgAfTiposUnidades implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long tunPk;
    private String tunCodigo;
    private String tunNombre;
    private String tunNombreBusqueda;
    private Boolean tunHabilitado;
    private Boolean tunEsUnidadAdministrativa;
    private LocalDateTime tunUltModFecha;
    private String tunUltModUsuario;
    private Integer tunVersion;

    public SgAfTiposUnidades() {
        tunHabilitado = Boolean.TRUE;
    }

    public Long getTunPk() {
        return tunPk;
    }

    public void setTunPk(Long tunPk) {
        this.tunPk = tunPk;
    }

    public String getTunCodigo() {
        return tunCodigo;
    }

    public void setTunCodigo(String tunCodigo) {
        this.tunCodigo = tunCodigo;
    }

    public String getTunNombre() {
        return tunNombre;
    }

    public void setTunNombre(String tunNombre) {
        this.tunNombre = tunNombre;
    }

    public String getTunNombreBusqueda() {
        return tunNombreBusqueda;
    }

    public void setTunNombreBusqueda(String tunNombreBusqueda) {
        this.tunNombreBusqueda = tunNombreBusqueda;
    }

    public Boolean getTunHabilitado() {
        return tunHabilitado;
    }

    public void setTunHabilitado(Boolean tunHabilitado) {
        this.tunHabilitado = tunHabilitado;
    }

    public Boolean getTunEsUnidadAdministrativa() {
        return tunEsUnidadAdministrativa;
    }

    public void setTunEsUnidadAdministrativa(Boolean tunEsUnidadAdministrativa) {
        this.tunEsUnidadAdministrativa = tunEsUnidadAdministrativa;
    }

    public LocalDateTime getTunUltModFecha() {
        return tunUltModFecha;
    }

    public void setTunUltModFecha(LocalDateTime tunUltModFecha) {
        this.tunUltModFecha = tunUltModFecha;
    }

    public String getTunUltModUsuario() {
        return tunUltModUsuario;
    }

    public void setTunUltModUsuario(String tunUltModUsuario) {
        this.tunUltModUsuario = tunUltModUsuario;
    }

    public Integer getTunVersion() {
        return tunVersion;
    }

    public void setTunVersion(Integer tunVersion) {
        this.tunVersion = tunVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.tunPk);
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
        final SgAfTiposUnidades other = (SgAfTiposUnidades) obj;
        if (!Objects.equals(this.tunPk, other.tunPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgAfTiposUnidades[ tbiPk =" + tunPk + " ]";
    }

}

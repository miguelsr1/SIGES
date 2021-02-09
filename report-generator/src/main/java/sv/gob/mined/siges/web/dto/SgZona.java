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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "zonPk", scope = SgZona.class)
public class SgZona implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long zonPk;

    private String zonCodigo;

    private String zonNombre;

    private Boolean zonHabilitado;

    private LocalDateTime zonUltModFecha;

    private String zonUltModUsuario;

    private Integer zonVersion;

    public SgZona() {
        this.zonHabilitado = Boolean.TRUE;
    }

    public Long getZonPk() {
        return zonPk;
    }

    public void setZonPk(Long zonPk) {
        this.zonPk = zonPk;
    }

    public String getZonCodigo() {
        return zonCodigo;
    }

    public void setZonCodigo(String zonCodigo) {
        this.zonCodigo = zonCodigo;
    }

    public String getZonNombre() {
        return zonNombre;
    }

    public void setZonNombre(String zonNombre) {
        this.zonNombre = zonNombre;
    }

    public LocalDateTime getZonUltModFecha() {
        return zonUltModFecha;
    }

    public void setZonUltModFecha(LocalDateTime zonUltModFecha) {
        this.zonUltModFecha = zonUltModFecha;
    }

    public String getZonUltModUsuario() {
        return zonUltModUsuario;
    }

    public void setZonUltModUsuario(String zonUltModUsuario) {
        this.zonUltModUsuario = zonUltModUsuario;
    }

    public Integer getZonVersion() {
        return zonVersion;
    }

    public void setZonVersion(Integer zonVersion) {
        this.zonVersion = zonVersion;
    }

    public Boolean getZonHabilitado() {
        return zonHabilitado;
    }

    public void setZonHabilitado(Boolean zonHabilitado) {
        this.zonHabilitado = zonHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zonPk != null ? zonPk.hashCode() : 0);
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
        final SgZona other = (SgZona) obj;
        if (!Objects.equals(this.zonPk, other.zonPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgZona[ zonPk=" + zonPk + " ]";
    }

}

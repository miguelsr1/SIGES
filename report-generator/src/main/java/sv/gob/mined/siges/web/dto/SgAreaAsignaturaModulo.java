/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aamPk", scope = SgAreaAsignaturaModulo.class)
public class SgAreaAsignaturaModulo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long aamPk;

    private String aamCodigo;

    private String aamNombre;

    private Boolean aamHabilitado;

    private LocalDateTime aamUltModFecha;

    private String aamUltModUsuario;

    private Integer aamVersion;

    public SgAreaAsignaturaModulo() {
        this.aamHabilitado = Boolean.TRUE;
    }

    public Long getAamPk() {
        return aamPk;
    }

    public void setAamPk(Long aamPk) {
        this.aamPk = aamPk;
    }

    public String getAamCodigo() {
        return aamCodigo;
    }

    public void setAamCodigo(String aamCodigo) {
        this.aamCodigo = aamCodigo;
    }

    public String getAamNombre() {
        return aamNombre;
    }

    public void setAamNombre(String aamNombre) {
        this.aamNombre = aamNombre;
    }

    public LocalDateTime getAamUltModFecha() {
        return aamUltModFecha;
    }

    public void setAamUltModFecha(LocalDateTime aamUltModFecha) {
        this.aamUltModFecha = aamUltModFecha;
    }

    public String getAamUltModUsuario() {
        return aamUltModUsuario;
    }

    public void setAamUltModUsuario(String aamUltModUsuario) {
        this.aamUltModUsuario = aamUltModUsuario;
    }

    public Integer getAamVersion() {
        return aamVersion;
    }

    public void setAamVersion(Integer aamVersion) {
        this.aamVersion = aamVersion;
    }

    public Boolean getAamHabilitado() {
        return aamHabilitado;
    }

    public void setAamHabilitado(Boolean aamHabilitado) {
        this.aamHabilitado = aamHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aamPk != null ? aamPk.hashCode() : 0);
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
        final SgAreaAsignaturaModulo other = (SgAreaAsignaturaModulo) obj;
        if (!Objects.equals(this.aamPk, other.aamPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgAreaAsignaturaModulo[ aamPk=" + aamPk + " ]";
    }

}

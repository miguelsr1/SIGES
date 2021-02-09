/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eciPk", scope = SgEstadoCivil.class)
public class SgEstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long eciPk;

    private String eciCodigo;

    private String eciNombre;

    private Boolean eciHabilitado;

    private LocalDateTime eciUltModFecha;

    private String eciUltModUsuario;

    private Integer eciVersion;

    public SgEstadoCivil() {
        this.eciHabilitado = Boolean.TRUE;
    }

    public Long getEciPk() {
        return eciPk;
    }

    public void setEciPk(Long eciPk) {
        this.eciPk = eciPk;
    }

    public String getEciCodigo() {
        return eciCodigo;
    }

    public void setEciCodigo(String eciCodigo) {
        this.eciCodigo = eciCodigo;
    }

    public String getEciNombre() {
        return eciNombre;
    }

    public void setEciNombre(String eciNombre) {
        this.eciNombre = eciNombre;
    }

    public LocalDateTime getEciUltModFecha() {
        return eciUltModFecha;
    }

    public void setEciUltModFecha(LocalDateTime eciUltModFecha) {
        this.eciUltModFecha = eciUltModFecha;
    }

    public String getEciUltModUsuario() {
        return eciUltModUsuario;
    }

    public void setEciUltModUsuario(String eciUltModUsuario) {
        this.eciUltModUsuario = eciUltModUsuario;
    }

    public Integer getEciVersion() {
        return eciVersion;
    }

    public void setEciVersion(Integer eciVersion) {
        this.eciVersion = eciVersion;
    }

    public Boolean getEciHabilitado() {
        return eciHabilitado;
    }

    public void setEciHabilitado(Boolean eciHabilitado) {
        this.eciHabilitado = eciHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eciPk != null ? eciPk.hashCode() : 0);
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
        final SgEstadoCivil other = (SgEstadoCivil) obj;
        if (!Objects.equals(this.eciPk, other.eciPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstadoCivil[ eciPk=" + eciPk + " ]";
    }

}

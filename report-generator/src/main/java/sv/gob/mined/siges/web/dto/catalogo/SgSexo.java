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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sexPk", scope = SgSexo.class)
public class SgSexo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sexPk;

    private String sexCodigo;

    private String sexNombre;

    private Boolean sexHabilitado;

    private LocalDateTime sexUltModFecha;

    private String sexUltModUsuario;

    private Integer sexVersion;

    public SgSexo() {
        this.sexHabilitado = Boolean.TRUE;
    }

    public Long getSexPk() {
        return sexPk;
    }

    public void setSexPk(Long sexPk) {
        this.sexPk = sexPk;
    }

    public String getSexCodigo() {
        return sexCodigo;
    }

    public void setSexCodigo(String sexCodigo) {
        this.sexCodigo = sexCodigo;
    }

    public String getSexNombre() {
        return sexNombre;
    }

    public void setSexNombre(String sexNombre) {
        this.sexNombre = sexNombre;
    }

    public LocalDateTime getSexUltModFecha() {
        return sexUltModFecha;
    }

    public void setSexUltModFecha(LocalDateTime sexUltModFecha) {
        this.sexUltModFecha = sexUltModFecha;
    }

    public String getSexUltModUsuario() {
        return sexUltModUsuario;
    }

    public void setSexUltModUsuario(String sexUltModUsuario) {
        this.sexUltModUsuario = sexUltModUsuario;
    }

    public Integer getSexVersion() {
        return sexVersion;
    }

    public void setSexVersion(Integer sexVersion) {
        this.sexVersion = sexVersion;
    }

    public Boolean getSexHabilitado() {
        return sexHabilitado;
    }

    public void setSexHabilitado(Boolean sexHabilitado) {
        this.sexHabilitado = sexHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sexPk != null ? sexPk.hashCode() : 0);
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
        final SgSexo other = (SgSexo) obj;
        if (!Objects.equals(this.sexPk, other.sexPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgSexo[ sexPk=" + sexPk + " ]";
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "decPk", scope = SgDependenciaEconomica.class)
public class SgDependenciaEconomica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long decPk;

    private String decCodigo;

    private String decNombre;

    private Boolean decHabilitado;

    private LocalDateTime decUltModFecha;

    private String decUltModUsuario;

    private Integer decVersion;

    public SgDependenciaEconomica() {
        this.decHabilitado = Boolean.TRUE;
    }

    public Long getDecPk() {
        return decPk;
    }

    public void setDecPk(Long decPk) {
        this.decPk = decPk;
    }

    public String getDecCodigo() {
        return decCodigo;
    }

    public void setDecCodigo(String decCodigo) {
        this.decCodigo = decCodigo;
    }

    public String getDecNombre() {
        return decNombre;
    }

    public void setDecNombre(String decNombre) {
        this.decNombre = decNombre;
    }

    public LocalDateTime getDecUltModFecha() {
        return decUltModFecha;
    }

    public void setDecUltModFecha(LocalDateTime decUltModFecha) {
        this.decUltModFecha = decUltModFecha;
    }

    public String getDecUltModUsuario() {
        return decUltModUsuario;
    }

    public void setDecUltModUsuario(String decUltModUsuario) {
        this.decUltModUsuario = decUltModUsuario;
    }

    public Integer getDecVersion() {
        return decVersion;
    }

    public void setDecVersion(Integer decVersion) {
        this.decVersion = decVersion;
    }

    public Boolean getDecHabilitado() {
        return decHabilitado;
    }

    public void setDecHabilitado(Boolean decHabilitado) {
        this.decHabilitado = decHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (decPk != null ? decPk.hashCode() : 0);
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
        final SgDependenciaEconomica other = (SgDependenciaEconomica) obj;
        if (!Objects.equals(this.decPk, other.decPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgDependenciaEconomica[ decPk=" + decPk + " ]";
    }

}

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "escPk", scope = SgEscolaridad.class)
public class SgEscolaridad implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long escPk;

    private String escCodigo;

    private String escNombre;

    private Boolean escHabilitado;

    private LocalDateTime escUltModFecha;

    private String escUltModUsuario;

    private Integer escVersion;

    public SgEscolaridad() {
        this.escHabilitado = Boolean.TRUE;
    }

    public Long getEscPk() {
        return escPk;
    }

    public void setEscPk(Long escPk) {
        this.escPk = escPk;
    }

    public String getEscCodigo() {
        return escCodigo;
    }

    public void setEscCodigo(String escCodigo) {
        this.escCodigo = escCodigo;
    }

    public String getEscNombre() {
        return escNombre;
    }

    public void setEscNombre(String escNombre) {
        this.escNombre = escNombre;
    }

    public LocalDateTime getEscUltModFecha() {
        return escUltModFecha;
    }

    public void setEscUltModFecha(LocalDateTime escUltModFecha) {
        this.escUltModFecha = escUltModFecha;
    }

    public String getEscUltModUsuario() {
        return escUltModUsuario;
    }

    public void setEscUltModUsuario(String escUltModUsuario) {
        this.escUltModUsuario = escUltModUsuario;
    }

    public Integer getEscVersion() {
        return escVersion;
    }

    public void setEscVersion(Integer escVersion) {
        this.escVersion = escVersion;
    }

    public Boolean getEscHabilitado() {
        return escHabilitado;
    }

    public void setEscHabilitado(Boolean escHabilitado) {
        this.escHabilitado = escHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escPk != null ? escPk.hashCode() : 0);
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
        final SgEscolaridad other = (SgEscolaridad) obj;
        if (!Objects.equals(this.escPk, other.escPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgEscolaridad[ escPk=" + escPk + " ]";
    }

}

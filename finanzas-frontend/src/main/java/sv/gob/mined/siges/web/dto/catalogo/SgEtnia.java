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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etnPk", scope = SgEtnia.class)
public class SgEtnia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long etnPk;

    private String etnCodigo;

    private String etnNombre;

    private Boolean etnHabilitado;

    private LocalDateTime etnUltModFecha;

    private String etnUltModUsuario;

    private Integer etnVersion;

    public SgEtnia() {
        this.etnHabilitado = Boolean.TRUE;
    }

    public Long getEtnPk() {
        return etnPk;
    }

    public void setEtnPk(Long etnPk) {
        this.etnPk = etnPk;
    }

    public String getEtnCodigo() {
        return etnCodigo;
    }

    public void setEtnCodigo(String etnCodigo) {
        this.etnCodigo = etnCodigo;
    }

    public String getEtnNombre() {
        return etnNombre;
    }

    public void setEtnNombre(String etnNombre) {
        this.etnNombre = etnNombre;
    }

    public LocalDateTime getEtnUltModFecha() {
        return etnUltModFecha;
    }

    public void setEtnUltModFecha(LocalDateTime etnUltModFecha) {
        this.etnUltModFecha = etnUltModFecha;
    }

    public String getEtnUltModUsuario() {
        return etnUltModUsuario;
    }

    public void setEtnUltModUsuario(String etnUltModUsuario) {
        this.etnUltModUsuario = etnUltModUsuario;
    }

    public Integer getEtnVersion() {
        return etnVersion;
    }

    public void setEtnVersion(Integer etnVersion) {
        this.etnVersion = etnVersion;
    }

    public Boolean getEtnHabilitado() {
        return etnHabilitado;
    }

    public void setEtnHabilitado(Boolean etnHabilitado) {
        this.etnHabilitado = etnHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etnPk != null ? etnPk.hashCode() : 0);
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
        final SgEtnia other = (SgEtnia) obj;
        if (!Objects.equals(this.etnPk, other.etnPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgEtnia[ etnPk=" + etnPk + " ]";
    }

}

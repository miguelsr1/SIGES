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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "disPk", scope = SgDiscapacidad.class)
public class SgDiscapacidad implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long disPk;

    private String disCodigo;

    private String disNombre;

    private Boolean disHabilitado;

    private LocalDateTime disUltModFecha;

    private String disUltModUsuario;

    private Integer disVersion;

    public SgDiscapacidad() {
        this.disHabilitado = Boolean.TRUE;
    }

    public Long getDisPk() {
        return disPk;
    }

    public void setDisPk(Long disPk) {
        this.disPk = disPk;
    }

    public String getDisCodigo() {
        return disCodigo;
    }

    public void setDisCodigo(String disCodigo) {
        this.disCodigo = disCodigo;
    }

    public String getDisNombre() {
        return disNombre;
    }

    public void setDisNombre(String disNombre) {
        this.disNombre = disNombre;
    }

    public LocalDateTime getDisUltModFecha() {
        return disUltModFecha;
    }

    public void setDisUltModFecha(LocalDateTime disUltModFecha) {
        this.disUltModFecha = disUltModFecha;
    }

    public String getDisUltModUsuario() {
        return disUltModUsuario;
    }

    public void setDisUltModUsuario(String disUltModUsuario) {
        this.disUltModUsuario = disUltModUsuario;
    }

    public Integer getDisVersion() {
        return disVersion;
    }

    public void setDisVersion(Integer disVersion) {
        this.disVersion = disVersion;
    }

    public Boolean getDisHabilitado() {
        return disHabilitado;
    }

    public void setDisHabilitado(Boolean disHabilitado) {
        this.disHabilitado = disHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disPk != null ? disPk.hashCode() : 0);
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
        final SgDiscapacidad other = (SgDiscapacidad) obj;
        if (!Objects.equals(this.disPk, other.disPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgDiscapacidad[ disPk=" + disPk + " ]";
    }

}

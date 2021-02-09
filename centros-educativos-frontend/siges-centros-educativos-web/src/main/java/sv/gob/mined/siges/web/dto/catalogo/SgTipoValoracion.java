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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tvaPk", scope = SgTipoValoracion.class)
public class SgTipoValoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tvaPk;

    private String tvaCodigo;

    private String tvaNombre;

    private Boolean tvaHabilitado;

    private LocalDateTime tvaUltModFecha;

    private String tvaUltModUsuario;

    private Integer tvaVersion;

    public SgTipoValoracion() {
        this.tvaHabilitado = Boolean.TRUE;
    }

    public Long getTvaPk() {
        return tvaPk;
    }

    public void setTvaPk(Long tvaPk) {
        this.tvaPk = tvaPk;
    }

    public String getTvaCodigo() {
        return tvaCodigo;
    }

    public void setTvaCodigo(String tvaCodigo) {
        this.tvaCodigo = tvaCodigo;
    }

    public String getTvaNombre() {
        return tvaNombre;
    }

    public void setTvaNombre(String tvaNombre) {
        this.tvaNombre = tvaNombre;
    }

    public LocalDateTime getTvaUltModFecha() {
        return tvaUltModFecha;
    }

    public void setTvaUltModFecha(LocalDateTime tvaUltModFecha) {
        this.tvaUltModFecha = tvaUltModFecha;
    }

    public String getTvaUltModUsuario() {
        return tvaUltModUsuario;
    }

    public void setTvaUltModUsuario(String tvaUltModUsuario) {
        this.tvaUltModUsuario = tvaUltModUsuario;
    }

    public Integer getTvaVersion() {
        return tvaVersion;
    }

    public void setTvaVersion(Integer tvaVersion) {
        this.tvaVersion = tvaVersion;
    }

    public Boolean getTvaHabilitado() {
        return tvaHabilitado;
    }

    public void setTvaHabilitado(Boolean tvaHabilitado) {
        this.tvaHabilitado = tvaHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tvaPk != null ? tvaPk.hashCode() : 0);
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
        final SgTipoValoracion other = (SgTipoValoracion) obj;
        if (!Objects.equals(this.tvaPk, other.tvaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoValoracion[ tvaPk=" + tvaPk + " ]";
    }

}

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tsaPk", scope = SgTipoSangre.class)
public class SgTipoSangre implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tsaPk;

    private String tsaCodigo;

    private String tsaNombre;

    private Boolean tsaHabilitado;

    private LocalDateTime tsaUltModFecha;

    private String tsaUltModUsuario;

    private Integer tsaVersion;

    public SgTipoSangre() {
        this.tsaHabilitado = Boolean.TRUE;
    }

    public Long getTsaPk() {
        return tsaPk;
    }

    public void setTsaPk(Long tsaPk) {
        this.tsaPk = tsaPk;
    }

    public String getTsaCodigo() {
        return tsaCodigo;
    }

    public void setTsaCodigo(String tsaCodigo) {
        this.tsaCodigo = tsaCodigo;
    }

    public String getTsaNombre() {
        return tsaNombre;
    }

    public void setTsaNombre(String tsaNombre) {
        this.tsaNombre = tsaNombre;
    }

    public LocalDateTime getTsaUltModFecha() {
        return tsaUltModFecha;
    }

    public void setTsaUltModFecha(LocalDateTime tsaUltModFecha) {
        this.tsaUltModFecha = tsaUltModFecha;
    }

    public String getTsaUltModUsuario() {
        return tsaUltModUsuario;
    }

    public void setTsaUltModUsuario(String tsaUltModUsuario) {
        this.tsaUltModUsuario = tsaUltModUsuario;
    }

    public Integer getTsaVersion() {
        return tsaVersion;
    }

    public void setTsaVersion(Integer tsaVersion) {
        this.tsaVersion = tsaVersion;
    }

    public Boolean getTsaHabilitado() {
        return tsaHabilitado;
    }

    public void setTsaHabilitado(Boolean tsaHabilitado) {
        this.tsaHabilitado = tsaHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tsaPk != null ? tsaPk.hashCode() : 0);
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
        final SgTipoSangre other = (SgTipoSangre) obj;
        if (!Objects.equals(this.tsaPk, other.tsaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoSangre[ tsaPk=" + tsaPk + " ]";
    }

}

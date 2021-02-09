/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "niePk", scope = SgNie.class)
public class SgNie implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long niePk;

    private String nieCodigo;

    private String nieNombre;

    private Boolean nieHabilitado;

    private LocalDateTime nieUltModFecha;

    private String nieUltModUsuario;

    private Integer nieVersion;

    public SgNie() {
        this.nieHabilitado = Boolean.TRUE;
    }

    public Long getNiePk() {
        return niePk;
    }

    public void setNiePk(Long niePk) {
        this.niePk = niePk;
    }

    public String getNieCodigo() {
        return nieCodigo;
    }

    public void setNieCodigo(String nieCodigo) {
        this.nieCodigo = nieCodigo;
    }

    public String getNieNombre() {
        return nieNombre;
    }

    public void setNieNombre(String nieNombre) {
        this.nieNombre = nieNombre;
    }

    public LocalDateTime getNieUltModFecha() {
        return nieUltModFecha;
    }

    public void setNieUltModFecha(LocalDateTime nieUltModFecha) {
        this.nieUltModFecha = nieUltModFecha;
    }

    public String getNieUltModUsuario() {
        return nieUltModUsuario;
    }

    public void setNieUltModUsuario(String nieUltModUsuario) {
        this.nieUltModUsuario = nieUltModUsuario;
    }

    public Integer getNieVersion() {
        return nieVersion;
    }

    public void setNieVersion(Integer nieVersion) {
        this.nieVersion = nieVersion;
    }

    public Boolean getNieHabilitado() {
        return nieHabilitado;
    }

    public void setNieHabilitado(Boolean nieHabilitado) {
        this.nieHabilitado = nieHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (niePk != null ? niePk.hashCode() : 0);
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
        final SgNie other = (SgNie) obj;
        if (!Objects.equals(this.niePk, other.niePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgNie[ niePk=" + niePk + " ]";
    }

}

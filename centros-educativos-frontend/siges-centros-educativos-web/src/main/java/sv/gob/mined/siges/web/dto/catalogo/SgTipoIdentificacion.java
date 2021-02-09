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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tinPk", scope = SgTipoIdentificacion.class)
public class SgTipoIdentificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tinPk;

    private String tinCodigo;

    private String tinNombre;

    private Boolean tinHabilitado;

    private LocalDateTime tinUltModFecha;

    private String tinUltModUsuario;

    private Integer tinVersion;

    public SgTipoIdentificacion() {
        this.tinHabilitado = Boolean.TRUE;
    }

    public Long getTinPk() {
        return tinPk;
    }

    public void setTinPk(Long tinPk) {
        this.tinPk = tinPk;
    }

    public String getTinCodigo() {
        return tinCodigo;
    }

    public void setTinCodigo(String tinCodigo) {
        this.tinCodigo = tinCodigo;
    }

    public String getTinNombre() {
        return tinNombre;
    }

    public void setTinNombre(String tinNombre) {
        this.tinNombre = tinNombre;
    }

    public LocalDateTime getTinUltModFecha() {
        return tinUltModFecha;
    }

    public void setTinUltModFecha(LocalDateTime tinUltModFecha) {
        this.tinUltModFecha = tinUltModFecha;
    }

    public String getTinUltModUsuario() {
        return tinUltModUsuario;
    }

    public void setTinUltModUsuario(String tinUltModUsuario) {
        this.tinUltModUsuario = tinUltModUsuario;
    }

    public Integer getTinVersion() {
        return tinVersion;
    }

    public void setTinVersion(Integer tinVersion) {
        this.tinVersion = tinVersion;
    }

    public Boolean getTinHabilitado() {
        return tinHabilitado;
    }

    public void setTinHabilitado(Boolean tinHabilitado) {
        this.tinHabilitado = tinHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tinPk != null ? tinPk.hashCode() : 0);
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
        final SgTipoIdentificacion other = (SgTipoIdentificacion) obj;
        if (!Objects.equals(this.tinPk, other.tinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoIdentificacion[ tinPk=" + tinPk + " ]";
    }

}

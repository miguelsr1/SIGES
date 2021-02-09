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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tssPk", scope = SgTipoServicioSanitario.class)
public class SgTipoServicioSanitario implements Serializable {

    private Long tssPk;

    private String tssCodigo;

    private Boolean tssHabilitado;

    private String tssNombre;

    private String tssNombreBusqueda;

    private LocalDateTime tssUltModFecha;

    private String tssUltModUsuario;

    private Integer tssVersion;

    public SgTipoServicioSanitario() {
        this.tssHabilitado = Boolean.TRUE;
    }

    public Long getTssPk() {
        return tssPk;
    }

    public void setTssPk(Long tssPk) {
        this.tssPk = tssPk;
    }

    public String getTssCodigo() {
        return tssCodigo;
    }

    public void setTssCodigo(String tssCodigo) {
        this.tssCodigo = tssCodigo;
    }

    public Boolean getTssHabilitado() {
        return tssHabilitado;
    }

    public void setTssHabilitado(Boolean tssHabilitado) {
        this.tssHabilitado = tssHabilitado;
    }

    public String getTssNombre() {
        return tssNombre;
    }

    public void setTssNombre(String tssNombre) {
        this.tssNombre = tssNombre;
    }

    public String getTssNombreBusqueda() {
        return tssNombreBusqueda;
    }

    public void setTssNombreBusqueda(String tssNombreBusqueda) {
        this.tssNombreBusqueda = tssNombreBusqueda;
    }

    public LocalDateTime getTssUltModFecha() {
        return tssUltModFecha;
    }

    public void setTssUltModFecha(LocalDateTime tssUltModFecha) {
        this.tssUltModFecha = tssUltModFecha;
    }

    public String getTssUltModUsuario() {
        return tssUltModUsuario;
    }

    public void setTssUltModUsuario(String tssUltModUsuario) {
        this.tssUltModUsuario = tssUltModUsuario;
    }

    public Integer getTssVersion() {
        return tssVersion;
    }

    public void setTssVersion(Integer tssVersion) {
        this.tssVersion = tssVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tssPk != null ? tssPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoServicioSanitario)) {
            return false;
        }
        SgTipoServicioSanitario other = (SgTipoServicioSanitario) object;
        if ((this.tssPk == null && other.tssPk != null) || (this.tssPk != null && !this.tssPk.equals(other.tssPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoServicioSanitario[ tssPk=" + tssPk + " ]";
    }

}

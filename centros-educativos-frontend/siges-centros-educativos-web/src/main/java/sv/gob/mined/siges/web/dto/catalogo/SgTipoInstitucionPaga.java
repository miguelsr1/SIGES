/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tipPk", scope = SgTipoInstitucionPaga.class)
public class SgTipoInstitucionPaga implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tipPk;

    private String tipCodigo;

    private Boolean tipHabilitado;

    private String tipNombre;

    private String tipNombreBusqueda;

    private LocalDateTime tipUltModFecha;

    private String tipUltModUsuario;

    private Integer tipVersion;

    public SgTipoInstitucionPaga() {
    }

    public SgTipoInstitucionPaga(Long tipPk) {
        this.tipPk = tipPk;
    }

    public Long getTipPk() {
        return tipPk;
    }

    public void setTipPk(Long tipPk) {
        this.tipPk = tipPk;
    }

    public String getTipCodigo() {
        return tipCodigo;
    }

    public void setTipCodigo(String tipCodigo) {
        this.tipCodigo = tipCodigo;
    }

    public Boolean getTipHabilitado() {
        return tipHabilitado;
    }

    public void setTipHabilitado(Boolean tipHabilitado) {
        this.tipHabilitado = tipHabilitado;
    }

    public String getTipNombre() {
        return tipNombre;
    }

    public void setTipNombre(String tipNombre) {
        this.tipNombre = tipNombre;
    }

    public String getTipNombreBusqueda() {
        return tipNombreBusqueda;
    }

    public void setTipNombreBusqueda(String tipNombreBusqueda) {
        this.tipNombreBusqueda = tipNombreBusqueda;
    }

    public LocalDateTime getTipUltModFecha() {
        return tipUltModFecha;
    }

    public void setTipUltModFecha(LocalDateTime tipUltModFecha) {
        this.tipUltModFecha = tipUltModFecha;
    }

    public String getTipUltModUsuario() {
        return tipUltModUsuario;
    }

    public void setTipUltModUsuario(String tipUltModUsuario) {
        this.tipUltModUsuario = tipUltModUsuario;
    }

    public Integer getTipVersion() {
        return tipVersion;
    }

    public void setTipVersion(Integer tipVersion) {
        this.tipVersion = tipVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipPk != null ? tipPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoInstitucionPaga)) {
            return false;
        }
        SgTipoInstitucionPaga other = (SgTipoInstitucionPaga) object;
        if ((this.tipPk == null && other.tipPk != null) || (this.tipPk != null && !this.tipPk.equals(other.tipPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoInstitucionPaga[ tipPk=" + tipPk + " ]";
    }

}

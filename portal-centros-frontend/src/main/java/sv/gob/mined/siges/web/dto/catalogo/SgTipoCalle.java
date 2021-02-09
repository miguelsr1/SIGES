/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tllPk", scope = SgTipoCalle.class)
public class SgTipoCalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tllPk;

    private String tllCodigo;

    private Boolean tllHabilitado;

    private String tllNombre;

    private LocalDateTime tllUltModFecha;

    private String tllUltModUsuario;

    private Integer tllVersion;

    public SgTipoCalle() {
    }

    public SgTipoCalle(Long tllPk) {
        this.tllPk = tllPk;
    }

    public Long getTllPk() {
        return tllPk;
    }

    public void setTllPk(Long tllPk) {
        this.tllPk = tllPk;
    }

    public String getTllCodigo() {
        return tllCodigo;
    }

    public void setTllCodigo(String tllCodigo) {
        this.tllCodigo = tllCodigo;
    }

    public Boolean getTllHabilitado() {
        return tllHabilitado;
    }

    public void setTllHabilitado(Boolean tllHabilitado) {
        this.tllHabilitado = tllHabilitado;
    }

    public String getTllNombre() {
        return tllNombre;
    }

    public void setTllNombre(String tllNombre) {
        this.tllNombre = tllNombre;
    }

    public LocalDateTime getTllUltModFecha() {
        return tllUltModFecha;
    }

    public void setTllUltModFecha(LocalDateTime tllUltModFecha) {
        this.tllUltModFecha = tllUltModFecha;
    }

    public String getTllUltModUsuario() {
        return tllUltModUsuario;
    }

    public void setTllUltModUsuario(String tllUltModUsuario) {
        this.tllUltModUsuario = tllUltModUsuario;
    }

    public Integer getTllVersion() {
        return tllVersion;
    }

    public void setTllVersion(Integer tllVersion) {
        this.tllVersion = tllVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tllPk != null ? tllPk.hashCode() : 0);
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
        final SgTipoCalle other = (SgTipoCalle) obj;
        if (!Objects.equals(this.tllPk, other.tllPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoCalle[ tllPk=" + tllPk + " ]";
    }

}

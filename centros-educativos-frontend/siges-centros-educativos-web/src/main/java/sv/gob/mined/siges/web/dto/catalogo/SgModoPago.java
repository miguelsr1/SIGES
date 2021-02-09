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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mpaPk", scope = SgModoPago.class)
public class SgModoPago implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mpaPk;

    private String mpaCodigo;

    private Boolean mpaHabilitado;

    private String mpaNombre;

    private String mpaNombreBusqueda;

    private LocalDateTime mpaUltModFecha;

    private String mpaUltModUsuario;

    private Integer mpaVersion;

    public SgModoPago() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mpaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mpaNombre);
    }

    public SgModoPago(Long mpaPk) {
        this.mpaPk = mpaPk;
    }

    public Long getMpaPk() {
        return mpaPk;
    }

    public void setMpaPk(Long mpaPk) {
        this.mpaPk = mpaPk;
    }

    public String getMpaCodigo() {
        return mpaCodigo;
    }

    public void setMpaCodigo(String mpaCodigo) {
        this.mpaCodigo = mpaCodigo;
    }

    public Boolean getMpaHabilitado() {
        return mpaHabilitado;
    }

    public void setMpaHabilitado(Boolean mpaHabilitado) {
        this.mpaHabilitado = mpaHabilitado;
    }

    public String getMpaNombre() {
        return mpaNombre;
    }

    public void setMpaNombre(String mpaNombre) {
        this.mpaNombre = mpaNombre;
    }

    public String getMpaNombreBusqueda() {
        return mpaNombreBusqueda;
    }

    public void setMpaNombreBusqueda(String mpaNombreBusqueda) {
        this.mpaNombreBusqueda = mpaNombreBusqueda;
    }

    public LocalDateTime getMpaUltModFecha() {
        return mpaUltModFecha;
    }

    public void setMpaUltModFecha(LocalDateTime mpaUltModFecha) {
        this.mpaUltModFecha = mpaUltModFecha;
    }

    public String getMpaUltModUsuario() {
        return mpaUltModUsuario;
    }

    public void setMpaUltModUsuario(String mpaUltModUsuario) {
        this.mpaUltModUsuario = mpaUltModUsuario;
    }

    public Integer getMpaVersion() {
        return mpaVersion;
    }

    public void setMpaVersion(Integer mpaVersion) {
        this.mpaVersion = mpaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpaPk != null ? mpaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModoPago)) {
            return false;
        }
        SgModoPago other = (SgModoPago) object;
        if ((this.mpaPk == null && other.mpaPk != null) || (this.mpaPk != null && !this.mpaPk.equals(other.mpaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModoPago[ mpaPk=" + mpaPk + " ]";
    }

}

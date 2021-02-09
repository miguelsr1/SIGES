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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "lsaPk", scope = SgLeySalario.class)
public class SgLeySalario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long lsaPk;

    private String lsaCodigo;

    private Boolean lsaHabilitado;

    private String lsaNombre;

    private String lsaNombreBusqueda;

    private LocalDateTime lsaUltModFecha;

    private String lsaUltModUsuario;

    private Integer lsaVersion;

    public SgLeySalario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.lsaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.lsaNombre);
    }

    public SgLeySalario(Long lsaPk) {
        this.lsaPk = lsaPk;
    }

    public Long getLsaPk() {
        return lsaPk;
    }

    public void setLsaPk(Long lsaPk) {
        this.lsaPk = lsaPk;
    }

    public String getLsaCodigo() {
        return lsaCodigo;
    }

    public void setLsaCodigo(String lsaCodigo) {
        this.lsaCodigo = lsaCodigo;
    }

    public Boolean getLsaHabilitado() {
        return lsaHabilitado;
    }

    public void setLsaHabilitado(Boolean lsaHabilitado) {
        this.lsaHabilitado = lsaHabilitado;
    }

    public String getLsaNombre() {
        return lsaNombre;
    }

    public void setLsaNombre(String lsaNombre) {
        this.lsaNombre = lsaNombre;
    }

    public String getLsaNombreBusqueda() {
        return lsaNombreBusqueda;
    }

    public void setLsaNombreBusqueda(String lsaNombreBusqueda) {
        this.lsaNombreBusqueda = lsaNombreBusqueda;
    }

    public LocalDateTime getLsaUltModFecha() {
        return lsaUltModFecha;
    }

    public void setLsaUltModFecha(LocalDateTime lsaUltModFecha) {
        this.lsaUltModFecha = lsaUltModFecha;
    }

    public String getLsaUltModUsuario() {
        return lsaUltModUsuario;
    }

    public void setLsaUltModUsuario(String lsaUltModUsuario) {
        this.lsaUltModUsuario = lsaUltModUsuario;
    }

    public Integer getLsaVersion() {
        return lsaVersion;
    }

    public void setLsaVersion(Integer lsaVersion) {
        this.lsaVersion = lsaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lsaPk != null ? lsaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgLeySalario)) {
            return false;
        }
        SgLeySalario other = (SgLeySalario) object;
        if ((this.lsaPk == null && other.lsaPk != null) || (this.lsaPk != null && !this.lsaPk.equals(other.lsaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgLeySalario[ lsaPk=" + lsaPk + " ]";
    }

}

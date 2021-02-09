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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ffiPk", scope = SgFuenteFinanciamiento.class)
public class SgFuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ffiPk;

    private String ffiCodigo;

    private Boolean ffiHabilitado;

    private String ffiNombre;

    private String ffiNombreBusqueda;

    private LocalDateTime ffiUltModFecha;

    private String ffiUltModUsuario;

    private Integer ffiVersion;

    public SgFuenteFinanciamiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ffiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ffiNombre);
    }

    public SgFuenteFinanciamiento(Long ffiPk) {
        this.ffiPk = ffiPk;
    }

    public Long getFfiPk() {
        return ffiPk;
    }

    public void setFfiPk(Long ffiPk) {
        this.ffiPk = ffiPk;
    }

    public String getFfiCodigo() {
        return ffiCodigo;
    }

    public void setFfiCodigo(String ffiCodigo) {
        this.ffiCodigo = ffiCodigo;
    }

    public Boolean getFfiHabilitado() {
        return ffiHabilitado;
    }

    public void setFfiHabilitado(Boolean ffiHabilitado) {
        this.ffiHabilitado = ffiHabilitado;
    }

    public String getFfiNombre() {
        return ffiNombre;
    }

    public void setFfiNombre(String ffiNombre) {
        this.ffiNombre = ffiNombre;
    }

    public String getFfiNombreBusqueda() {
        return ffiNombreBusqueda;
    }

    public void setFfiNombreBusqueda(String ffiNombreBusqueda) {
        this.ffiNombreBusqueda = ffiNombreBusqueda;
    }

    public LocalDateTime getFfiUltModFecha() {
        return ffiUltModFecha;
    }

    public void setFfiUltModFecha(LocalDateTime ffiUltModFecha) {
        this.ffiUltModFecha = ffiUltModFecha;
    }

    public String getFfiUltModUsuario() {
        return ffiUltModUsuario;
    }

    public void setFfiUltModUsuario(String ffiUltModUsuario) {
        this.ffiUltModUsuario = ffiUltModUsuario;
    }

    public Integer getFfiVersion() {
        return ffiVersion;
    }

    public void setFfiVersion(Integer ffiVersion) {
        this.ffiVersion = ffiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ffiPk != null ? ffiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgFuenteFinanciamiento)) {
            return false;
        }
        SgFuenteFinanciamiento other = (SgFuenteFinanciamiento) object;
        if ((this.ffiPk == null && other.ffiPk != null) || (this.ffiPk != null && !this.ffiPk.equals(other.ffiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFuenteFinanciamiento[ ffiPk=" + ffiPk + " ]";
    }

}

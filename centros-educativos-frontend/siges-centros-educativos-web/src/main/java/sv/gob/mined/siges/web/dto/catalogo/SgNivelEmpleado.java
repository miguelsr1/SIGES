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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nemPk", scope = SgNivelEmpleado.class)
public class SgNivelEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nemPk;

    private String nemCodigo;

    private Boolean nemHabilitado;

    private String nemNombre;

    private String nemNombreBusqueda;

    private LocalDateTime nemUltModFecha;

    private String nemUltModUsuario;

    private Integer nemVersion;

    public SgNivelEmpleado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nemNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nemNombre);
    }

    public SgNivelEmpleado(Long nemPk) {
        this.nemPk = nemPk;
    }

    public Long getNemPk() {
        return nemPk;
    }

    public void setNemPk(Long nemPk) {
        this.nemPk = nemPk;
    }

    public String getNemCodigo() {
        return nemCodigo;
    }

    public void setNemCodigo(String nemCodigo) {
        this.nemCodigo = nemCodigo;
    }

    public Boolean getNemHabilitado() {
        return nemHabilitado;
    }

    public void setNemHabilitado(Boolean nemHabilitado) {
        this.nemHabilitado = nemHabilitado;
    }

    public String getNemNombre() {
        return nemNombre;
    }

    public void setNemNombre(String nemNombre) {
        this.nemNombre = nemNombre;
    }

    public String getNemNombreBusqueda() {
        return nemNombreBusqueda;
    }

    public void setNemNombreBusqueda(String nemNombreBusqueda) {
        this.nemNombreBusqueda = nemNombreBusqueda;
    }

    public LocalDateTime getNemUltModFecha() {
        return nemUltModFecha;
    }

    public void setNemUltModFecha(LocalDateTime nemUltModFecha) {
        this.nemUltModFecha = nemUltModFecha;
    }

    public String getNemUltModUsuario() {
        return nemUltModUsuario;
    }

    public void setNemUltModUsuario(String nemUltModUsuario) {
        this.nemUltModUsuario = nemUltModUsuario;
    }

    public Integer getNemVersion() {
        return nemVersion;
    }

    public void setNemVersion(Integer nemVersion) {
        this.nemVersion = nemVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nemPk != null ? nemPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelEmpleado)) {
            return false;
        }
        SgNivelEmpleado other = (SgNivelEmpleado) object;
        if ((this.nemPk == null && other.nemPk != null) || (this.nemPk != null && !this.nemPk.equals(other.nemPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelEmpleado[ nemPk=" + nemPk + " ]";
    }

}

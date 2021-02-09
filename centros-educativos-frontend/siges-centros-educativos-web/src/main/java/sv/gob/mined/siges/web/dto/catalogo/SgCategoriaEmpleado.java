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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cemPk", scope = SgCategoriaEmpleado.class)
public class SgCategoriaEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cemPk;

    private String cemCodigo;

    private Boolean cemHabilitado;

    private String cemNombre;

    private String cemNombreBusqueda;

    private LocalDateTime cemUltModFecha;

    private String cemUltModUsuario;

    private Integer cemVersion;

    public SgCategoriaEmpleado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cemNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cemNombre);
    }

    public SgCategoriaEmpleado(Long cemPk) {
        this.cemPk = cemPk;
    }

    public Long getCemPk() {
        return cemPk;
    }

    public void setCemPk(Long cemPk) {
        this.cemPk = cemPk;
    }

    public String getCemCodigo() {
        return cemCodigo;
    }

    public void setCemCodigo(String cemCodigo) {
        this.cemCodigo = cemCodigo;
    }

    public Boolean getCemHabilitado() {
        return cemHabilitado;
    }

    public void setCemHabilitado(Boolean cemHabilitado) {
        this.cemHabilitado = cemHabilitado;
    }

    public String getCemNombre() {
        return cemNombre;
    }

    public void setCemNombre(String cemNombre) {
        this.cemNombre = cemNombre;
    }

    public String getCemNombreBusqueda() {
        return cemNombreBusqueda;
    }

    public void setCemNombreBusqueda(String cemNombreBusqueda) {
        this.cemNombreBusqueda = cemNombreBusqueda;
    }

    public LocalDateTime getCemUltModFecha() {
        return cemUltModFecha;
    }

    public void setCemUltModFecha(LocalDateTime cemUltModFecha) {
        this.cemUltModFecha = cemUltModFecha;
    }

    public String getCemUltModUsuario() {
        return cemUltModUsuario;
    }

    public void setCemUltModUsuario(String cemUltModUsuario) {
        this.cemUltModUsuario = cemUltModUsuario;
    }

    public Integer getCemVersion() {
        return cemVersion;
    }

    public void setCemVersion(Integer cemVersion) {
        this.cemVersion = cemVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cemPk != null ? cemPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaEmpleado)) {
            return false;
        }
        SgCategoriaEmpleado other = (SgCategoriaEmpleado) object;
        if ((this.cemPk == null && other.cemPk != null) || (this.cemPk != null && !this.cemPk.equals(other.cemPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaEmpleado[ cemPk=" + cemPk + " ]";
    }

}

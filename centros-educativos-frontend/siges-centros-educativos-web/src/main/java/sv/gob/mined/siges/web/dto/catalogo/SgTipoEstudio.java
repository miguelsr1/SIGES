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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tesPk", scope = SgTipoEstudio.class)
public class SgTipoEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tesPk;

    private String tesCodigo;

    private Boolean tesHabilitado;

    private String tesNombre;

    private String tesNombreBusqueda;

    private LocalDateTime tesUltModFecha;

    private String tesUltModUsuario;

    private Integer tesVersion;

    public SgTipoEstudio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tesNombre);
    }

    public SgTipoEstudio(Long tesPk) {
        this.tesPk = tesPk;
    }

    public Long getTesPk() {
        return tesPk;
    }

    public void setTesPk(Long tesPk) {
        this.tesPk = tesPk;
    }

    public String getTesCodigo() {
        return tesCodigo;
    }

    public void setTesCodigo(String tesCodigo) {
        this.tesCodigo = tesCodigo;
    }

    public Boolean getTesHabilitado() {
        return tesHabilitado;
    }

    public void setTesHabilitado(Boolean tesHabilitado) {
        this.tesHabilitado = tesHabilitado;
    }

    public String getTesNombre() {
        return tesNombre;
    }

    public void setTesNombre(String tesNombre) {
        this.tesNombre = tesNombre;
    }

    public String getTesNombreBusqueda() {
        return tesNombreBusqueda;
    }

    public void setTesNombreBusqueda(String tesNombreBusqueda) {
        this.tesNombreBusqueda = tesNombreBusqueda;
    }

    public LocalDateTime getTesUltModFecha() {
        return tesUltModFecha;
    }

    public void setTesUltModFecha(LocalDateTime tesUltModFecha) {
        this.tesUltModFecha = tesUltModFecha;
    }

    public String getTesUltModUsuario() {
        return tesUltModUsuario;
    }

    public void setTesUltModUsuario(String tesUltModUsuario) {
        this.tesUltModUsuario = tesUltModUsuario;
    }

    public Integer getTesVersion() {
        return tesVersion;
    }

    public void setTesVersion(Integer tesVersion) {
        this.tesVersion = tesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tesPk != null ? tesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoEstudio)) {
            return false;
        }
        SgTipoEstudio other = (SgTipoEstudio) object;
        if ((this.tesPk == null && other.tesPk != null) || (this.tesPk != null && !this.tesPk.equals(other.tesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoEstudio[ tesPk=" + tesPk + " ]";
    }

}

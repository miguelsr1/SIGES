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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nfdPk", scope = SgNivelFormacionDocente.class)
public class SgNivelFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nfdPk;

    private String nfdCodigo;

    private Boolean nfdHabilitado;

    private String nfdNombre;

    private String nfdNombreBusqueda;

    private LocalDateTime nfdUltModFecha;

    private String nfdUltModUsuario;

    private Integer nfdVersion;

    public SgNivelFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nfdNombre);
    }

    public SgNivelFormacionDocente(Long nfdPk) {
        this.nfdPk = nfdPk;
    }

    public Long getNfdPk() {
        return nfdPk;
    }

    public void setNfdPk(Long nfdPk) {
        this.nfdPk = nfdPk;
    }

    public String getNfdCodigo() {
        return nfdCodigo;
    }

    public void setNfdCodigo(String nfdCodigo) {
        this.nfdCodigo = nfdCodigo;
    }

    public Boolean getNfdHabilitado() {
        return nfdHabilitado;
    }

    public void setNfdHabilitado(Boolean nfdHabilitado) {
        this.nfdHabilitado = nfdHabilitado;
    }

    public String getNfdNombre() {
        return nfdNombre;
    }

    public void setNfdNombre(String nfdNombre) {
        this.nfdNombre = nfdNombre;
    }

    public String getNfdNombreBusqueda() {
        return nfdNombreBusqueda;
    }

    public void setNfdNombreBusqueda(String nfdNombreBusqueda) {
        this.nfdNombreBusqueda = nfdNombreBusqueda;
    }

    public LocalDateTime getNfdUltModFecha() {
        return nfdUltModFecha;
    }

    public void setNfdUltModFecha(LocalDateTime nfdUltModFecha) {
        this.nfdUltModFecha = nfdUltModFecha;
    }

    public String getNfdUltModUsuario() {
        return nfdUltModUsuario;
    }

    public void setNfdUltModUsuario(String nfdUltModUsuario) {
        this.nfdUltModUsuario = nfdUltModUsuario;
    }

    public Integer getNfdVersion() {
        return nfdVersion;
    }

    public void setNfdVersion(Integer nfdVersion) {
        this.nfdVersion = nfdVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nfdPk != null ? nfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelFormacionDocente)) {
            return false;
        }
        SgNivelFormacionDocente other = (SgNivelFormacionDocente) object;
        if ((this.nfdPk == null && other.nfdPk != null) || (this.nfdPk != null && !this.nfdPk.equals(other.nfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelFormacionDocente[ nfdPk=" + nfdPk + " ]";
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "motPk", scope = SgAfMotivoTraslado.class)
public class SgAfMotivoTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long motPk;
    
    private String motCodigo;
    
    private Boolean motHabilitado;
    
    private String motNombre;
    
    private String motNombreBusqueda;
    
    private LocalDateTime motUltModFecha;
    
    private String motUltModUsuario;
    
    private Integer motVersion;

    public SgAfMotivoTraslado() {
        this.motHabilitado = Boolean.TRUE;
    }

    public SgAfMotivoTraslado(Long motPk) {
        this.motPk = motPk;
    }

    public Long getMotPk() {
        return motPk;
    }

    public void setMotPk(Long motPk) {
        this.motPk = motPk;
    }

    public String getMotCodigo() {
        return motCodigo;
    }

    public void setMotCodigo(String motCodigo) {
        this.motCodigo = motCodigo;
    }

    public Boolean getMotHabilitado() {
        return motHabilitado;
    }

    public void setMotHabilitado(Boolean motHabilitado) {
        this.motHabilitado = motHabilitado;
    }

    public String getMotNombre() {
        return motNombre;
    }

    public void setMotNombre(String motNombre) {
        this.motNombre = motNombre;
    }

    public String getMotNombreBusqueda() {
        return motNombreBusqueda;
    }

    public void setMotNombreBusqueda(String motNombreBusqueda) {
        this.motNombreBusqueda = motNombreBusqueda;
    }

    public LocalDateTime getMotUltModFecha() {
        return motUltModFecha;
    }

    public void setMotUltModFecha(LocalDateTime motUltModFecha) {
        this.motUltModFecha = motUltModFecha;
    }

    public String getMotUltModUsuario() {
        return motUltModUsuario;
    }

    public void setMotUltModUsuario(String motUltModUsuario) {
        this.motUltModUsuario = motUltModUsuario;
    }

    public Integer getMotVersion() {
        return motVersion;
    }

    public void setMotVersion(Integer motVersion) {
        this.motVersion = motVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (motPk != null ? motPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfMotivoTraslado)) {
            return false;
        }
        SgAfMotivoTraslado other = (SgAfMotivoTraslado) object;
        if ((this.motPk == null && other.motPk != null) || (this.motPk != null && !this.motPk.equals(other.motPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoTraslado[ motPk=" + motPk + " ]";
    }
    
}

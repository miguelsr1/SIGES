/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import sv.gob.mined.siges.utils.SofisStringUtils;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "acaPk", scope = SgAlcanceCapacitacion.class)
public class SgAlcanceCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long acaPk;
    
    private String acaCodigo;
    
    private Boolean acaHabilitado;
    
    private String acaNombre;
    
    private String acaNombreBusqueda;
    
    private LocalDateTime acaUltModFecha;
    
    private String acaUltModUsuario;
    
    private Integer acaVersion;

    public SgAlcanceCapacitacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.acaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.acaNombre);
    }

    public SgAlcanceCapacitacion(Long acaPk) {
        this.acaPk = acaPk;
    }

    public Long getAcaPk() {
        return acaPk;
    }

    public void setAcaPk(Long acaPk) {
        this.acaPk = acaPk;
    }

    public String getAcaCodigo() {
        return acaCodigo;
    }

    public void setAcaCodigo(String acaCodigo) {
        this.acaCodigo = acaCodigo;
    }

    public Boolean getAcaHabilitado() {
        return acaHabilitado;
    }

    public void setAcaHabilitado(Boolean acaHabilitado) {
        this.acaHabilitado = acaHabilitado;
    }

    public String getAcaNombre() {
        return acaNombre;
    }

    public void setAcaNombre(String acaNombre) {
        this.acaNombre = acaNombre;
    }

    public String getAcaNombreBusqueda() {
        return acaNombreBusqueda;
    }

    public void setAcaNombreBusqueda(String acaNombreBusqueda) {
        this.acaNombreBusqueda = acaNombreBusqueda;
    }

    public LocalDateTime getAcaUltModFecha() {
        return acaUltModFecha;
    }

    public void setAcaUltModFecha(LocalDateTime acaUltModFecha) {
        this.acaUltModFecha = acaUltModFecha;
    }

    public String getAcaUltModUsuario() {
        return acaUltModUsuario;
    }

    public void setAcaUltModUsuario(String acaUltModUsuario) {
        this.acaUltModUsuario = acaUltModUsuario;
    }

    public Integer getAcaVersion() {
        return acaVersion;
    }

    public void setAcaVersion(Integer acaVersion) {
        this.acaVersion = acaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acaPk != null ? acaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAlcanceCapacitacion)) {
            return false;
        }
        SgAlcanceCapacitacion other = (SgAlcanceCapacitacion) object;
        if ((this.acaPk == null && other.acaPk != null) || (this.acaPk != null && !this.acaPk.equals(other.acaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAlcanceCapacitacion[ acaPk=" + acaPk + " ]";
    }
    
}

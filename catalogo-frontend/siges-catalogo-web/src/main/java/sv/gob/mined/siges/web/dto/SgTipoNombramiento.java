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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tnoPk", scope = SgTipoNombramiento.class)
public class SgTipoNombramiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long tnoPk;
    
    private String tnoCodigo;
    
    private Boolean tnoHabilitado;
    
    private String tnoNombre;
    
    private String tnoNombreBusqueda;
    
    private LocalDateTime tnoUltModFecha;
    
    private String tnoUltModUsuario;
    
    private Integer tnoVersion;
    
    private Boolean tnoAplicaAcuerdo;
    
    private Boolean tnoAplicaContrato;
    
    private Boolean tnoAplicaOtros;

    public SgTipoNombramiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tnoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tnoNombre);
    }

    public SgTipoNombramiento(Long tnoPk) {
        this.tnoPk = tnoPk;
    }

    public Long getTnoPk() {
        return tnoPk;
    }

    public void setTnoPk(Long tnoPk) {
        this.tnoPk = tnoPk;
    }

    public String getTnoCodigo() {
        return tnoCodigo;
    }

    public void setTnoCodigo(String tnoCodigo) {
        this.tnoCodigo = tnoCodigo;
    }

    public Boolean getTnoHabilitado() {
        return tnoHabilitado;
    }

    public void setTnoHabilitado(Boolean tnoHabilitado) {
        this.tnoHabilitado = tnoHabilitado;
    }

    public String getTnoNombre() {
        return tnoNombre;
    }

    public void setTnoNombre(String tnoNombre) {
        this.tnoNombre = tnoNombre;
    }

    public String getTnoNombreBusqueda() {
        return tnoNombreBusqueda;
    }

    public void setTnoNombreBusqueda(String tnoNombreBusqueda) {
        this.tnoNombreBusqueda = tnoNombreBusqueda;
    }

    public LocalDateTime getTnoUltModFecha() {
        return tnoUltModFecha;
    }

    public void setTnoUltModFecha(LocalDateTime tnoUltModFecha) {
        this.tnoUltModFecha = tnoUltModFecha;
    }

    public String getTnoUltModUsuario() {
        return tnoUltModUsuario;
    }

    public void setTnoUltModUsuario(String tnoUltModUsuario) {
        this.tnoUltModUsuario = tnoUltModUsuario;
    }

    public Integer getTnoVersion() {
        return tnoVersion;
    }

    public void setTnoVersion(Integer tnoVersion) {
        this.tnoVersion = tnoVersion;
    }

    public Boolean getTnoAplicaAcuerdo() {
        return tnoAplicaAcuerdo;
    }

    public void setTnoAplicaAcuerdo(Boolean tnoAplicaAcuerdo) {
        this.tnoAplicaAcuerdo = tnoAplicaAcuerdo;
    }

    public Boolean getTnoAplicaContrato() {
        return tnoAplicaContrato;
    }

    public void setTnoAplicaContrato(Boolean tnoAplicaContrato) {
        this.tnoAplicaContrato = tnoAplicaContrato;
    }

    public Boolean getTnoAplicaOtros() {
        return tnoAplicaOtros;
    }

    public void setTnoAplicaOtros(Boolean tnoAplicaOtros) {
        this.tnoAplicaOtros = tnoAplicaOtros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tnoPk != null ? tnoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoNombramiento)) {
            return false;
        }
        SgTipoNombramiento other = (SgTipoNombramiento) object;
        if ((this.tnoPk == null && other.tnoPk != null) || (this.tnoPk != null && !this.tnoPk.equals(other.tnoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoNombramiento[ tnoPk=" + tnoPk + " ]";
    }
    
}

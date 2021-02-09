/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ipsPk", scope = SgInstitucionPagaSalario.class)
public class SgInstitucionPagaSalario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ipsPk;
    
    private String ipsCodigo;
    
    private Boolean ipsHabilitado;
    
    private String ipsNombre;
    
    private String ipsNombreBusqueda;
    
    private LocalDateTime ipsUltModFecha;
    
    private String ipsUltModUsuario;
    
    private Integer ipsVersion;
    
    private SgTipoInstitucionPaga ipsTipoInstitucion;
    
    private Boolean ipsAplicaAcuerdo;
    
    private Boolean ipsAplicaContrato;
    
    private Boolean ipsAplicaOtros;

    public SgInstitucionPagaSalario() {
        this.ipsHabilitado = Boolean.TRUE;
    }


    public SgInstitucionPagaSalario(Long ipsPk) {
        this.ipsPk = ipsPk;
    }

    public Long getIpsPk() {
        return ipsPk;
    }

    public void setIpsPk(Long ipsPk) {
        this.ipsPk = ipsPk;
    }

    public String getIpsCodigo() {
        return ipsCodigo;
    }

    public void setIpsCodigo(String ipsCodigo) {
        this.ipsCodigo = ipsCodigo;
    }

    public Boolean getIpsHabilitado() {
        return ipsHabilitado;
    }

    public void setIpsHabilitado(Boolean ipsHabilitado) {
        this.ipsHabilitado = ipsHabilitado;
    }

    public String getIpsNombre() {
        return ipsNombre;
    }

    public void setIpsNombre(String ipsNombre) {
        this.ipsNombre = ipsNombre;
    }

    public String getIpsNombreBusqueda() {
        return ipsNombreBusqueda;
    }

    public void setIpsNombreBusqueda(String ipsNombreBusqueda) {
        this.ipsNombreBusqueda = ipsNombreBusqueda;
    }

    public LocalDateTime getIpsUltModFecha() {
        return ipsUltModFecha;
    }

    public void setIpsUltModFecha(LocalDateTime ipsUltModFecha) {
        this.ipsUltModFecha = ipsUltModFecha;
    }

    public String getIpsUltModUsuario() {
        return ipsUltModUsuario;
    }

    public void setIpsUltModUsuario(String ipsUltModUsuario) {
        this.ipsUltModUsuario = ipsUltModUsuario;
    }

    public Integer getIpsVersion() {
        return ipsVersion;
    }

    public void setIpsVersion(Integer ipsVersion) {
        this.ipsVersion = ipsVersion;
    }

    public SgTipoInstitucionPaga getIpsTipoInstitucion() {
        return ipsTipoInstitucion;
    }

    public void setIpsTipoInstitucion(SgTipoInstitucionPaga ipsTipoInstitucion) {
        this.ipsTipoInstitucion = ipsTipoInstitucion;
    }

    public Boolean getIpsAplicaAcuerdo() {
        return ipsAplicaAcuerdo;
    }

    public void setIpsAplicaAcuerdo(Boolean ipsAplicaAcuerdo) {
        this.ipsAplicaAcuerdo = ipsAplicaAcuerdo;
    }

    public Boolean getIpsAplicaContrato() {
        return ipsAplicaContrato;
    }

    public void setIpsAplicaContrato(Boolean ipsAplicaContrato) {
        this.ipsAplicaContrato = ipsAplicaContrato;
    }

    public Boolean getIpsAplicaOtros() {
        return ipsAplicaOtros;
    }

    public void setIpsAplicaOtros(Boolean ipsAplicaOtros) {
        this.ipsAplicaOtros = ipsAplicaOtros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ipsPk != null ? ipsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgInstitucionPagaSalario)) {
            return false;
        }
        SgInstitucionPagaSalario other = (SgInstitucionPagaSalario) object;
        if ((this.ipsPk == null && other.ipsPk != null) || (this.ipsPk != null && !this.ipsPk.equals(other.ipsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInstitucionPagaSalario[ ipsPk=" + ipsPk + " ]";
    }
    
}

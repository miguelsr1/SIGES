/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_institucion_paga_salario", uniqueConstraints = {
    @UniqueConstraint(name = "ips_codigo_uk", columnNames = {"ips_codigo"})
    ,
    @UniqueConstraint(name = "ips_nombre_uk", columnNames = {"ips_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ipsPk", scope = SgInstitucionPagaSalario.class)
public class SgInstitucionPagaSalario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ips_pk")
    private Long ipsPk;
    
    @Size(max = 45)
    @Column(name = "ips_codigo",length = 45)
    @AtributoCodigo
    private String ipsCodigo;
    
    @Column(name = "ips_habilitado")
    @AtributoHabilitado
    private Boolean ipsHabilitado;
    
    @Size(max = 255)
    @Column(name = "ips_nombre",length = 255)
    @AtributoNormalizable
    private String ipsNombre;
    
    @Size(max = 255)
    @Column(name = "ips_nombre_busqueda",length = 255)
    @AtributoNombre
    private String ipsNombreBusqueda;
    
    @Column(name = "ips_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ipsUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ips_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ipsUltModUsuario;
    
    @Column(name = "ips_version")
    @Version
    private Integer ipsVersion;
    
    @JoinColumn(name = "ips_tipo_institucion", referencedColumnName = "tip_pk")
    @ManyToOne
    private SgTipoInstitucionPaga ipsTipoInstitucion;
    
    @Column(name = "ips_aplica_acuerdo")
    private Boolean ipsAplicaAcuerdo;
    
    @Column(name = "ips_aplica_contrato")
    private Boolean ipsAplicaContrato;
    
    @Column(name = "ips_aplica_otros")
    private Boolean ipsAplicaOtros;

    public SgInstitucionPagaSalario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ipsNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ipsNombre);
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

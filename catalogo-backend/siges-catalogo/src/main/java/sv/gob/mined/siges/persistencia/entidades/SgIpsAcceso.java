/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_ips_acceso", uniqueConstraints = {
    @UniqueConstraint(name = "ip_acceso_codigo_uk", columnNames = {"ipa_codigo"})
    ,
    @UniqueConstraint(name = "ip_acceso_nombre_uk", columnNames = {"ipa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ipaPk", scope = SgIpsAcceso.class)
public class SgIpsAcceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ipa_pk")
    private Long ipaPk;
    
    @Size(max = 4)
    @Column(name = "ipa_codigo", length = 4)
    @AtributoCodigo
    private String ipaCodigo;
    
    @Column(name = "ipa_habilitado")
    @AtributoHabilitado
    private Boolean ipaHabilitado;
    
    @Size(max = 100)
    @Column(name = "ipa_nombre", length = 100)
    @AtributoNormalizable
    private String ipaNombre;
    
    @Size(max = 100)
    @Column(name = "ipa_nombre_busqueda", length = 100)
    @AtributoNombre
    private String ipaNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "ipa_descripcion", length = 255)
    @AtributoDescripcion
    private String ipaDescripcion;
    
    @Size(max = 15)
    @Column(name = "ipa_ip_acceso", length = 15)
    private String ipaIpAcceso;
    
    @Column(name = "ipa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ipaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ipa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ipaUltModUsuario;
    
    @Column(name = "ipa_version")
    @Version
    private Integer ipaVersion;

    public SgIpsAcceso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ipaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ipaNombre);
    }

    public SgIpsAcceso(Long ipaPk) {
        this.ipaPk = ipaPk;
    }

    public Long getIpaPk() {
        return ipaPk;
    }

    public void setIpaPk(Long ipaPk) {
        this.ipaPk = ipaPk;
    }

    public String getIpaCodigo() {
        return ipaCodigo;
    }

    public void setIpaCodigo(String ipaCodigo) {
        this.ipaCodigo = ipaCodigo;
    }

    public Boolean getIpaHabilitado() {
        return ipaHabilitado;
    }

    public void setIpaHabilitado(Boolean ipaHabilitado) {
        this.ipaHabilitado = ipaHabilitado;
    }

    public String getIpaNombre() {
        return ipaNombre;
    }

    public void setIpaNombre(String ipaNombre) {
        this.ipaNombre = ipaNombre;
    }

    public String getIpaNombreBusqueda() {
        return ipaNombreBusqueda;
    }

    public void setIpaNombreBusqueda(String ipaNombreBusqueda) {
        this.ipaNombreBusqueda = ipaNombreBusqueda;
    }

    public String getIpaDescripcion() {
        return ipaDescripcion;
    }

    public void setIpaDescripcion(String ipaDescripcion) {
        this.ipaDescripcion = ipaDescripcion;
    }

    public LocalDateTime getIpaUltModFecha() {
        return ipaUltModFecha;
    }

    public void setIpaUltModFecha(LocalDateTime ipaUltModFecha) {
        this.ipaUltModFecha = ipaUltModFecha;
    }

    public String getIpaUltModUsuario() {
        return ipaUltModUsuario;
    }

    public void setIpaUltModUsuario(String ipaUltModUsuario) {
        this.ipaUltModUsuario = ipaUltModUsuario;
    }

    public Integer getIpaVersion() {
        return ipaVersion;
    }

    public void setIpaVersion(Integer ipaVersion) {
        this.ipaVersion = ipaVersion;
    }

    public String getIpaIpAcceso() {
        return ipaIpAcceso;
    }

    public void setIpaIpAcceso(String ipaIpAcceso) {
        this.ipaIpAcceso = ipaIpAcceso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.ipaPk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgIpsAcceso other = (SgIpsAcceso) obj;
        if (!Objects.equals(this.ipaPk, other.ipaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgIpsAcceso{" + "ipaPk=" + ipaPk + '}';
    }
}

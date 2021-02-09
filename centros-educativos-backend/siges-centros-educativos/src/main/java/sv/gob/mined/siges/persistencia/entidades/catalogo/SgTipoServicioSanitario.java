/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
@Table(name = "sg_tipos_servicio_sanitario", uniqueConstraints = {
    @UniqueConstraint(name = "tss_codigo_uk", columnNames = {"tss_codigo"})
    ,
    @UniqueConstraint(name = "tss_nombre_uk", columnNames = {"tss_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tssPk", scope = SgTipoServicioSanitario.class)
public class SgTipoServicioSanitario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tss_pk")
    private Long tssPk;
    
    @Size(max = 4)
    @Column(name = "tss_codigo",length = 4)
    @AtributoCodigo
    private String tssCodigo;
    
    @Column(name = "tss_habilitado")
    @AtributoHabilitado
    private Boolean tssHabilitado;
    
    @Size(max = 255)
    @Column(name = "tss_nombre", length = 255)
    @AtributoNormalizable
    private String tssNombre;
    
    @Size(max = 255)
    @Column(name = "tss_nombre_busqueda", length = 255)
    @AtributoNombre
    private String tssNombreBusqueda;
    
    @Column(name = "tss_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tssUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tss_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tssUltModUsuario;
    
    @Column(name = "tss_version")
    @Version
    private Integer tssVersion;

    public SgTipoServicioSanitario() {
    }


    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tssNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tssNombre);
    }


    public Long getTssPk() {
        return tssPk;
    }

    public void setTssPk(Long tssPk) {
        this.tssPk = tssPk;
    }

    public String getTssCodigo() {
        return tssCodigo;
    }

    public void setTssCodigo(String tssCodigo) {
        this.tssCodigo = tssCodigo;
    }

    public Boolean getTssHabilitado() {
        return tssHabilitado;
    }

    public void setTssHabilitado(Boolean tssHabilitado) {
        this.tssHabilitado = tssHabilitado;
    }

    public String getTssNombre() {
        return tssNombre;
    }

    public void setTssNombre(String tssNombre) {
        this.tssNombre = tssNombre;
    }

    public String getTssNombreBusqueda() {
        return tssNombreBusqueda;
    }

    public void setTssNombreBusqueda(String tssNombreBusqueda) {
        this.tssNombreBusqueda = tssNombreBusqueda;
    }

    public LocalDateTime getTssUltModFecha() {
        return tssUltModFecha;
    }

    public void setTssUltModFecha(LocalDateTime tssUltModFecha) {
        this.tssUltModFecha = tssUltModFecha;
    }


    public String getTssUltModUsuario() {
        return tssUltModUsuario;
    }

    public void setTssUltModUsuario(String tssUltModUsuario) {
        this.tssUltModUsuario = tssUltModUsuario;
    }

    public Integer getTssVersion() {
        return tssVersion;
    }

    public void setTssVersion(Integer tssVersion) {
        this.tssVersion = tssVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tssPk != null ? tssPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoServicioSanitario)) {
            return false;
        }
        SgTipoServicioSanitario other = (SgTipoServicioSanitario) object;
        if ((this.tssPk == null && other.tssPk != null) || (this.tssPk != null && !this.tssPk.equals(other.tssPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoServicioSanitario[ tssPk=" + tssPk + " ]";
    }
    
}

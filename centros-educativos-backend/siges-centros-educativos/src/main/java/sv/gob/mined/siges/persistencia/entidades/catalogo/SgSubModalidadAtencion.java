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
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.UniqueConstraint;
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
@Table(name = "sg_sub_modalidades",uniqueConstraints = {
    @UniqueConstraint(name = "smo_codigo_uk", columnNames = {"smo_codigo"})
    ,
    @UniqueConstraint(name = "smo_nombre_uk", columnNames = {"smo_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "smoPk", scope = SgSubModalidadAtencion.class) 
public class SgSubModalidadAtencion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "smo_pk")
    private Long smoPk;
    
    @Size(max = 10)
    @Column(name = "smo_codigo",length = 10)
    @AtributoCodigo
    private String smoCodigo;
    
    @Size(max = 255)
    @Column(name = "smo_nombre",length = 255)
    @AtributoNormalizable
    private String smoNombre;
    
    @Size(max = 255)
    @Column(name = "smo_nombre_busqueda",length = 255)
    @AtributoNombre
    private String smoNombreBusqueda;
    
    @Size(max = 500)
    @Column(name = "smo_descripcion",length = 500)
    private String smoDescripcion;
    
    @Column(name = "smo_habilitado")
    @AtributoHabilitado
    private Boolean smoHabilitado;
    
    @Column(name = "smo_integrada")
    private Boolean smoIntegrada;
    
    @Column(name = "smo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime smoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "smo_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String smoUltModUsuario;
    
    @Column(name = "smo_version")
    @Version
    private Integer smoVersion;
    
    @JoinColumn(name = "smo_modalidad_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgModalidadAtencion smoModalidadFk;

    public SgSubModalidadAtencion() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.smoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.smoNombre);
    }

    
    public Long getSmoPk() {
        return smoPk;
    }

    public void setSmoPk(Long smoPk) {
        this.smoPk = smoPk;
    }

    public String getSmoCodigo() {
        return smoCodigo;
    }

    public void setSmoCodigo(String smoCodigo) {
        this.smoCodigo = smoCodigo;
    }

    public String getSmoNombre() {
        return smoNombre;
    }

    public void setSmoNombre(String smoNombre) {
        this.smoNombre = smoNombre;
    }

    public String getSmoNombreBusqueda() {
        return smoNombreBusqueda;
    }

    public void setSmoNombreBusqueda(String smoNombreBusqueda) {
        this.smoNombreBusqueda = smoNombreBusqueda;
    }

    public Boolean getSmoIntegrada() {
        return smoIntegrada;
    }

    public void setSmoIntegrada(Boolean smoIntegrada) {
        this.smoIntegrada = smoIntegrada;
    }
    
    public String getSmoDescripcion() {
        return smoDescripcion;
    }

    public void setSmoDescripcion(String smoDescripcion) {
        this.smoDescripcion = smoDescripcion;
    }

    public Boolean getSmoHabilitado() {
        return smoHabilitado;
    }

    public void setSmoHabilitado(Boolean smoHabilitado) {
        this.smoHabilitado = smoHabilitado;
    }

    public LocalDateTime getSmoUltModFecha() {
        return smoUltModFecha;
    }

    public void setSmoUltModFecha(LocalDateTime smoUltModFecha) {
        this.smoUltModFecha = smoUltModFecha;
    }

    public String getSmoUltModUsuario() {
        return smoUltModUsuario;
    }

    public void setSmoUltModUsuario(String smoUltModUsuario) {
        this.smoUltModUsuario = smoUltModUsuario;
    }

    public Integer getSmoVersion() {
        return smoVersion;
    }

    public void setSmoVersion(Integer smoVersion) {
        this.smoVersion = smoVersion;
    }

    public SgModalidadAtencion getSmoModalidadFk() {
        return smoModalidadFk;
    }

    public void setSmoModalidadFk(SgModalidadAtencion smoModalidadFk) {
        this.smoModalidadFk = smoModalidadFk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (smoPk != null ? smoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSubModalidadAtencion)) {
            return false;
        }
        SgSubModalidadAtencion other = (SgSubModalidadAtencion) object;
        if ((this.smoPk == null && other.smoPk != null) || (this.smoPk != null && !this.smoPk.equals(other.smoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSubModalidad[ smoPk=" + smoPk + " ]";
    }
    
}

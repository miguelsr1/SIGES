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
@Table(name = "sg_sistemas_gestion_contenido", uniqueConstraints = {
    @UniqueConstraint(name = "sgc_codigo_uk", columnNames = {"sgc_codigo"})
    ,
    @UniqueConstraint(name = "sgc_nombre_uk", columnNames = {"sgc_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sgcPk", scope = SgSistemaGestionContenido.class)
public class SgSistemaGestionContenido implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sgc_pk")
    private Long sgcPk;
    
    @Size(max = 45)
    @Column(name = "sgc_codigo",length = 45)
    @AtributoCodigo
    private String sgcCodigo;
    
    @Column(name = "sgc_habilitado")
    @AtributoHabilitado
    private Boolean sgcHabilitado;
    
    @Size(max = 255)
    @Column(name = "sgc_nombre",length = 255)
    @AtributoNombre
    private String sgcNombre;
    
    @Size(max = 255)
    @Column(name = "sgc_nombre_busqueda",length = 255)
    @AtributoNormalizable
    private String sgcNombreBusqueda;
    
    @Column(name = "sgc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sgcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "sgc_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String sgcUltModUsuario;
    
    @Column(name = "sgc_version")
    @Version
    private Integer sgcVersion;

    public SgSistemaGestionContenido() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sgcNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sgcNombre);
    }

    public SgSistemaGestionContenido(Long sgcPk) {
        this.sgcPk = sgcPk;
    }

    public Long getSgcPk() {
        return sgcPk;
    }

    public void setSgcPk(Long sgcPk) {
        this.sgcPk = sgcPk;
    }

    public String getSgcCodigo() {
        return sgcCodigo;
    }

    public void setSgcCodigo(String sgcCodigo) {
        this.sgcCodigo = sgcCodigo;
    }

    public Boolean getSgcHabilitado() {
        return sgcHabilitado;
    }

    public void setSgcHabilitado(Boolean sgcHabilitado) {
        this.sgcHabilitado = sgcHabilitado;
    }

    public String getSgcNombre() {
        return sgcNombre;
    }

    public void setSgcNombre(String sgcNombre) {
        this.sgcNombre = sgcNombre;
    }

    public String getSgcNombreBusqueda() {
        return sgcNombreBusqueda;
    }

    public void setSgcNombreBusqueda(String sgcNombreBusqueda) {
        this.sgcNombreBusqueda = sgcNombreBusqueda;
    }

    public LocalDateTime getSgcUltModFecha() {
        return sgcUltModFecha;
    }

    public void setSgcUltModFecha(LocalDateTime sgcUltModFecha) {
        this.sgcUltModFecha = sgcUltModFecha;
    }

    public String getSgcUltModUsuario() {
        return sgcUltModUsuario;
    }

    public void setSgcUltModUsuario(String sgcUltModUsuario) {
        this.sgcUltModUsuario = sgcUltModUsuario;
    }

    public Integer getSgcVersion() {
        return sgcVersion;
    }

    public void setSgcVersion(Integer sgcVersion) {
        this.sgcVersion = sgcVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sgcPk != null ? sgcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSistemaGestionContenido)) {
            return false;
        }
        SgSistemaGestionContenido other = (SgSistemaGestionContenido) object;
        if ((this.sgcPk == null && other.sgcPk != null) || (this.sgcPk != null && !this.sgcPk.equals(other.sgcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSistemaGestionContenido[ sgcPk=" + sgcPk + " ]";
    }
    
}

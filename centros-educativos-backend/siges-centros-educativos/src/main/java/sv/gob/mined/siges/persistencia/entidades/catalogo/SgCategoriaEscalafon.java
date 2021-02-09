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
@Table(name = "sg_categoria_escalafon", uniqueConstraints = {
    @UniqueConstraint(name = "ces_codigo_uk", columnNames = {"ces_codigo"})
    ,
    @UniqueConstraint(name = "ces_nombre_uk", columnNames = {"ces_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cesPk", scope = SgCategoriaEscalafon.class)
public class SgCategoriaEscalafon implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ces_pk")
    private Long cesPk;
    
    @Size(max = 45)
    @Column(name = "ces_codigo")
    @AtributoCodigo
    private String cesCodigo;
    
    @Column(name = "ces_habilitado")
    @AtributoHabilitado
    private Boolean cesHabilitado;
    
    @Size(max = 255)
    @Column(name = "ces_nombre")
    @AtributoNormalizable
    private String cesNombre;
    
    @Size(max = 255)
    @Column(name = "ces_nombre_busqueda")
    @AtributoNombre
    private String cesNombreBusqueda;
    
    @Column(name = "ces_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cesUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ces_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String cesUltModUsuario;
    
    @Column(name = "ces_version")
    @Version
    private Integer cesVersion;

    public SgCategoriaEscalafon() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cesNombre);
    }

    public SgCategoriaEscalafon(Long cesPk) {
        this.cesPk = cesPk;
    }

    public Long getCesPk() {
        return cesPk;
    }

    public void setCesPk(Long cesPk) {
        this.cesPk = cesPk;
    }

    public String getCesCodigo() {
        return cesCodigo;
    }

    public void setCesCodigo(String cesCodigo) {
        this.cesCodigo = cesCodigo;
    }

    public Boolean getCesHabilitado() {
        return cesHabilitado;
    }

    public void setCesHabilitado(Boolean cesHabilitado) {
        this.cesHabilitado = cesHabilitado;
    }

    public String getCesNombre() {
        return cesNombre;
    }

    public void setCesNombre(String cesNombre) {
        this.cesNombre = cesNombre;
    }

    public String getCesNombreBusqueda() {
        return cesNombreBusqueda;
    }

    public void setCesNombreBusqueda(String cesNombreBusqueda) {
        this.cesNombreBusqueda = cesNombreBusqueda;
    }

    public LocalDateTime getCesUltModFecha() {
        return cesUltModFecha;
    }

    public void setCesUltModFecha(LocalDateTime cesUltModFecha) {
        this.cesUltModFecha = cesUltModFecha;
    }

    public String getCesUltModUsuario() {
        return cesUltModUsuario;
    }

    public void setCesUltModUsuario(String cesUltModUsuario) {
        this.cesUltModUsuario = cesUltModUsuario;
    }

    public Integer getCesVersion() {
        return cesVersion;
    }

    public void setCesVersion(Integer cesVersion) {
        this.cesVersion = cesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cesPk != null ? cesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaEscalafon)) {
            return false;
        }
        SgCategoriaEscalafon other = (SgCategoriaEscalafon) object;
        if ((this.cesPk == null && other.cesPk != null) || (this.cesPk != null && !this.cesPk.equals(other.cesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaEscalafon[ cesPk=" + cesPk + " ]";
    }
    
}

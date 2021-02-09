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
@Table(name = "sg_tipo_institucion_paga", uniqueConstraints = {
    @UniqueConstraint(name = "tip_codigo_uk", columnNames = {"tip_codigo"})
    ,
    @UniqueConstraint(name = "tip_nombre_uk", columnNames = {"tip_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tipPk", scope = SgTipoInstitucionPaga.class)
public class SgTipoInstitucionPaga implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tip_pk")
    private Long tipPk;
    
    @Size(max = 45)
    @Column(name = "tip_codigo",length = 45)
    @AtributoCodigo
    private String tipCodigo;
    
    @Column(name = "tip_habilitado")
    @AtributoHabilitado
    private Boolean tipHabilitado;
    
    @Size(max = 255)
    @Column(name = "tip_nombre",length = 255)
    @AtributoNormalizable
    private String tipNombre;
    
    @Size(max = 255)
    @Column(name = "tip_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tipNombreBusqueda;
    
    @Column(name = "tip_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tipUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tip_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tipUltModUsuario;
    
    @Column(name = "tip_version")
    @Version
    private Integer tipVersion;

    public SgTipoInstitucionPaga() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tipNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tipNombre);
    }

    public SgTipoInstitucionPaga(Long tipPk) {
        this.tipPk = tipPk;
    }

    public Long getTipPk() {
        return tipPk;
    }

    public void setTipPk(Long tipPk) {
        this.tipPk = tipPk;
    }

    public String getTipCodigo() {
        return tipCodigo;
    }

    public void setTipCodigo(String tipCodigo) {
        this.tipCodigo = tipCodigo;
    }

    public Boolean getTipHabilitado() {
        return tipHabilitado;
    }

    public void setTipHabilitado(Boolean tipHabilitado) {
        this.tipHabilitado = tipHabilitado;
    }

    public String getTipNombre() {
        return tipNombre;
    }

    public void setTipNombre(String tipNombre) {
        this.tipNombre = tipNombre;
    }

    public String getTipNombreBusqueda() {
        return tipNombreBusqueda;
    }

    public void setTipNombreBusqueda(String tipNombreBusqueda) {
        this.tipNombreBusqueda = tipNombreBusqueda;
    }

    public LocalDateTime getTipUltModFecha() {
        return tipUltModFecha;
    }

    public void setTipUltModFecha(LocalDateTime tipUltModFecha) {
        this.tipUltModFecha = tipUltModFecha;
    }

    public String getTipUltModUsuario() {
        return tipUltModUsuario;
    }

    public void setTipUltModUsuario(String tipUltModUsuario) {
        this.tipUltModUsuario = tipUltModUsuario;
    }

    public Integer getTipVersion() {
        return tipVersion;
    }

    public void setTipVersion(Integer tipVersion) {
        this.tipVersion = tipVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipPk != null ? tipPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoInstitucionPaga)) {
            return false;
        }
        SgTipoInstitucionPaga other = (SgTipoInstitucionPaga) object;
        if ((this.tipPk == null && other.tipPk != null) || (this.tipPk != null && !this.tipPk.equals(other.tipPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoInstitucionPaga[ tipPk=" + tipPk + " ]";
    }
    
}

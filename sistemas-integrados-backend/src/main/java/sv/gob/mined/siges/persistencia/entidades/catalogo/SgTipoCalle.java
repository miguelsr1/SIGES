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
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_tipos_calle", uniqueConstraints = {
    @UniqueConstraint(name = "tll_codigo_uk", columnNames = {"tll_codigo"})
    ,
    @UniqueConstraint(name = "tll_nombre_uk", columnNames = {"tll_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tllPk", scope = SgTipoCalle.class)
public class SgTipoCalle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tll_pk")
    private Long tllPk;
    
    @Size(max = 45)
    @Column(name = "tll_codigo",length = 45)
    @AtributoCodigo
    private String tllCodigo;
    
    @Column(name = "tll_habilitado")
    @AtributoHabilitado
    private Boolean tllHabilitado;
    
    @Size(max = 255)
    @Column(name = "tll_nombre",length = 255)
    @AtributoNormalizable
    private String tllNombre;
    
    @Size(max = 255)
    @Column(name = "tll_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tllNombreBusqueda;
    
    @Column(name = "tll_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tllUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tll_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tllUltModUsuario;
    
    @Column(name = "tll_version")
    @Version
    private Integer tllVersion;

    public SgTipoCalle() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tllNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tllNombre);
    }

    public SgTipoCalle(Long tllPk) {
        this.tllPk = tllPk;
    }

    public Long getTllPk() {
        return tllPk;
    }

    public void setTllPk(Long tllPk) {
        this.tllPk = tllPk;
    }

    public String getTllCodigo() {
        return tllCodigo;
    }

    public void setTllCodigo(String tllCodigo) {
        this.tllCodigo = tllCodigo;
    }

    public Boolean getTllHabilitado() {
        return tllHabilitado;
    }

    public void setTllHabilitado(Boolean tllHabilitado) {
        this.tllHabilitado = tllHabilitado;
    }

    public String getTllNombre() {
        return tllNombre;
    }

    public void setTllNombre(String tllNombre) {
        this.tllNombre = tllNombre;
    }

    public String getTllNombreBusqueda() {
        return tllNombreBusqueda;
    }

    public void setTllNombreBusqueda(String tllNombreBusqueda) {
        this.tllNombreBusqueda = tllNombreBusqueda;
    }

    public LocalDateTime getTllUltModFecha() {
        return tllUltModFecha;
    }

    public void setTllUltModFecha(LocalDateTime tllUltModFecha) {
        this.tllUltModFecha = tllUltModFecha;
    }


    public String getTllUltModUsuario() {
        return tllUltModUsuario;
    }

    public void setTllUltModUsuario(String tllUltModUsuario) {
        this.tllUltModUsuario = tllUltModUsuario;
    }

    public Integer getTllVersion() {
        return tllVersion;
    }

    public void setTllVersion(Integer tllVersion) {
        this.tllVersion = tllVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tllPk != null ? tllPk.hashCode() : 0);
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
        final SgTipoCalle other = (SgTipoCalle) obj;
        if (!Objects.equals(this.tllPk, other.tllPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoCalle[ tllPk=" + tllPk + " ]";
    }
    
}

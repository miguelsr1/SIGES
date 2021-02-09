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
@Table(name = "sg_niveles_idioma", uniqueConstraints = {
    @UniqueConstraint(name = "nid_codigo_uk", columnNames = {"nid_codigo"})
    ,
    @UniqueConstraint(name = "nid_nombre_uk", columnNames = {"nid_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nidPk", scope = SgNivelIdioma.class)
public class SgNivelIdioma implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nid_pk")
    private Long nidPk;
    
    @Size(max = 45)
    @Column(name = "nid_codigo",length = 45)
    @AtributoCodigo
    private String nidCodigo;
    
    @Column(name = "nid_habilitado")
    @AtributoHabilitado
    private Boolean nidHabilitado;
    
    @Size(max = 255)
    @Column(name = "nid_nombre",length = 255)
    @AtributoNormalizable
    private String nidNombre;
    
    @Size(max = 255)
    @Column(name = "nid_nombre_busqueda",length = 255)
    @AtributoNombre
    private String nidNombreBusqueda;
    
    @Column(name = "nid_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nidUltModFecha;
    
    @Size(max = 45)
    @Column(name = "nid_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String nidUltModUsuario;
    
    @Column(name = "nid_version")
    @Version
    private Integer nidVersion;

    public SgNivelIdioma() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nidNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nidNombre);
    }

    public SgNivelIdioma(Long nidPk) {
        this.nidPk = nidPk;
    }

    public Long getNidPk() {
        return nidPk;
    }

    public void setNidPk(Long nidPk) {
        this.nidPk = nidPk;
    }

    public String getNidCodigo() {
        return nidCodigo;
    }

    public void setNidCodigo(String nidCodigo) {
        this.nidCodigo = nidCodigo;
    }

    public Boolean getNidHabilitado() {
        return nidHabilitado;
    }

    public void setNidHabilitado(Boolean nidHabilitado) {
        this.nidHabilitado = nidHabilitado;
    }

    public String getNidNombre() {
        return nidNombre;
    }

    public void setNidNombre(String nidNombre) {
        this.nidNombre = nidNombre;
    }

    public String getNidNombreBusqueda() {
        return nidNombreBusqueda;
    }

    public void setNidNombreBusqueda(String nidNombreBusqueda) {
        this.nidNombreBusqueda = nidNombreBusqueda;
    }

    public LocalDateTime getNidUltModFecha() {
        return nidUltModFecha;
    }

    public void setNidUltModFecha(LocalDateTime nidUltModFecha) {
        this.nidUltModFecha = nidUltModFecha;
    }

    public String getNidUltModUsuario() {
        return nidUltModUsuario;
    }

    public void setNidUltModUsuario(String nidUltModUsuario) {
        this.nidUltModUsuario = nidUltModUsuario;
    }

    public Integer getNidVersion() {
        return nidVersion;
    }

    public void setNidVersion(Integer nidVersion) {
        this.nidVersion = nidVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nidPk != null ? nidPk.hashCode() : 0);
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
        final SgNivelIdioma other = (SgNivelIdioma) obj;
        if (!Objects.equals(this.nidPk, other.nidPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelIdioma[ nidPk=" + nidPk + " ]";
    }
    
}

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
@Table(name = "sg_nivel_formacion_docente", uniqueConstraints = {
    @UniqueConstraint(name = "nfd_codigo_uk", columnNames = {"nfd_codigo"})
    ,
    @UniqueConstraint(name = "nfd_nombre_uk", columnNames = {"nfd_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nfdPk", scope = SgNivelFormacionDocente.class)
public class SgNivelFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nfd_pk")
    private Long nfdPk;
    
    @Size(max = 45)
    @Column(name = "nfd_codigo",length = 45)
    @AtributoCodigo
    private String nfdCodigo;
    
    @Column(name = "nfd_habilitado")
    @AtributoHabilitado
    private Boolean nfdHabilitado;
    
    @Size(max = 255)
    @Column(name = "nfd_nombre",length = 255)
    @AtributoNormalizable
    private String nfdNombre;
    
    @Size(max = 255)
    @Column(name = "nfd_nombre_busqueda",length = 255)
    @AtributoNombre
    private String nfdNombreBusqueda;
    
    @Column(name = "nfd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nfdUltModFecha;
    
    @Size(max = 45)
    @Column(name = "nfd_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String nfdUltModUsuario;
    
    @Column(name = "nfd_version")
    @Version
    private Integer nfdVersion;

    public SgNivelFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nfdNombre);
    }

    public SgNivelFormacionDocente(Long nfdPk) {
        this.nfdPk = nfdPk;
    }

    public Long getNfdPk() {
        return nfdPk;
    }

    public void setNfdPk(Long nfdPk) {
        this.nfdPk = nfdPk;
    }

    public String getNfdCodigo() {
        return nfdCodigo;
    }

    public void setNfdCodigo(String nfdCodigo) {
        this.nfdCodigo = nfdCodigo;
    }

    public Boolean getNfdHabilitado() {
        return nfdHabilitado;
    }

    public void setNfdHabilitado(Boolean nfdHabilitado) {
        this.nfdHabilitado = nfdHabilitado;
    }

    public String getNfdNombre() {
        return nfdNombre;
    }

    public void setNfdNombre(String nfdNombre) {
        this.nfdNombre = nfdNombre;
    }

    public String getNfdNombreBusqueda() {
        return nfdNombreBusqueda;
    }

    public void setNfdNombreBusqueda(String nfdNombreBusqueda) {
        this.nfdNombreBusqueda = nfdNombreBusqueda;
    }

    public LocalDateTime getNfdUltModFecha() {
        return nfdUltModFecha;
    }

    public void setNfdUltModFecha(LocalDateTime nfdUltModFecha) {
        this.nfdUltModFecha = nfdUltModFecha;
    }

    public String getNfdUltModUsuario() {
        return nfdUltModUsuario;
    }

    public void setNfdUltModUsuario(String nfdUltModUsuario) {
        this.nfdUltModUsuario = nfdUltModUsuario;
    }

    public Integer getNfdVersion() {
        return nfdVersion;
    }

    public void setNfdVersion(Integer nfdVersion) {
        this.nfdVersion = nfdVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nfdPk != null ? nfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelFormacionDocente)) {
            return false;
        }
        SgNivelFormacionDocente other = (SgNivelFormacionDocente) object;
        if ((this.nfdPk == null && other.nfdPk != null) || (this.nfdPk != null && !this.nfdPk.equals(other.nfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelFormacionDocente[ nfdPk=" + nfdPk + " ]";
    }
    
}

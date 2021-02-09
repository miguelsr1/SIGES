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
@Table(name = "sg_categoria_formacion_docente", uniqueConstraints = {
    @UniqueConstraint(name = "cfd_codigo_uk", columnNames = {"cfd_codigo"})
    ,
    @UniqueConstraint(name = "cfd_nombre_uk", columnNames = {"cfd_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cfdPk", scope = SgCategoriaFormacionDocente.class)
public class SgCategoriaFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cfd_pk")
    private Long cfdPk;
    
    @Size(max = 45)
    @Column(name = "cfd_codigo",length = 45)
    @AtributoCodigo
    private String cfdCodigo;
    
    @Column(name = "cfd_habilitado")
    @AtributoHabilitado
    private Boolean cfdHabilitado;
    
    @Size(max = 255)
    @Column(name = "cfd_nombre",length = 255)
    @AtributoNormalizable
    private String cfdNombre;
    
    @Size(max = 255)
    @Column(name = "cfd_nombre_busqueda",length = 255)
    @AtributoNombre
    private String cfdNombreBusqueda;
    
    @Column(name = "cfd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cfdUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cfd_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String cfdUltModUsuario;
    
    @Column(name = "cfd_version")
    @Version
    private Integer cfdVersion;

    public SgCategoriaFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cfdNombre);
    }

    public Long getCfdPk() {
        return cfdPk;
    }

    public void setCfdPk(Long cfdPk) {
        this.cfdPk = cfdPk;
    }

    public String getCfdCodigo() {
        return cfdCodigo;
    }

    public void setCfdCodigo(String cfdCodigo) {
        this.cfdCodigo = cfdCodigo;
    }

    public Boolean getCfdHabilitado() {
        return cfdHabilitado;
    }

    public void setCfdHabilitado(Boolean cfdHabilitado) {
        this.cfdHabilitado = cfdHabilitado;
    }

    public String getCfdNombre() {
        return cfdNombre;
    }

    public void setCfdNombre(String cfdNombre) {
        this.cfdNombre = cfdNombre;
    }

    public String getCfdNombreBusqueda() {
        return cfdNombreBusqueda;
    }

    public void setCfdNombreBusqueda(String cfdNombreBusqueda) {
        this.cfdNombreBusqueda = cfdNombreBusqueda;
    }

    public LocalDateTime getCfdUltModFecha() {
        return cfdUltModFecha;
    }

    public void setCfdUltModFecha(LocalDateTime cfdUltModFecha) {
        this.cfdUltModFecha = cfdUltModFecha;
    }

    public String getCfdUltModUsuario() {
        return cfdUltModUsuario;
    }

    public void setCfdUltModUsuario(String cfdUltModUsuario) {
        this.cfdUltModUsuario = cfdUltModUsuario;
    }

    public Integer getCfdVersion() {
        return cfdVersion;
    }

    public void setCfdVersion(Integer cfdVersion) {
        this.cfdVersion = cfdVersion;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cfdPk != null ? cfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCategoriaFormacionDocente)) {
            return false;
        }
        SgCategoriaFormacionDocente other = (SgCategoriaFormacionDocente) object;
        if ((this.cfdPk == null && other.cfdPk != null) || (this.cfdPk != null && !this.cfdPk.equals(other.cfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaFormacionDocente[ cfdPk=" + cfdPk + " ]";
    }
    
}

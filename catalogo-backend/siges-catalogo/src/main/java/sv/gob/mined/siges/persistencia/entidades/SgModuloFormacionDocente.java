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
@Table(name = "sg_modulo_formacion_docente", uniqueConstraints = {
    @UniqueConstraint(name = "mfd_codigo_uk", columnNames = {"mfd_codigo"})
    ,
    @UniqueConstraint(name = "mfd_nombre_uk", columnNames = {"mfd_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mfdPk", scope = SgModuloFormacionDocente.class)
public class SgModuloFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mfd_pk")
    private Long mfdPk;
    
    @Size(max = 45)
    @Column(name = "mfd_codigo",length = 45)
    @AtributoCodigo
    private String mfdCodigo;
    
    @Column(name = "mfd_habilitado")
    @AtributoHabilitado
    private Boolean mfdHabilitado;
    
    @Size(max = 255)
    @Column(name = "mfd_nombre",length = 255)
    @AtributoNormalizable
    private String mfdNombre;
    
    @Size(max = 255)
    @Column(name = "mfd_nombre_busqueda",length = 255)
    @AtributoNombre
    private String mfdNombreBusqueda;
    
    @Column(name = "mfd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mfdUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mfd_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mfdUltModUsuario;
    
    @Column(name = "mfd_version")
    @Version
    private Integer mfdVersion;

    public SgModuloFormacionDocente() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mfdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mfdNombre);
    }

    public SgModuloFormacionDocente(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public Long getMfdPk() {
        return mfdPk;
    }

    public void setMfdPk(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public String getMfdCodigo() {
        return mfdCodigo;
    }

    public void setMfdCodigo(String mfdCodigo) {
        this.mfdCodigo = mfdCodigo;
    }

    public Boolean getMfdHabilitado() {
        return mfdHabilitado;
    }

    public void setMfdHabilitado(Boolean mfdHabilitado) {
        this.mfdHabilitado = mfdHabilitado;
    }

    public String getMfdNombre() {
        return mfdNombre;
    }

    public void setMfdNombre(String mfdNombre) {
        this.mfdNombre = mfdNombre;
    }

    public String getMfdNombreBusqueda() {
        return mfdNombreBusqueda;
    }

    public void setMfdNombreBusqueda(String mfdNombreBusqueda) {
        this.mfdNombreBusqueda = mfdNombreBusqueda;
    }

    public LocalDateTime getMfdUltModFecha() {
        return mfdUltModFecha;
    }

    public void setMfdUltModFecha(LocalDateTime mfdUltModFecha) {
        this.mfdUltModFecha = mfdUltModFecha;
    }

    public String getMfdUltModUsuario() {
        return mfdUltModUsuario;
    }

    public void setMfdUltModUsuario(String mfdUltModUsuario) {
        this.mfdUltModUsuario = mfdUltModUsuario;
    }

    public Integer getMfdVersion() {
        return mfdVersion;
    }

    public void setMfdVersion(Integer mfdVersion) {
        this.mfdVersion = mfdVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mfdPk != null ? mfdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModuloFormacionDocente)) {
            return false;
        }
        SgModuloFormacionDocente other = (SgModuloFormacionDocente) object;
        if ((this.mfdPk == null && other.mfdPk != null) || (this.mfdPk != null && !this.mfdPk.equals(other.mfdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente[ mfdPk=" + mfdPk + " ]";
    }
    
}

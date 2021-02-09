/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_areas_asignatura_modulo", uniqueConstraints = {
    @UniqueConstraint(name = "aam_codigo_uk", columnNames = {"aam_codigo"})
    ,
    @UniqueConstraint(name = "aam_nombre_uk", columnNames = {"aam_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aamPk", scope = SgAreaAsignaturaModulo.class)
@Audited
public class SgAreaAsignaturaModulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aam_pk", nullable = false)
    private Long aamPk;

    @Size(max = 45)
    @Column(name = "aam_codigo", length = 45)
    @AtributoCodigo
    private String aamCodigo;

    @Size(max = 255)
    @Column(name = "aam_nombre", length = 255)
    @AtributoNormalizable
    private String aamNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "aam_nombre_busqueda", length = 255)
    private String aamNombreBusqueda;

    @Column(name = "aam_habilitado")
    @AtributoHabilitado
    private Boolean aamHabilitado;

    @Column(name = "aam_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aamUltModFecha;

    @Size(max = 45)
    @Column(name = "aam_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aamUltModUsuario;

    @Column(name = "aam_version")
    @Version
    private Integer aamVersion;

    public SgAreaAsignaturaModulo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.aamNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.aamNombre);
    }

    public Long getAamPk() {
        return aamPk;
    }

    public void setAamPk(Long aamPk) {
        this.aamPk = aamPk;
    }

    public String getAamCodigo() {
        return aamCodigo;
    }

    public void setAamCodigo(String aamCodigo) {
        this.aamCodigo = aamCodigo;
    }

    public String getAamNombre() {
        return aamNombre;
    }

    public void setAamNombre(String aamNombre) {
        this.aamNombre = aamNombre;
    }

    public String getAamNombreBusqueda() {
        return aamNombreBusqueda;
    }

    public void setAamNombreBusqueda(String aamNombreBusqueda) {
        this.aamNombreBusqueda = aamNombreBusqueda;
    }

    public Boolean getAamHabilitado() {
        return aamHabilitado;
    }

    public void setAamHabilitado(Boolean aamHabilitado) {
        this.aamHabilitado = aamHabilitado;
    }

    public LocalDateTime getAamUltModFecha() {
        return aamUltModFecha;
    }

    public void setAamUltModFecha(LocalDateTime aamUltModFecha) {
        this.aamUltModFecha = aamUltModFecha;
    }

    public String getAamUltModUsuario() {
        return aamUltModUsuario;
    }

    public void setAamUltModUsuario(String aamUltModUsuario) {
        this.aamUltModUsuario = aamUltModUsuario;
    }

    public Integer getAamVersion() {
        return aamVersion;
    }

    public void setAamVersion(Integer aamVersion) {
        this.aamVersion = aamVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.aamPk);
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
        final SgAreaAsignaturaModulo other = (SgAreaAsignaturaModulo) obj;
        if (!Objects.equals(this.aamPk, other.aamPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAreaAsignaturaModulo{" + "aamPk=" + aamPk + ", aamCodigo=" + aamCodigo + ", aamNombre=" + aamNombre + ", aamNombreBusqueda=" + aamNombreBusqueda + ", aamHabilitado=" + aamHabilitado + ", aamUltModFecha=" + aamUltModFecha + ", aamUltModUsuario=" + aamUltModUsuario + ", aamVersion=" + aamVersion + '}';
    }
    
    

}

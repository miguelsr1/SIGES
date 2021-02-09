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

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_jornadas_laborales", uniqueConstraints = {
    @UniqueConstraint(name = "jla_codigo_uk", columnNames = {"jla_codigo"})
    ,
    @UniqueConstraint(name = "jla_nombre_uk", columnNames = {"jla_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "jlaPk", scope = SgJornadaLaboral.class)
public class SgJornadaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "jla_pk", nullable = false)
    private Long jlaPk;

    @Size(max = 4)
    @Column(name = "jla_codigo", length = 4)
    @AtributoCodigo
    private String jlaCodigo;

    @Size(max = 255)
    @Column(name = "jla_nombre", length = 255)
    @AtributoNormalizable
    private String jlaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "jla_nombre_busqueda", length = 255)
    private String jlaNombreBusqueda;

    @Column(name = "jla_habilitado")
    @AtributoHabilitado
    private Boolean jlaHabilitado;

    @Column(name = "jla_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime jlaUltModFecha;

    @Size(max = 45)
    @Column(name = "jla_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String jlaUltModUsuario;

    @Column(name = "jla_version")
    @Version
    private Integer jlaVersion;

    public SgJornadaLaboral() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.jlaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.jlaNombre);
    }

    public Long getJlaPk() {
        return jlaPk;
    }

    public void setJlaPk(Long jlaPk) {
        this.jlaPk = jlaPk;
    }

    public String getJlaCodigo() {
        return jlaCodigo;
    }

    public void setJlaCodigo(String jlaCodigo) {
        this.jlaCodigo = jlaCodigo;
    }

    public String getJlaNombre() {
        return jlaNombre;
    }

    public void setJlaNombre(String jlaNombre) {
        this.jlaNombre = jlaNombre;
    }

    public String getJlaNombreBusqueda() {
        return jlaNombreBusqueda;
    }

    public void setJlaNombreBusqueda(String jlaNombreBusqueda) {
        this.jlaNombreBusqueda = jlaNombreBusqueda;
    }

    public Boolean getJlaHabilitado() {
        return jlaHabilitado;
    }

    public void setJlaHabilitado(Boolean jlaHabilitado) {
        this.jlaHabilitado = jlaHabilitado;
    }

    public LocalDateTime getJlaUltModFecha() {
        return jlaUltModFecha;
    }

    public void setJlaUltModFecha(LocalDateTime jlaUltModFecha) {
        this.jlaUltModFecha = jlaUltModFecha;
    }

    public String getJlaUltModUsuario() {
        return jlaUltModUsuario;
    }

    public void setJlaUltModUsuario(String jlaUltModUsuario) {
        this.jlaUltModUsuario = jlaUltModUsuario;
    }

    public Integer getJlaVersion() {
        return jlaVersion;
    }

    public void setJlaVersion(Integer jlaVersion) {
        this.jlaVersion = jlaVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.jlaPk);
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
        final SgJornadaLaboral other = (SgJornadaLaboral) obj;
        if (!Objects.equals(this.jlaPk, other.jlaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesJornadaLaboral[ jlaPk=" + jlaPk + " ]";
    }
}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
@Table(name = "sg_recursos", uniqueConstraints = {
    @UniqueConstraint(name = "rcs_codigo_uk", columnNames = {"rcs_codigo"})
    ,
    @UniqueConstraint(name = "rcs_nombre_uk", columnNames = {"rcs_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rcsPk", scope = SgRecurso.class)
@Audited
public class SgRecurso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rcs_pk", nullable = false)
    private Long rcsPk;

    @Size(max = 45)
    @Column(name = "rcs_codigo", length = 45)
    @AtributoCodigo
    private String rcsCodigo;

    @Size(max = 255)
    @Column(name = "rcs_nombre", length = 255)
    @AtributoNormalizable
    private String rcsNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "rcs_nombre_busqueda", length = 255)
    private String rcsNombreBusqueda;

    @Column(name = "rcs_habilitado")
    @AtributoHabilitado
    private Boolean rcsHabilitado;

    @Column(name = "rcs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rcsUltModFecha;

    @Size(max = 45)
    @Column(name = "rcs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rcsUltModUsuario;

    @Column(name = "rcs_version")
    @Version
    private Integer rcsVersion;

    public SgRecurso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.rcsNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.rcsNombre);
    }

    public Long getRcsPk() {
        return rcsPk;
    }

    public void setRcsPk(Long rcsPk) {
        this.rcsPk = rcsPk;
    }

    public String getRcsCodigo() {
        return rcsCodigo;
    }

    public void setRcsCodigo(String rcsCodigo) {
        this.rcsCodigo = rcsCodigo;
    }

    public String getRcsNombre() {
        return rcsNombre;
    }

    public void setRcsNombre(String rcsNombre) {
        this.rcsNombre = rcsNombre;
    }

    public String getRcsNombreBusqueda() {
        return rcsNombreBusqueda;
    }

    public void setRcsNombreBusqueda(String rcsNombreBusqueda) {
        this.rcsNombreBusqueda = rcsNombreBusqueda;
    }

    public Boolean getRcsHabilitado() {
        return rcsHabilitado;
    }

    public void setRcsHabilitado(Boolean rcsHabilitado) {
        this.rcsHabilitado = rcsHabilitado;
    }

    public LocalDateTime getRcsUltModFecha() {
        return rcsUltModFecha;
    }

    public void setRcsUltModFecha(LocalDateTime rcsUltModFecha) {
        this.rcsUltModFecha = rcsUltModFecha;
    }

    public String getRcsUltModUsuario() {
        return rcsUltModUsuario;
    }

    public void setRcsUltModUsuario(String rcsUltModUsuario) {
        this.rcsUltModUsuario = rcsUltModUsuario;
    }

    public Integer getRcsVersion() {
        return rcsVersion;
    }

    public void setRcsVersion(Integer rcsVersion) {
        this.rcsVersion = rcsVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rcsPk);
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
        final SgRecurso other = (SgRecurso) obj;
        if (!Objects.equals(this.rcsPk, other.rcsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRecursos{" + "rcsPk=" + rcsPk + ", rcsCodigo=" + rcsCodigo + ", rcsNombre=" + rcsNombre + ", rcsNombreBusqueda=" + rcsNombreBusqueda + ", rcsHabilitado=" + rcsHabilitado + ", rcsUltModFecha=" + rcsUltModFecha + ", rcsUltModUsuario=" + rcsUltModUsuario + ", rcsVersion=" + rcsVersion + '}';
    }
    
    

}

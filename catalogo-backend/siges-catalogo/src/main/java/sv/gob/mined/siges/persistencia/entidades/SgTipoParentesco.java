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
@Table(name = "sg_tipos_parentesco", uniqueConstraints = {
    @UniqueConstraint(name = "tpa_codigo_uk", columnNames = {"tpa_codigo"})
    ,
    @UniqueConstraint(name = "tpa_nombre_uk", columnNames = {"tpa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tpaPk", scope = SgTipoParentesco.class)
public class SgTipoParentesco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tpa_pk", nullable = false)
    private Long tpaPk;

    @Size(max = 45)
    @Column(name = "tpa_codigo", length = 45)
    @AtributoCodigo
    private String tpaCodigo;

    @Size(max = 255)
    @Column(name = "tpa_nombre", length = 255)
    @AtributoNormalizable
    private String tpaNombre;

    @Size(max = 255)
    @Column(name = "tpa_nombre_busqueda", length = 255)
    @AtributoNombre
    private String tpaNombreBusqueda;

    @Column(name = "tpa_habilitado")
    @AtributoHabilitado
    private Boolean tpaHabilitado;

    @Column(name = "tpa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tpaUltModFecha;

    @Size(max = 45)
    @Column(name = "tpa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tpaUltModUsuario;

    @Column(name = "tpa_version")
    @Version
    private Integer tpaVersion;

    public SgTipoParentesco() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tpaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tpaNombre);
    }

    public Long getTpaPk() {
        return tpaPk;
    }

    public void setTpaPk(Long tpaPk) {
        this.tpaPk = tpaPk;
    }

    public String getTpaCodigo() {
        return tpaCodigo;
    }

    public void setTpaCodigo(String tpaCodigo) {
        this.tpaCodigo = tpaCodigo;
    }

    public String getTpaNombre() {
        return tpaNombre;
    }

    public void setTpaNombre(String tpaNombre) {
        this.tpaNombre = tpaNombre;
    }

    public String getTpaNombreBusqueda() {
        return tpaNombreBusqueda;
    }

    public void setTpaNombreBusqueda(String tpaNombreBusqueda) {
        this.tpaNombreBusqueda = tpaNombreBusqueda;
    }

    public Boolean getTpaHabilitado() {
        return tpaHabilitado;
    }

    public void setTpaHabilitado(Boolean tpaHabilitado) {
        this.tpaHabilitado = tpaHabilitado;
    }

    public LocalDateTime getTpaUltModFecha() {
        return tpaUltModFecha;
    }

    public void setTpaUltModFecha(LocalDateTime tpaUltModFecha) {
        this.tpaUltModFecha = tpaUltModFecha;
    }

    public String getTpaUltModUsuario() {
        return tpaUltModUsuario;
    }

    public void setTpaUltModUsuario(String tpaUltModUsuario) {
        this.tpaUltModUsuario = tpaUltModUsuario;
    }

    public Integer getTpaVersion() {
        return tpaVersion;
    }

    public void setTpaVersion(Integer tpaVersion) {
        this.tpaVersion = tpaVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tpaPk);
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
        final SgTipoParentesco other = (SgTipoParentesco) obj;
        if (!Objects.equals(this.tpaPk, other.tpaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoParentesco{" + "tpaPk=" + tpaPk + ", tpaCodigo=" + tpaCodigo + ", tpaNombre=" + tpaNombre + ", tpaNombreBusqueda=" + tpaNombreBusqueda + ", tpaHabilitado=" + tpaHabilitado + ", tpaUltModFecha=" + tpaUltModFecha + ", tpaUltModUsuario=" + tpaUltModUsuario + ", tpaVersion=" + tpaVersion + '}';
    }

}

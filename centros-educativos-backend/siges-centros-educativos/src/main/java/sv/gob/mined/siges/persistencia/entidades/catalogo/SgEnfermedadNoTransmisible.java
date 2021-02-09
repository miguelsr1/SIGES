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

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_enfer_no_transm", uniqueConstraints = {
    @UniqueConstraint(name = "ent_codigo_uk", columnNames = {"ent_codigo"})
    ,
    @UniqueConstraint(name = "ent_nombre_uk", columnNames = {"ent_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "entPk", scope = SgEnfermedadNoTransmisible.class)
public class SgEnfermedadNoTransmisible implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ent_pk", nullable = false)
    private Long entPk;

    @Size(max = 45)
    @Column(name = "ent_codigo", length = 45)
    @AtributoCodigo
    private String entCodigo;

    @Size(max = 255)
    @Column(name = "ent_nombre", length = 255)
    @AtributoNormalizable
    private String entNombre;

    @Size(max = 255)
    @Column(name = "ent_nombre_busqueda", length = 255)
    @AtributoNombre
    private String entNombreBusqueda;

    @Column(name = "ent_habilitado")
    @AtributoHabilitado
    private Boolean entHabilitado;

    @Column(name = "ent_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime entUltModFecha;

    @Size(max = 45)
    @Column(name = "ent_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String entUltModUsuario;

    @Column(name = "ent_version")
    @Version
    private Integer entVersion;

    public SgEnfermedadNoTransmisible() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.entNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.entNombre);
    }

    public Long getEntPk() {
        return entPk;
    }

    public void setEntPk(Long entPk) {
        this.entPk = entPk;
    }

    public String getEntCodigo() {
        return entCodigo;
    }

    public void setEntCodigo(String entCodigo) {
        this.entCodigo = entCodigo;
    }

    public String getEntNombre() {
        return entNombre;
    }

    public void setEntNombre(String entNombre) {
        this.entNombre = entNombre;
    }

    public String getEntNombreBusqueda() {
        return entNombreBusqueda;
    }

    public void setEntNombreBusqueda(String entNombreBusqueda) {
        this.entNombreBusqueda = entNombreBusqueda;
    }

    public Boolean getEntHabilitado() {
        return entHabilitado;
    }

    public void setEntHabilitado(Boolean entHabilitado) {
        this.entHabilitado = entHabilitado;
    }

    public LocalDateTime getEntUltModFecha() {
        return entUltModFecha;
    }

    public void setEntUltModFecha(LocalDateTime entUltModFecha) {
        this.entUltModFecha = entUltModFecha;
    }

    public String getEntUltModUsuario() {
        return entUltModUsuario;
    }

    public void setEntUltModUsuario(String entUltModUsuario) {
        this.entUltModUsuario = entUltModUsuario;
    }

    public Integer getEntVersion() {
        return entVersion;
    }

    public void setEntVersion(Integer entVersion) {
        this.entVersion = entVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.entPk);
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
        final SgEnfermedadNoTransmisible other = (SgEnfermedadNoTransmisible) obj;
        if (!Objects.equals(this.entPk, other.entPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEnferNoTransm{" + "entPk=" + entPk + ", entCodigo=" + entCodigo + ", entNombre=" + entNombre + ", entNombreBusqueda=" + entNombreBusqueda + ", entHabilitado=" + entHabilitado + ", entUltModFecha=" + entUltModFecha + ", entUltModUsuario=" + entUltModUsuario + ", entVersion=" + entVersion + '}';
    }

}

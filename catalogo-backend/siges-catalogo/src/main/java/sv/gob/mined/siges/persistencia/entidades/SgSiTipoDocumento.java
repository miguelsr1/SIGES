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
@Table(name = "sg_si_tipo_documento", uniqueConstraints = {
    @UniqueConstraint(name = "tid_codigo_uk", columnNames = {"tid_codigo"})
    ,
    @UniqueConstraint(name = "tid_nombre_uk", columnNames = {"tid_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tidPk", scope = SgSiTipoDocumento.class)
@Audited
public class SgSiTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tid_pk", nullable = false)
    private Long tidPk;

    @Size(max = 45)
    @Column(name = "tid_codigo", length = 45)
    @AtributoCodigo
    private String tidCodigo;

    @Size(max = 255)
    @Column(name = "tid_nombre", length = 255)
    @AtributoNormalizable
    private String tidNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tid_nombre_busqueda", length = 255)
    private String tidNombreBusqueda;

    @Column(name = "tid_habilitado")
    @AtributoHabilitado
    private Boolean tidHabilitado;

    @Column(name = "tid_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tidUltModFecha;

    @Size(max = 45)
    @Column(name = "tid_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tidUltModUsuario;

    @Column(name = "tid_version")
    @Version
    private Integer tidVersion;

    public SgSiTipoDocumento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tidNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tidNombre);
    }

    public Long getTidPk() {
        return tidPk;
    }

    public void setTidPk(Long tidPk) {
        this.tidPk = tidPk;
    }

    public String getTidCodigo() {
        return tidCodigo;
    }

    public void setTidCodigo(String tidCodigo) {
        this.tidCodigo = tidCodigo;
    }

    public String getTidNombre() {
        return tidNombre;
    }

    public void setTidNombre(String tidNombre) {
        this.tidNombre = tidNombre;
    }

    public String getTidNombreBusqueda() {
        return tidNombreBusqueda;
    }

    public void setTidNombreBusqueda(String tidNombreBusqueda) {
        this.tidNombreBusqueda = tidNombreBusqueda;
    }

    public Boolean getTidHabilitado() {
        return tidHabilitado;
    }

    public void setTidHabilitado(Boolean tidHabilitado) {
        this.tidHabilitado = tidHabilitado;
    }

    public LocalDateTime getTidUltModFecha() {
        return tidUltModFecha;
    }

    public void setTidUltModFecha(LocalDateTime tidUltModFecha) {
        this.tidUltModFecha = tidUltModFecha;
    }

    public String getTidUltModUsuario() {
        return tidUltModUsuario;
    }

    public void setTidUltModUsuario(String tidUltModUsuario) {
        this.tidUltModUsuario = tidUltModUsuario;
    }

    public Integer getTidVersion() {
        return tidVersion;
    }

    public void setTidVersion(Integer tidVersion) {
        this.tidVersion = tidVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tidPk);
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
        final SgSiTipoDocumento other = (SgSiTipoDocumento) obj;
        if (!Objects.equals(this.tidPk, other.tidPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSiTipoDocumento{" + "tiiPk=" + tidPk + ", tidCodigo=" + tidCodigo + ", tidNombre=" + tidNombre + ", tidNombreBusqueda=" + tidNombreBusqueda + ", tidHabilitado=" + tidHabilitado + ", tidUltModFecha=" + tidUltModFecha + ", tidUltModUsuario=" + tidUltModUsuario + ", tidVersion=" + tidVersion + '}';
    }
}


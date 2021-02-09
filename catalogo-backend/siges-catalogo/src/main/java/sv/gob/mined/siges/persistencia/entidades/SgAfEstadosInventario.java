/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_estados_inventario", uniqueConstraints = {
    @UniqueConstraint(name = "ein_codigo_uk", columnNames = {"ein_codigo"})
    ,
    @UniqueConstraint(name = "ein_nombre_uk", columnNames = {"ein_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "einPk", scope = SgAfEstadosInventario.class)
public class SgAfEstadosInventario  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ein_pk", nullable = false)
    private Long einPk;

    @Size(max = 4)
    @Column(name = "ein_codigo", length = 4)
    @AtributoCodigo
    private String einCodigo;

    @Size(max = 255)
    @Column(name = "ein_nombre", length = 255)
    @AtributoNormalizable
    private String einNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ein_nombre_busqueda", length = 255)
    private String einNombreBusqueda;

    @Column(name = "ein_habilitado")
    @AtributoHabilitado
    private Boolean einHabilitado;

    @Column(name = "ein_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime einUltModFecha;

    @Size(max = 45)
    @Column(name = "ein_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String einUltModUsuario;

    @Column(name = "ein_version")
    @Version
    private Integer einVersion;

    public SgAfEstadosInventario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.einNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.einNombre);
    }

    public Long getEinPk() {
        return einPk;
    }

    public void setEinPk(Long einPk) {
        this.einPk = einPk;
    }

    public String getEinCodigo() {
        return einCodigo;
    }

    public void setEinCodigo(String einCodigo) {
        this.einCodigo = einCodigo;
    }

    public String getEinNombre() {
        return einNombre;
    }

    public void setEinNombre(String einNombre) {
        this.einNombre = einNombre;
    }

    public String getEinNombreBusqueda() {
        return einNombreBusqueda;
    }

    public void setEinNombreBusqueda(String einNombreBusqueda) {
        this.einNombreBusqueda = einNombreBusqueda;
    }

    public Boolean getEinHabilitado() {
        return einHabilitado;
    }

    public void setEinHabilitado(Boolean einHabilitado) {
        this.einHabilitado = einHabilitado;
    }

    public LocalDateTime getEinUltModFecha() {
        return einUltModFecha;
    }

    public void setEinUltModFecha(LocalDateTime einUltModFecha) {
        this.einUltModFecha = einUltModFecha;
    }

    public String getEinUltModUsuario() {
        return einUltModUsuario;
    }

    public void setEinUltModUsuario(String einUltModUsuario) {
        this.einUltModUsuario = einUltModUsuario;
    }

    public Integer getEinVersion() {
        return einVersion;
    }

    public void setEinVersion(Integer einVersion) {
        this.einVersion = einVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.einPk);
        hash = 67 * hash + Objects.hashCode(this.einCodigo);
        hash = 67 * hash + Objects.hashCode(this.einNombre);
        hash = 67 * hash + Objects.hashCode(this.einNombreBusqueda);
        hash = 67 * hash + Objects.hashCode(this.einHabilitado);
        hash = 67 * hash + Objects.hashCode(this.einUltModFecha);
        hash = 67 * hash + Objects.hashCode(this.einUltModUsuario);
        hash = 67 * hash + Objects.hashCode(this.einVersion);
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
        final SgAfEstadosInventario other = (SgAfEstadosInventario) obj;
        if (!Objects.equals(this.einPk, other.einPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosInventario[ einPk=" + einPk + " ]";
    }

}


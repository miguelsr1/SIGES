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
@Table(name = "sg_tipos_telefono", uniqueConstraints = {
    @UniqueConstraint(name = "tto_codigo_uk", columnNames = {"tto_codigo"})
    ,
    @UniqueConstraint(name = "tto_nombre_uk", columnNames = {"tto_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ttoPk", scope = SgTipoTelefono.class)
public class SgTipoTelefono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tto_pk", nullable = false)
    private Long ttoPk;

    @Size(max = 4)
    @Column(name = "tto_codigo", length = 4)
    @AtributoCodigo
    private String ttoCodigo;

    @Size(max = 255)
    @Column(name = "tto_nombre", length = 255)
    @AtributoNormalizable
    private String ttoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tto_nombre_busqueda", length = 255)
    private String ttoNombreBusqueda;

    @Column(name = "tto_habilitado")
    @AtributoHabilitado
    private Boolean ttoHabilitado;

    @Column(name = "tto_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ttoUltModFecha;

    @Size(max = 45)
    @Column(name = "tto_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ttoUltModUsuario;

    @Column(name = "tto_version")
    @Version
    private Integer ttoVersion;

    public SgTipoTelefono() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ttoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ttoNombre);
    }

    public Long getTtoPk() {
        return ttoPk;
    }

    public void setTtoPk(Long ttoPk) {
        this.ttoPk = ttoPk;
    }

    public String getTtoCodigo() {
        return ttoCodigo;
    }

    public void setTtoCodigo(String ttoCodigo) {
        this.ttoCodigo = ttoCodigo;
    }

    public String getTtoNombre() {
        return ttoNombre;
    }

    public void setTtoNombre(String ttoNombre) {
        this.ttoNombre = ttoNombre;
    }

    public String getTtoNombreBusqueda() {
        return ttoNombreBusqueda;
    }

    public void setTtoNombreBusqueda(String ttoNombreBusqueda) {
        this.ttoNombreBusqueda = ttoNombreBusqueda;
    }

    public Boolean getTtoHabilitado() {
        return ttoHabilitado;
    }

    public void setTtoHabilitado(Boolean ttoHabilitado) {
        this.ttoHabilitado = ttoHabilitado;
    }

    public LocalDateTime getTtoUltModFecha() {
        return ttoUltModFecha;
    }

    public void setTtoUltModFecha(LocalDateTime ttoUltModFecha) {
        this.ttoUltModFecha = ttoUltModFecha;
    }

    public String getTtoUltModUsuario() {
        return ttoUltModUsuario;
    }

    public void setTtoUltModUsuario(String ttoUltModUsuario) {
        this.ttoUltModUsuario = ttoUltModUsuario;
    }

    public Integer getTtoVersion() {
        return ttoVersion;
    }

    public void setTtoVersion(Integer ttoVersion) {
        this.ttoVersion = ttoVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.ttoPk);
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
        final SgTipoTelefono other = (SgTipoTelefono) obj;
        if (!Objects.equals(this.ttoPk, other.ttoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTipoTelefono[ ttoPk=" + ttoPk + " ]";
    }

}

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
@Table(name = "sg_tipos_identificacion", uniqueConstraints = {
    @UniqueConstraint(name = "tin_codigo_uk", columnNames = {"tin_codigo"})
    ,
    @UniqueConstraint(name = "tin_nombre_uk", columnNames = {"tin_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tinPk", scope = SgTipoIdentificacion.class)
public class SgTipoIdentificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tin_pk", nullable = false)
    private Long tinPk;

    @Size(max = 4)
    @Column(name = "tin_codigo", length = 4)
    @AtributoCodigo
    private String tinCodigo;

    @Size(max = 255)
    @Column(name = "tin_nombre", length = 255)
    @AtributoNormalizable
    private String tinNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tin_nombre_busqueda", length = 255)
    private String tinNombreBusqueda;

    @Column(name = "tin_habilitado")
    @AtributoHabilitado
    private Boolean tinHabilitado;

    @Column(name = "tin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tinUltModFecha;

    @Size(max = 45)
    @Column(name = "tin_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tinUltModUsuario;

    @Column(name = "tin_version")
    @Version
    private Integer tinVersion;

    public SgTipoIdentificacion() {
    }

    public SgTipoIdentificacion(Long tinPk, Integer tinVersion) {
        this.tinPk = tinPk;
        this.tinVersion = tinVersion;
    }
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tinNombre);
    }

    public Long getTinPk() {
        return tinPk;
    }

    public void setTinPk(Long tinPk) {
        this.tinPk = tinPk;
    }

    public String getTinCodigo() {
        return tinCodigo;
    }

    public void setTinCodigo(String tinCodigo) {
        this.tinCodigo = tinCodigo;
    }

    public String getTinNombre() {
        return tinNombre;
    }

    public void setTinNombre(String tinNombre) {
        this.tinNombre = tinNombre;
    }

    public String getTinNombreBusqueda() {
        return tinNombreBusqueda;
    }

    public void setTinNombreBusqueda(String tinNombreBusqueda) {
        this.tinNombreBusqueda = tinNombreBusqueda;
    }

    public Boolean getTinHabilitado() {
        return tinHabilitado;
    }

    public void setTinHabilitado(Boolean tinHabilitado) {
        this.tinHabilitado = tinHabilitado;
    }

    public LocalDateTime getTinUltModFecha() {
        return tinUltModFecha;
    }

    public void setTinUltModFecha(LocalDateTime tinUltModFecha) {
        this.tinUltModFecha = tinUltModFecha;
    }

    public String getTinUltModUsuario() {
        return tinUltModUsuario;
    }

    public void setTinUltModUsuario(String tinUltModUsuario) {
        this.tinUltModUsuario = tinUltModUsuario;
    }

    public Integer getTinVersion() {
        return tinVersion;
    }

    public void setTinVersion(Integer tinVersion) {
        this.tinVersion = tinVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.tinPk);
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
        final SgTipoIdentificacion other = (SgTipoIdentificacion) obj;
        if (!Objects.equals(this.tinPk, other.tinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTipoIdentificacion[ tinPk=" + tinPk + " ]";
    }

}

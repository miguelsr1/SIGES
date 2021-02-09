/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
@Table(name = "sg_inf_tipo_imagen", uniqueConstraints = {
    @UniqueConstraint(name = "tii_codigo_uk", columnNames = {"tii_codigo"})
    ,
    @UniqueConstraint(name = "tii_nombre_uk", columnNames = {"tii_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tiiPk", scope = SgInfTipoImagen.class)
@Audited
public class SgInfTipoImagen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tii_pk", nullable = false)
    private Long tiiPk;

    @Size(max = 45)
    @Column(name = "tii_codigo", length = 45)
    @AtributoCodigo
    private String tiiCodigo;

    @Size(max = 255)
    @Column(name = "tii_nombre", length = 255)
    @AtributoNormalizable
    private String tiiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tii_nombre_busqueda", length = 255)
    private String tiiNombreBusqueda;

    @Column(name = "tii_habilitado")
    @AtributoHabilitado
    private Boolean tiiHabilitado;

    @Column(name = "tii_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tiiUltModFecha;

    @Size(max = 45)
    @Column(name = "tii_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tiiUltModUsuario;

    @Column(name = "tii_version")
    @Version
    private Integer tiiVersion;

    public SgInfTipoImagen() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tiiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tiiNombre);
    }

    public Long getTiiPk() {
        return tiiPk;
    }

    public void setTiiPk(Long tiiPk) {
        this.tiiPk = tiiPk;
    }

    public String getTiiCodigo() {
        return tiiCodigo;
    }

    public void setTiiCodigo(String tiiCodigo) {
        this.tiiCodigo = tiiCodigo;
    }

    public String getTiiNombre() {
        return tiiNombre;
    }

    public void setTiiNombre(String tiiNombre) {
        this.tiiNombre = tiiNombre;
    }

    public String getTiiNombreBusqueda() {
        return tiiNombreBusqueda;
    }

    public void setTiiNombreBusqueda(String tiiNombreBusqueda) {
        this.tiiNombreBusqueda = tiiNombreBusqueda;
    }

    public Boolean getTiiHabilitado() {
        return tiiHabilitado;
    }

    public void setTiiHabilitado(Boolean tiiHabilitado) {
        this.tiiHabilitado = tiiHabilitado;
    }

    public LocalDateTime getTiiUltModFecha() {
        return tiiUltModFecha;
    }

    public void setTiiUltModFecha(LocalDateTime tiiUltModFecha) {
        this.tiiUltModFecha = tiiUltModFecha;
    }

    public String getTiiUltModUsuario() {
        return tiiUltModUsuario;
    }

    public void setTiiUltModUsuario(String tiiUltModUsuario) {
        this.tiiUltModUsuario = tiiUltModUsuario;
    }

    public Integer getTiiVersion() {
        return tiiVersion;
    }

    public void setTiiVersion(Integer tiiVersion) {
        this.tiiVersion = tiiVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tiiPk);
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
        final SgInfTipoImagen other = (SgInfTipoImagen) obj;
        if (!Objects.equals(this.tiiPk, other.tiiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTipoImagen{" + "tiiPk=" + tiiPk + ", tiiCodigo=" + tiiCodigo + ", tiiNombre=" + tiiNombre + ", tiiNombreBusqueda=" + tiiNombreBusqueda + ", tiiHabilitado=" + tiiHabilitado + ", tiiUltModFecha=" + tiiUltModFecha + ", tiiUltModUsuario=" + tiiUltModUsuario + ", tiiVersion=" + tiiVersion + '}';
    }
    
    

}

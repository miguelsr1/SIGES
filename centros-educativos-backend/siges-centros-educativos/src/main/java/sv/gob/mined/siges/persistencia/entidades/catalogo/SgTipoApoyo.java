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
@Table(name = "sg_tipos_apoyo", uniqueConstraints = {
    @UniqueConstraint(name = "tap_codigo_uk", columnNames = {"tap_codigo"})
    ,
    @UniqueConstraint(name = "tap_nombre_uk", columnNames = {"tap_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tapPk", scope = SgTipoApoyo.class)
@Audited
public class SgTipoApoyo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tap_pk", nullable = false)
    private Long tapPk;

    @Size(max = 45)
    @Column(name = "tap_codigo", length = 45)
    @AtributoCodigo
    private String tapCodigo;

    @Size(max = 255)
    @Column(name = "tap_nombre", length = 255)
    @AtributoNormalizable
    private String tapNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tap_nombre_busqueda", length = 255)
    private String tapNombreBusqueda;

    @Column(name = "tap_habilitado")
    @AtributoHabilitado
    private Boolean tapHabilitado;

    @Column(name = "tap_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tapUltModFecha;

    @Size(max = 45)
    @Column(name = "tap_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tapUltModUsuario;

    @Column(name = "tap_version")
    @Version
    private Integer tapVersion;

    public SgTipoApoyo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tapNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tapNombre);
    }

    public Long getTapPk() {
        return tapPk;
    }

    public void setTapPk(Long tapPk) {
        this.tapPk = tapPk;
    }

    public String getTapCodigo() {
        return tapCodigo;
    }

    public void setTapCodigo(String tapCodigo) {
        this.tapCodigo = tapCodigo;
    }

    public String getTapNombre() {
        return tapNombre;
    }

    public void setTapNombre(String tapNombre) {
        this.tapNombre = tapNombre;
    }

    public String getTapNombreBusqueda() {
        return tapNombreBusqueda;
    }

    public void setTapNombreBusqueda(String tapNombreBusqueda) {
        this.tapNombreBusqueda = tapNombreBusqueda;
    }

    public Boolean getTapHabilitado() {
        return tapHabilitado;
    }

    public void setTapHabilitado(Boolean tapHabilitado) {
        this.tapHabilitado = tapHabilitado;
    }

    public LocalDateTime getTapUltModFecha() {
        return tapUltModFecha;
    }

    public void setTapUltModFecha(LocalDateTime tapUltModFecha) {
        this.tapUltModFecha = tapUltModFecha;
    }

    public String getTapUltModUsuario() {
        return tapUltModUsuario;
    }

    public void setTapUltModUsuario(String tapUltModUsuario) {
        this.tapUltModUsuario = tapUltModUsuario;
    }

    public Integer getTapVersion() {
        return tapVersion;
    }

    public void setTapVersion(Integer tapVersion) {
        this.tapVersion = tapVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tapPk);
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
        final SgTipoApoyo other = (SgTipoApoyo) obj;
        if (!Objects.equals(this.tapPk, other.tapPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoApoyo{" + "tapPk=" + tapPk + ", tapCodigo=" + tapCodigo + ", tapNombre=" + tapNombre + ", tapNombreBusqueda=" + tapNombreBusqueda + ", tapHabilitado=" + tapHabilitado + ", tapUltModFecha=" + tapUltModFecha + ", tapUltModUsuario=" + tapUltModUsuario + ", tapVersion=" + tapVersion + '}';
    }
    
    

}

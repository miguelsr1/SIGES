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
@Table(name = "sg_programas_institucional", uniqueConstraints = {
    @UniqueConstraint(name = "pin_codigo_uk", columnNames = {"pin_codigo"})
    ,
    @UniqueConstraint(name = "pin_nombre_uk", columnNames = {"pin_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "pinPk", scope = SgProgramaInstitucional.class)
@Audited
public class SgProgramaInstitucional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pin_pk", nullable = false)
    private Long pinPk;

    @Size(max = 45)
    @Column(name = "pin_codigo", length = 45)
    @AtributoCodigo
    private String pinCodigo;

    @Size(max = 255)
    @Column(name = "pin_nombre", length = 255)
    @AtributoNormalizable
    private String pinNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pin_nombre_busqueda", length = 255)
    private String pinNombreBusqueda;

    @Column(name = "pin_habilitado")
    @AtributoHabilitado
    private Boolean pinHabilitado;

    @Column(name = "pin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pinUltModFecha;

    @Size(max = 45)
    @Column(name = "pin_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pinUltModUsuario;

    @Column(name = "pin_version")
    @Version
    private Integer pinVersion;

    public SgProgramaInstitucional() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pinNombre);
    }

    public Long getPinPk() {
        return pinPk;
    }

    public void setPinPk(Long pinPk) {
        this.pinPk = pinPk;
    }

    public String getPinCodigo() {
        return pinCodigo;
    }

    public void setPinCodigo(String pinCodigo) {
        this.pinCodigo = pinCodigo;
    }

    public String getPinNombre() {
        return pinNombre;
    }

    public void setPinNombre(String pinNombre) {
        this.pinNombre = pinNombre;
    }

    public String getPinNombreBusqueda() {
        return pinNombreBusqueda;
    }

    public void setPinNombreBusqueda(String pinNombreBusqueda) {
        this.pinNombreBusqueda = pinNombreBusqueda;
    }

    public Boolean getPinHabilitado() {
        return pinHabilitado;
    }

    public void setPinHabilitado(Boolean pinHabilitado) {
        this.pinHabilitado = pinHabilitado;
    }

    public LocalDateTime getPinUltModFecha() {
        return pinUltModFecha;
    }

    public void setPinUltModFecha(LocalDateTime pinUltModFecha) {
        this.pinUltModFecha = pinUltModFecha;
    }

    public String getPinUltModUsuario() {
        return pinUltModUsuario;
    }

    public void setPinUltModUsuario(String pinUltModUsuario) {
        this.pinUltModUsuario = pinUltModUsuario;
    }

    public Integer getPinVersion() {
        return pinVersion;
    }

    public void setPinVersion(Integer pinVersion) {
        this.pinVersion = pinVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pinPk);
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
        final SgProgramaInstitucional other = (SgProgramaInstitucional) obj;
        if (!Objects.equals(this.pinPk, other.pinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgProgramaInstitucional{" + "pinPk=" + pinPk + ", pinCodigo=" + pinCodigo + ", pinNombre=" + pinNombre + ", pinNombreBusqueda=" + pinNombreBusqueda + ", pinHabilitado=" + pinHabilitado + ", pinUltModFecha=" + pinUltModFecha + ", pinUltModUsuario=" + pinUltModUsuario + ", pinVersion=" + pinVersion + '}';
    }
    
    

}

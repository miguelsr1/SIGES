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
@Table(name = "sg_estados_civil", uniqueConstraints = {
    @UniqueConstraint(name = "eci_codigo_uk", columnNames = {"eci_codigo"})
    ,
    @UniqueConstraint(name = "eci_nombre_uk", columnNames = {"eci_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "eciPk", scope = SgEstadoCivil.class)
public class SgEstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eci_pk", nullable = false)
    private Long eciPk;

    @Size(max = 4)
    @Column(name = "eci_codigo", length = 4)
    @AtributoCodigo
    private String eciCodigo;

    @Size(max = 255)
    @Column(name = "eci_nombre", length = 255)
    @AtributoNormalizable
    private String eciNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "eci_nombre_busqueda", length = 255)
    private String eciNombreBusqueda;

    @Column(name = "eci_habilitado")
    @AtributoHabilitado
    private Boolean eciHabilitado;

    @Column(name = "eci_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eciUltModFecha;

    @Size(max = 45)
    @Column(name = "eci_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eciUltModUsuario;

    @Column(name = "eci_version")
    @Version
    private Integer eciVersion;

    public SgEstadoCivil() {
    }

    public SgEstadoCivil(Long eciPk, Integer eciVersion) {
        this.eciPk = eciPk;
        this.eciVersion = eciVersion;
    }
    
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.eciNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.eciNombre);
    }

    public Long getEciPk() {
        return eciPk;
    }

    public void setEciPk(Long eciPk) {
        this.eciPk = eciPk;
    }

    public String getEciCodigo() {
        return eciCodigo;
    }

    public void setEciCodigo(String eciCodigo) {
        this.eciCodigo = eciCodigo;
    }

    public String getEciNombre() {
        return eciNombre;
    }

    public void setEciNombre(String eciNombre) {
        this.eciNombre = eciNombre;
    }

    public String getEciNombreBusqueda() {
        return eciNombreBusqueda;
    }

    public void setEciNombreBusqueda(String eciNombreBusqueda) {
        this.eciNombreBusqueda = eciNombreBusqueda;
    }

    public Boolean getEciHabilitado() {
        return eciHabilitado;
    }

    public void setEciHabilitado(Boolean eciHabilitado) {
        this.eciHabilitado = eciHabilitado;
    }

    public LocalDateTime getEciUltModFecha() {
        return eciUltModFecha;
    }

    public void setEciUltModFecha(LocalDateTime eciUltModFecha) {
        this.eciUltModFecha = eciUltModFecha;
    }

    public String getEciUltModUsuario() {
        return eciUltModUsuario;
    }

    public void setEciUltModUsuario(String eciUltModUsuario) {
        this.eciUltModUsuario = eciUltModUsuario;
    }

    public Integer getEciVersion() {
        return eciVersion;
    }

    public void setEciVersion(Integer eciVersion) {
        this.eciVersion = eciVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.eciPk);
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
        final SgEstadoCivil other = (SgEstadoCivil) obj;
        if (!Objects.equals(this.eciPk, other.eciPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesEstadoCivil[ eciPk=" + eciPk + " ]";
    }

}

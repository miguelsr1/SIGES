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
@Table(name = "sg_motivos_fallecimiento", uniqueConstraints = {
    @UniqueConstraint(name = "mfa_codigo_uk", columnNames = {"mfa_codigo"})
    ,
    @UniqueConstraint(name = "mfa_nombre_uk", columnNames = {"mfa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mfaPk", scope = SgMotivoFallecimiento.class)
@Audited
public class SgMotivoFallecimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mfa_pk", nullable = false)
    private Long mfaPk;

    @Size(max = 45)
    @Column(name = "mfa_codigo", length = 45)
    @AtributoCodigo
    private String mfaCodigo;

    @Size(max = 255)
    @Column(name = "mfa_nombre", length = 255)
    @AtributoNormalizable
    private String mfaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mfa_nombre_busqueda", length = 255)
    private String mfaNombreBusqueda;

    @Column(name = "mfa_habilitado")
    @AtributoHabilitado
    private Boolean mfaHabilitado;

    @Column(name = "mfa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mfaUltModFecha;

    @Size(max = 45)
    @Column(name = "mfa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mfaUltModUsuario;

    @Column(name = "mfa_version")
    @Version
    private Integer mfaVersion;

    public SgMotivoFallecimiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mfaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mfaNombre);
    }

    public Long getMfaPk() {
        return mfaPk;
    }

    public void setMfaPk(Long mfaPk) {
        this.mfaPk = mfaPk;
    }

    public String getMfaCodigo() {
        return mfaCodigo;
    }

    public void setMfaCodigo(String mfaCodigo) {
        this.mfaCodigo = mfaCodigo;
    }

    public String getMfaNombre() {
        return mfaNombre;
    }

    public void setMfaNombre(String mfaNombre) {
        this.mfaNombre = mfaNombre;
    }

    public String getMfaNombreBusqueda() {
        return mfaNombreBusqueda;
    }

    public void setMfaNombreBusqueda(String mfaNombreBusqueda) {
        this.mfaNombreBusqueda = mfaNombreBusqueda;
    }

    public Boolean getMfaHabilitado() {
        return mfaHabilitado;
    }

    public void setMfaHabilitado(Boolean mfaHabilitado) {
        this.mfaHabilitado = mfaHabilitado;
    }

    public LocalDateTime getMfaUltModFecha() {
        return mfaUltModFecha;
    }

    public void setMfaUltModFecha(LocalDateTime mfaUltModFecha) {
        this.mfaUltModFecha = mfaUltModFecha;
    }

    public String getMfaUltModUsuario() {
        return mfaUltModUsuario;
    }

    public void setMfaUltModUsuario(String mfaUltModUsuario) {
        this.mfaUltModUsuario = mfaUltModUsuario;
    }

    public Integer getMfaVersion() {
        return mfaVersion;
    }

    public void setMfaVersion(Integer mfaVersion) {
        this.mfaVersion = mfaVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mfaPk);
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
        final SgMotivoFallecimiento other = (SgMotivoFallecimiento) obj;
        if (!Objects.equals(this.mfaPk, other.mfaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivoFallecimiento{" + "mfaPk=" + mfaPk + ", mfaCodigo=" + mfaCodigo + ", mfaNombre=" + mfaNombre + ", mfaNombreBusqueda=" + mfaNombreBusqueda + ", mfaHabilitado=" + mfaHabilitado + ", mfaUltModFecha=" + mfaUltModFecha + ", mfaUltModUsuario=" + mfaUltModUsuario + ", mfaVersion=" + mfaVersion + '}';
    }
    
    

}

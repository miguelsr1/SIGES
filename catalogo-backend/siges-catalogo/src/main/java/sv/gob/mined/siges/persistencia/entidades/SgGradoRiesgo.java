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
@Table(name = "sg_grados_riesgo", uniqueConstraints = {
    @UniqueConstraint(name = "gre_codigo_uk", columnNames = {"gre_codigo"})
    ,
    @UniqueConstraint(name = "gre_nombre_uk", columnNames = {"gre_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "grePk", scope = SgGradoRiesgo.class)
@Audited
public class SgGradoRiesgo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gre_pk", nullable = false)
    private Long grePk;

    @Size(max = 45)
    @Column(name = "gre_codigo", length = 45)
    @AtributoCodigo
    private String greCodigo;

    @Size(max = 255)
    @Column(name = "gre_nombre", length = 255)
    @AtributoNormalizable
    private String greNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "gre_nombre_busqueda", length = 255)
    private String greNombreBusqueda;

    @Column(name = "gre_habilitado")
    @AtributoHabilitado
    private Boolean greHabilitado;

    @Column(name = "gre_riesgo_ambiental")
    private Boolean greRiesgoAmbiental;

    @Column(name = "gre_riesgo_social")
    private Boolean greRiesgoSocial;

    @Column(name = "gre_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime greUltModFecha;

    @Size(max = 45)
    @Column(name = "gre_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String greUltModUsuario;

    @Column(name = "gre_version")
    @Version
    private Integer greVersion;

    public SgGradoRiesgo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.greNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.greNombre);
    }

    public Long getGrePk() {
        return grePk;
    }

    public void setGrePk(Long grePk) {
        this.grePk = grePk;
    }

    public String getGreCodigo() {
        return greCodigo;
    }

    public void setGreCodigo(String greCodigo) {
        this.greCodigo = greCodigo;
    }

    public String getGreNombre() {
        return greNombre;
    }

    public void setGreNombre(String greNombre) {
        this.greNombre = greNombre;
    }

    public String getGreNombreBusqueda() {
        return greNombreBusqueda;
    }

    public void setGreNombreBusqueda(String greNombreBusqueda) {
        this.greNombreBusqueda = greNombreBusqueda;
    }

    public Boolean getGreHabilitado() {
        return greHabilitado;
    }

    public void setGreHabilitado(Boolean greHabilitado) {
        this.greHabilitado = greHabilitado;
    }

    public Boolean getGreRiesgoAmbiental() {
        return greRiesgoAmbiental;
    }

    public void setGreRiesgoAmbiental(Boolean greRiesgoAmbiental) {
        this.greRiesgoAmbiental = greRiesgoAmbiental;
    }

    public Boolean getGreRiesgoSocial() {
        return greRiesgoSocial;
    }

    public void setGreRiesgoSocial(Boolean greRiesgoSocial) {
        this.greRiesgoSocial = greRiesgoSocial;
    }

    public LocalDateTime getGreUltModFecha() {
        return greUltModFecha;
    }

    public void setGreUltModFecha(LocalDateTime greUltModFecha) {
        this.greUltModFecha = greUltModFecha;
    }

    public String getGreUltModUsuario() {
        return greUltModUsuario;
    }

    public void setGreUltModUsuario(String greUltModUsuario) {
        this.greUltModUsuario = greUltModUsuario;
    }

    public Integer getGreVersion() {
        return greVersion;
    }

    public void setGreVersion(Integer greVersion) {
        this.greVersion = greVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.grePk);
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
        final SgGradoRiesgo other = (SgGradoRiesgo) obj;
        if (!Objects.equals(this.grePk, other.grePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgGradoRiesgo{" + "grePk=" + grePk + ", greCodigo=" + greCodigo + ", greNombre=" + greNombre + ", greNombreBusqueda=" + greNombreBusqueda + ", greHabilitado=" + greHabilitado + ", greUltModFecha=" + greUltModFecha + ", greUltModUsuario=" + greUltModUsuario + ", greVersion=" + greVersion + '}';
    }
    
    

}

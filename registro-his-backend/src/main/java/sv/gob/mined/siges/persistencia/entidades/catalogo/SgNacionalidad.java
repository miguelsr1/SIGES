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
@Table(name = "sg_nacionalidades", uniqueConstraints = {
    @UniqueConstraint(name = "nac_codigo_uk", columnNames = {"nac_codigo"})
    ,
    @UniqueConstraint(name = "nac_nombre_uk", columnNames = {"nac_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nacPk", scope = SgNacionalidad.class)
@Audited
public class SgNacionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nac_pk", nullable = false)
    private Long nacPk;

    @Size(max = 45)
    @Column(name = "nac_codigo", length = 45)
    @AtributoCodigo
    private String nacCodigo;

    @Size(max = 255)
    @Column(name = "nac_nombre", length = 255)
    @AtributoNormalizable
    private String nacNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "nac_nombre_busqueda", length = 255)
    private String nacNombreBusqueda;

    @Column(name = "nac_habilitado")
    @AtributoHabilitado
    private Boolean nacHabilitado;

    @Column(name = "nac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nacUltModFecha;

    @Size(max = 45)
    @Column(name = "nac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String nacUltModUsuario;

    @Column(name = "nac_version")
    @Version
    private Integer nacVersion;

    public SgNacionalidad() {
    }

    public SgNacionalidad(Long nacPk, Integer nacVersion) {
        this.nacPk = nacPk;
        this.nacVersion = nacVersion;
    }
    
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nacNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nacNombre);
    }

    public Long getNacPk() {
        return nacPk;
    }

    public void setNacPk(Long nacPk) {
        this.nacPk = nacPk;
    }

    public String getNacCodigo() {
        return nacCodigo;
    }

    public void setNacCodigo(String nacCodigo) {
        this.nacCodigo = nacCodigo;
    }

    public String getNacNombre() {
        return nacNombre;
    }

    public void setNacNombre(String nacNombre) {
        this.nacNombre = nacNombre;
    }

    public String getNacNombreBusqueda() {
        return nacNombreBusqueda;
    }

    public void setNacNombreBusqueda(String nacNombreBusqueda) {
        this.nacNombreBusqueda = nacNombreBusqueda;
    }

    public Boolean getNacHabilitado() {
        return nacHabilitado;
    }

    public void setNacHabilitado(Boolean nacHabilitado) {
        this.nacHabilitado = nacHabilitado;
    }

    public LocalDateTime getNacUltModFecha() {
        return nacUltModFecha;
    }

    public void setNacUltModFecha(LocalDateTime nacUltModFecha) {
        this.nacUltModFecha = nacUltModFecha;
    }

    public String getNacUltModUsuario() {
        return nacUltModUsuario;
    }

    public void setNacUltModUsuario(String nacUltModUsuario) {
        this.nacUltModUsuario = nacUltModUsuario;
    }

    public Integer getNacVersion() {
        return nacVersion;
    }

    public void setNacVersion(Integer nacVersion) {
        this.nacVersion = nacVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.nacPk);
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
        final SgNacionalidad other = (SgNacionalidad) obj;
        if (!Objects.equals(this.nacPk, other.nacPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgNacionalidad{" + "nacPk=" + nacPk + ", nacCodigo=" + nacCodigo + ", nacNombre=" + nacNombre + ", nacNombreBusqueda=" + nacNombreBusqueda + ", nacHabilitado=" + nacHabilitado + ", nacUltModFecha=" + nacUltModFecha + ", nacUltModUsuario=" + nacUltModUsuario + ", nacVersion=" + nacVersion + '}';
    }
    
    

}

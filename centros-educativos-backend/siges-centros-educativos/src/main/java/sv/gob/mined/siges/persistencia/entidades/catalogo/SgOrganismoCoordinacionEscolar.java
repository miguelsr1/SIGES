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
@Table(name = "sg_organismos_coordinacion_escolar", uniqueConstraints = {
    @UniqueConstraint(name = "oce_codigo_uk", columnNames = {"oce_codigo"})
    ,
    @UniqueConstraint(name = "oce_nombre_uk", columnNames = {"oce_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ocePk", scope = SgOrganismoCoordinacionEscolar.class)
@Audited
public class SgOrganismoCoordinacionEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oce_pk", nullable = false)
    private Long ocePk;

    @Size(max = 45)
    @Column(name = "oce_codigo", length = 45)
    @AtributoCodigo
    private String oceCodigo;

    @Size(max = 255)
    @Column(name = "oce_nombre", length = 255)
    @AtributoNormalizable
    private String oceNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "oce_nombre_busqueda", length = 255)
    private String oceNombreBusqueda;

    @Column(name = "oce_habilitado")
    @AtributoHabilitado
    private Boolean oceHabilitado;

    @Column(name = "oce_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime oceUltModFecha;

    @Size(max = 45)
    @Column(name = "oce_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String oceUltModUsuario;

    @Column(name = "oce_version")
    @Version
    private Integer oceVersion;

    public SgOrganismoCoordinacionEscolar() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.oceNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.oceNombre);
    }

    public Long getOcePk() {
        return ocePk;
    }

    public void setOcePk(Long ocePk) {
        this.ocePk = ocePk;
    }

    public String getOceCodigo() {
        return oceCodigo;
    }

    public void setOceCodigo(String oceCodigo) {
        this.oceCodigo = oceCodigo;
    }

    public String getOceNombre() {
        return oceNombre;
    }

    public void setOceNombre(String oceNombre) {
        this.oceNombre = oceNombre;
    }

    public String getOceNombreBusqueda() {
        return oceNombreBusqueda;
    }

    public void setOceNombreBusqueda(String oceNombreBusqueda) {
        this.oceNombreBusqueda = oceNombreBusqueda;
    }

    public Boolean getOceHabilitado() {
        return oceHabilitado;
    }

    public void setOceHabilitado(Boolean oceHabilitado) {
        this.oceHabilitado = oceHabilitado;
    }

    public LocalDateTime getOceUltModFecha() {
        return oceUltModFecha;
    }

    public void setOceUltModFecha(LocalDateTime oceUltModFecha) {
        this.oceUltModFecha = oceUltModFecha;
    }

    public String getOceUltModUsuario() {
        return oceUltModUsuario;
    }

    public void setOceUltModUsuario(String oceUltModUsuario) {
        this.oceUltModUsuario = oceUltModUsuario;
    }

    public Integer getOceVersion() {
        return oceVersion;
    }

    public void setOceVersion(Integer oceVersion) {
        this.oceVersion = oceVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ocePk);
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
        final SgOrganismoCoordinacionEscolar other = (SgOrganismoCoordinacionEscolar) obj;
        if (!Objects.equals(this.ocePk, other.ocePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganismoCoordinacionEscolar{" + "ocePk=" + ocePk + ", oceCodigo=" + oceCodigo + ", oceNombre=" + oceNombre + ", oceNombreBusqueda=" + oceNombreBusqueda + ", oceHabilitado=" + oceHabilitado + ", oceUltModFecha=" + oceUltModFecha + ", oceUltModUsuario=" + oceUltModUsuario + ", oceVersion=" + oceVersion + '}';
    }
    
    

}

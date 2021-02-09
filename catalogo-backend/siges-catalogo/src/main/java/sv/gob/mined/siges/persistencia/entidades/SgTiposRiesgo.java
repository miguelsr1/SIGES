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
@Table(name = "sg_tipos_riesgo", uniqueConstraints = {
    @UniqueConstraint(name = "tri_codigo_uk", columnNames = {"tri_codigo"})
    ,
    @UniqueConstraint(name = "tri_nombre_uk", columnNames = {"tri_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "triPk", scope = SgTiposRiesgo.class)
@Audited
public class SgTiposRiesgo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tri_pk", nullable = false)
    private Long triPk;

    @Size(max = 45)
    @Column(name = "tri_codigo", length = 45)
    @AtributoCodigo
    private String triCodigo;

    @Size(max = 255)
    @Column(name = "tri_nombre", length = 255)
    @AtributoNormalizable
    private String triNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tri_nombre_busqueda", length = 255)
    private String triNombreBusqueda;

    @Column(name = "tri_riesgo_ambiental")
    private Boolean triRiesgoAmbiental;

    @Column(name = "tri_riesgo_social")
    private Boolean triRiesgoSocial;

    @Column(name = "tri_habilitado")
    @AtributoHabilitado
    private Boolean triHabilitado;

    @Column(name = "tri_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime triUltModFecha;

    @Size(max = 45)
    @Column(name = "tri_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String triUltModUsuario;

    @Column(name = "tri_version")
    @Version
    private Integer triVersion;

    public SgTiposRiesgo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.triNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.triNombre);
    }

    public Long getTriPk() {
        return triPk;
    }

    public void setTriPk(Long triPk) {
        this.triPk = triPk;
    }

    public String getTriCodigo() {
        return triCodigo;
    }

    public void setTriCodigo(String triCodigo) {
        this.triCodigo = triCodigo;
    }

    public String getTriNombre() {
        return triNombre;
    }

    public void setTriNombre(String triNombre) {
        this.triNombre = triNombre;
    }

    public String getTriNombreBusqueda() {
        return triNombreBusqueda;
    }

    public void setTriNombreBusqueda(String triNombreBusqueda) {
        this.triNombreBusqueda = triNombreBusqueda;
    }

    public Boolean getTriRiesgoAmbiental() {
        return triRiesgoAmbiental;
    }

    public void setTriRiesgoAmbiental(Boolean triRiesgoAmbiental) {
        this.triRiesgoAmbiental = triRiesgoAmbiental;
    }

    public Boolean getTriRiesgoSocial() {
        return triRiesgoSocial;
    }

    public void setTriRiesgoSocial(Boolean triRiesgoSocial) {
        this.triRiesgoSocial = triRiesgoSocial;
    }

    public Boolean getTriHabilitado() {
        return triHabilitado;
    }

    public void setTriHabilitado(Boolean triHabilitado) {
        this.triHabilitado = triHabilitado;
    }

    public LocalDateTime getTriUltModFecha() {
        return triUltModFecha;
    }

    public void setTriUltModFecha(LocalDateTime triUltModFecha) {
        this.triUltModFecha = triUltModFecha;
    }

    public String getTriUltModUsuario() {
        return triUltModUsuario;
    }

    public void setTriUltModUsuario(String triUltModUsuario) {
        this.triUltModUsuario = triUltModUsuario;
    }

    public Integer getTriVersion() {
        return triVersion;
    }

    public void setTriVersion(Integer triVersion) {
        this.triVersion = triVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.triPk);
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
        final SgTiposRiesgo other = (SgTiposRiesgo) obj;
        if (!Objects.equals(this.triPk, other.triPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTiposRiesgo{" + "triPk=" + triPk + ", triCodigo=" + triCodigo + ", triNombre=" + triNombre + ", triNombreBusqueda=" + triNombreBusqueda + ", triHabilitado=" + triHabilitado + ", triUltModFecha=" + triUltModFecha + ", triUltModUsuario=" + triUltModUsuario + ", triVersion=" + triVersion + '}';
    }
    
    

}

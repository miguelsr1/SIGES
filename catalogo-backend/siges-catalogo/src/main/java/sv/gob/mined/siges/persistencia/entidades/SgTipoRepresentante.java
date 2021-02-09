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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sg_tipo_representante", uniqueConstraints = {
    @UniqueConstraint(name = "tre_codigo_uk", columnNames = {"tre_codigo"})
    ,
    @UniqueConstraint(name = "tre_nombre_uk", columnNames = {"tre_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "trePk", scope = SgTipoRepresentante.class)
@Audited
public class SgTipoRepresentante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tre_pk", nullable = false)
    private Long trePk;

    @Size(max = 45)
    @Column(name = "tre_codigo", length = 45)
    @AtributoCodigo
    private String treCodigo;

    @Size(max = 255)
    @Column(name = "tre_nombre", length = 255)
    @AtributoNormalizable
    private String treNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tre_nombre_busqueda", length = 255)
    private String treNombreBusqueda;

    @Column(name = "tre_habilitado")
    @AtributoHabilitado
    private Boolean treHabilitado;
    
    @JoinColumn(name = "tre_cargo_fk")
    @ManyToOne
    private SgCargo treCargoAsociado;

    @Column(name = "tre_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime treUltModFecha;

    @Size(max = 45)
    @Column(name = "tre_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String treUltModUsuario;

    @Column(name = "tre_version")
    @Version
    private Integer treVersion;

    public SgTipoRepresentante() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.treNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.treNombre);
    }

    public Long getTrePk() {
        return trePk;
    }

    public void setTrePk(Long trePk) {
        this.trePk = trePk;
    }

    public String getTreCodigo() {
        return treCodigo;
    }

    public void setTreCodigo(String treCodigo) {
        this.treCodigo = treCodigo;
    }

    public String getTreNombre() {
        return treNombre;
    }

    public void setTreNombre(String treNombre) {
        this.treNombre = treNombre;
    }

    public String getTreNombreBusqueda() {
        return treNombreBusqueda;
    }

    public void setTreNombreBusqueda(String treNombreBusqueda) {
        this.treNombreBusqueda = treNombreBusqueda;
    }

    public Boolean getTreHabilitado() {
        return treHabilitado;
    }

    public void setTreHabilitado(Boolean treHabilitado) {
        this.treHabilitado = treHabilitado;
    }

    public LocalDateTime getTreUltModFecha() {
        return treUltModFecha;
    }

    public void setTreUltModFecha(LocalDateTime treUltModFecha) {
        this.treUltModFecha = treUltModFecha;
    }

    public String getTreUltModUsuario() {
        return treUltModUsuario;
    }

    public void setTreUltModUsuario(String treUltModUsuario) {
        this.treUltModUsuario = treUltModUsuario;
    }

    public Integer getTreVersion() {
        return treVersion;
    }

    public void setTreVersion(Integer treVersion) {
        this.treVersion = treVersion;
    }

    public SgCargo getTreCargoAsociado() {
        return treCargoAsociado;
    }

    public void setTreCargoAsociado(SgCargo treCargoAsociado) {
        this.treCargoAsociado = treCargoAsociado;
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.trePk);
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
        final SgTipoRepresentante other = (SgTipoRepresentante) obj;
        if (!Objects.equals(this.trePk, other.trePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoRepresentante{" + "trePk=" + trePk + ", treCodigo=" + treCodigo + ", treNombre=" + treNombre + ", treNombreBusqueda=" + treNombreBusqueda + ", treHabilitado=" + treHabilitado + ", treUltModFecha=" + treUltModFecha + ", treUltModUsuario=" + treUltModUsuario + ", treVersion=" + treVersion + '}';
    }
    
    

}

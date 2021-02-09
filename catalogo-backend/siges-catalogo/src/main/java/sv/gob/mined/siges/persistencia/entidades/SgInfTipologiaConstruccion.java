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
@Table(name = "sg_inf_tipologia_construccion", uniqueConstraints = {
    @UniqueConstraint(name = "tic_codigo_uk", columnNames = {"tic_codigo"})
    ,
    @UniqueConstraint(name = "tic_nombre_uk", columnNames = {"tic_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticPk", scope = SgInfTipologiaConstruccion.class)
@Audited
public class SgInfTipologiaConstruccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tic_pk", nullable = false)
    private Long ticPk;

    @Size(max = 45)
    @Column(name = "tic_codigo", length = 45)
    @AtributoCodigo
    private String ticCodigo;

    @Size(max = 255)
    @Column(name = "tic_nombre", length = 255)
    @AtributoNormalizable
    private String ticNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tic_nombre_busqueda", length = 255)
    private String ticNombreBusqueda;

    @Column(name = "tic_habilitado")
    @AtributoHabilitado
    private Boolean ticHabilitado;

    @Column(name = "tic_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ticUltModFecha;

    @Size(max = 45)
    @Column(name = "tic_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ticUltModUsuario;

    @Column(name = "tic_version")
    @Version
    private Integer ticVersion;

    public SgInfTipologiaConstruccion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ticNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ticNombre);
    }

    public Long getTicPk() {
        return ticPk;
    }

    public void setTicPk(Long ticPk) {
        this.ticPk = ticPk;
    }

    public String getTicCodigo() {
        return ticCodigo;
    }

    public void setTicCodigo(String ticCodigo) {
        this.ticCodigo = ticCodigo;
    }

    public String getTicNombre() {
        return ticNombre;
    }

    public void setTicNombre(String ticNombre) {
        this.ticNombre = ticNombre;
    }

    public String getTicNombreBusqueda() {
        return ticNombreBusqueda;
    }

    public void setTicNombreBusqueda(String ticNombreBusqueda) {
        this.ticNombreBusqueda = ticNombreBusqueda;
    }

    public Boolean getTicHabilitado() {
        return ticHabilitado;
    }

    public void setTicHabilitado(Boolean ticHabilitado) {
        this.ticHabilitado = ticHabilitado;
    }

    public LocalDateTime getTicUltModFecha() {
        return ticUltModFecha;
    }

    public void setTicUltModFecha(LocalDateTime ticUltModFecha) {
        this.ticUltModFecha = ticUltModFecha;
    }

    public String getTicUltModUsuario() {
        return ticUltModUsuario;
    }

    public void setTicUltModUsuario(String ticUltModUsuario) {
        this.ticUltModUsuario = ticUltModUsuario;
    }

    public Integer getTicVersion() {
        return ticVersion;
    }

    public void setTicVersion(Integer ticVersion) {
        this.ticVersion = ticVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ticPk);
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
        final SgInfTipologiaConstruccion other = (SgInfTipologiaConstruccion) obj;
        if (!Objects.equals(this.ticPk, other.ticPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTipologiaConstruccion{" + "ticPk=" + ticPk + ", ticCodigo=" + ticCodigo + ", ticNombre=" + ticNombre + ", ticNombreBusqueda=" + ticNombreBusqueda + ", ticHabilitado=" + ticHabilitado + ", ticUltModFecha=" + ticUltModFecha + ", ticUltModUsuario=" + ticUltModUsuario + ", ticVersion=" + ticVersion + '}';
    }
    
    

}

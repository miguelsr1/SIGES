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
@Table(name = "sg_tipos_modulos", uniqueConstraints = {
    @UniqueConstraint(name = "tmo_codigo_uk", columnNames = {"tmo_codigo"})
    ,
    @UniqueConstraint(name = "tmo_nombre_uk", columnNames = {"tmo_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmoPk", scope = SgTipoModulo.class)
@Audited
public class SgTipoModulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tmo_pk", nullable = false)
    private Long tmoPk;

    @Size(max = 45)
    @Column(name = "tmo_codigo", length = 45)
    @AtributoCodigo
    private String tmoCodigo;

    @Size(max = 255)
    @Column(name = "tmo_nombre", length = 255)
    @AtributoNormalizable
    private String tmoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tmo_nombre_busqueda", length = 255)
    private String tmoNombreBusqueda;

    @Column(name = "tmo_habilitado")
    @AtributoHabilitado
    private Boolean tmoHabilitado;

    @Column(name = "tmo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tmoUltModFecha;

    @Size(max = 45)
    @Column(name = "tmo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tmoUltModUsuario;

    @Column(name = "tmo_version")
    @Version
    private Integer tmoVersion;

    public SgTipoModulo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tmoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tmoNombre);
    }

    public Long getTmoPk() {
        return tmoPk;
    }

    public void setTmoPk(Long tmoPk) {
        this.tmoPk = tmoPk;
    }

    public String getTmoCodigo() {
        return tmoCodigo;
    }

    public void setTmoCodigo(String tmoCodigo) {
        this.tmoCodigo = tmoCodigo;
    }

    public String getTmoNombre() {
        return tmoNombre;
    }

    public void setTmoNombre(String tmoNombre) {
        this.tmoNombre = tmoNombre;
    }

    public String getTmoNombreBusqueda() {
        return tmoNombreBusqueda;
    }

    public void setTmoNombreBusqueda(String tmoNombreBusqueda) {
        this.tmoNombreBusqueda = tmoNombreBusqueda;
    }

    public Boolean getTmoHabilitado() {
        return tmoHabilitado;
    }

    public void setTmoHabilitado(Boolean tmoHabilitado) {
        this.tmoHabilitado = tmoHabilitado;
    }

    public LocalDateTime getTmoUltModFecha() {
        return tmoUltModFecha;
    }

    public void setTmoUltModFecha(LocalDateTime tmoUltModFecha) {
        this.tmoUltModFecha = tmoUltModFecha;
    }

    public String getTmoUltModUsuario() {
        return tmoUltModUsuario;
    }

    public void setTmoUltModUsuario(String tmoUltModUsuario) {
        this.tmoUltModUsuario = tmoUltModUsuario;
    }

    public Integer getTmoVersion() {
        return tmoVersion;
    }

    public void setTmoVersion(Integer tmoVersion) {
        this.tmoVersion = tmoVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tmoPk);
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
        final SgTipoModulo other = (SgTipoModulo) obj;
        if (!Objects.equals(this.tmoPk, other.tmoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoModulo{" + "tmoPk=" + tmoPk + ", tmoCodigo=" + tmoCodigo + ", tmoNombre=" + tmoNombre + ", tmoNombreBusqueda=" + tmoNombreBusqueda + ", tmoHabilitado=" + tmoHabilitado + ", tmoUltModFecha=" + tmoUltModFecha + ", tmoUltModUsuario=" + tmoUltModUsuario + ", tmoVersion=" + tmoVersion + '}';
    }
    
    

}

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
@Table(name = "sg_tipos_vacuna", uniqueConstraints = {
    @UniqueConstraint(name = "tvc_codigo_uk", columnNames = {"tvc_codigo"})
    ,
    @UniqueConstraint(name = "tvc_nombre_uk", columnNames = {"tvc_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tvcPk", scope = SgTipoVacuna.class)
public class SgTipoVacuna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tvc_pk", nullable = false)
    private Long tvcPk;

    @Size(max = 45)
    @Column(name = "tvc_codigo", length = 45)
    @AtributoCodigo
    private String tvcCodigo;

    @Size(max = 255)
    @Column(name = "tvc_nombre", length = 255)
    @AtributoNormalizable
    private String tvcNombre;

    @Size(max = 255)
    @Column(name = "tvc_nombre_busqueda", length = 255)
    @AtributoNombre
    private String tvcNombreBusqueda;

    @Column(name = "tvc_habilitado")
    @AtributoHabilitado
    private Boolean tvcHabilitado;

    @Column(name = "tvc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tvcUltModFecha;

    @Size(max = 45)
    @Column(name = "tvc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tvcUltModUsuario;

    @Column(name = "tvc_version")
    @Version
    private Integer tvcVersion;

    public SgTipoVacuna() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tvcNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tvcNombre);
    }

    public Long getTvcPk() {
        return tvcPk;
    }

    public void setTvcPk(Long tvcPk) {
        this.tvcPk = tvcPk;
    }

    public String getTvcCodigo() {
        return tvcCodigo;
    }

    public void setTvcCodigo(String tvcCodigo) {
        this.tvcCodigo = tvcCodigo;
    }

    public String getTvcNombre() {
        return tvcNombre;
    }

    public void setTvcNombre(String tvcNombre) {
        this.tvcNombre = tvcNombre;
    }

    public String getTvcNombreBusqueda() {
        return tvcNombreBusqueda;
    }

    public void setTvcNombreBusqueda(String tvcNombreBusqueda) {
        this.tvcNombreBusqueda = tvcNombreBusqueda;
    }

    public Boolean getTvcHabilitado() {
        return tvcHabilitado;
    }

    public void setTvcHabilitado(Boolean tvcHabilitado) {
        this.tvcHabilitado = tvcHabilitado;
    }

    public LocalDateTime getTvcUltModFecha() {
        return tvcUltModFecha;
    }

    public void setTvcUltModFecha(LocalDateTime tvcUltModFecha) {
        this.tvcUltModFecha = tvcUltModFecha;
    }

    public String getTvcUltModUsuario() {
        return tvcUltModUsuario;
    }

    public void setTvcUltModUsuario(String tvcUltModUsuario) {
        this.tvcUltModUsuario = tvcUltModUsuario;
    }

    public Integer getTvcVersion() {
        return tvcVersion;
    }

    public void setTvcVersion(Integer tvcVersion) {
        this.tvcVersion = tvcVersion;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tvcPk);
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
        final SgTipoVacuna other = (SgTipoVacuna) obj;
        if (!Objects.equals(this.tvcPk, other.tvcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoVacuna{" + "tvcPk=" + tvcPk + ", tvcCodigo=" + tvcCodigo + ", tvcNombre=" + tvcNombre + ", tvcNombreBusqueda=" + tvcNombreBusqueda + ", tvcHabilitado=" + tvcHabilitado + ", tvcUltModFecha=" + tvcUltModFecha + ", tvcUltModUsuario=" + tvcUltModUsuario + ", tvcVersion=" + tvcVersion + '}';
    }

}

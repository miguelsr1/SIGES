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

@Entity
@Table(name = "sg_tipos_calendario_escolar", uniqueConstraints = {
    @UniqueConstraint(name = "tce_codigo_uk", columnNames = {"tce_codigo"})
    ,
    @UniqueConstraint(name = "tce_nombre_uk", columnNames = {"tce_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tcePk", scope = SgTipoCalendarioEscolar.class)
public class SgTipoCalendarioEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tce_pk", nullable = false)
    private Long tcePk;

    @Size(max = 4)
    @Column(name = "tce_codigo", length = 4)
    @AtributoCodigo
    private String tceCodigo;

    @Size(max = 255)
    @Column(name = "tce_nombre", length = 255)
    @AtributoNormalizable
    private String tceNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tce_nombre_busqueda", length = 255)
    private String tceNombreBusqueda;

    @Column(name = "tce_habilitado")
    @AtributoHabilitado
    private Boolean tceHabilitado;

    @Column(name = "tce_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tceUltModFecha;

    @Size(max = 45)
    @Column(name = "tce_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tceUltModUsuario;

    @Column(name = "tce_version")
    @Version
    private Integer tceVersion;

    public SgTipoCalendarioEscolar() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tceNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tceNombre);
    }

    public Long getTcePk() {
        return tcePk;
    }

    public void setTcePk(Long tcePk) {
        this.tcePk = tcePk;
    }

    public String getTceCodigo() {
        return tceCodigo;
    }

    public void setTceCodigo(String tceCodigo) {
        this.tceCodigo = tceCodigo;
    }

    public String getTceNombre() {
        return tceNombre;
    }

    public void setTceNombre(String tceNombre) {
        this.tceNombre = tceNombre;
    }

    public String getTceNombreBusqueda() {
        return tceNombreBusqueda;
    }

    public void setTceNombreBusqueda(String tceNombreBusqueda) {
        this.tceNombreBusqueda = tceNombreBusqueda;
    }

    public Boolean getTceHabilitado() {
        return tceHabilitado;
    }

    public void setTceHabilitado(Boolean tceHabilitado) {
        this.tceHabilitado = tceHabilitado;
    }

    public LocalDateTime getTceUltModFecha() {
        return tceUltModFecha;
    }

    public void setTceUltModFecha(LocalDateTime tceUltModFecha) {
        this.tceUltModFecha = tceUltModFecha;
    }

    public String getTceUltModUsuario() {
        return tceUltModUsuario;
    }

    public void setTceUltModUsuario(String tceUltModUsuario) {
        this.tceUltModUsuario = tceUltModUsuario;
    }

    public Integer getTceVersion() {
        return tceVersion;
    }

    public void setTceVersion(Integer tceVersion) {
        this.tceVersion = tceVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tcePk);
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
        final SgTipoCalendarioEscolar other = (SgTipoCalendarioEscolar) obj;
        if (!Objects.equals(this.tcePk, other.tcePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesTipoCalendarioEscolar{" + "tcePk=" + tcePk + ", tceCodigo=" + tceCodigo + ", tceNombre=" + tceNombre + ", tceNombreBusqueda=" + tceNombreBusqueda + ", tceHabilitado=" + tceHabilitado + ", tceUltModFecha=" + tceUltModFecha + ", tceUltModUsuario=" + tceUltModUsuario + ", tceVersion=" + tceVersion + '}';
    }

}

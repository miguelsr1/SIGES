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
@Table(name = "sg_tipos_asociaciones", uniqueConstraints = {
    @UniqueConstraint(name = "tas_codigo_uk", columnNames = {"tas_codigo"})
    ,
    @UniqueConstraint(name = "tas_nombre_uk", columnNames = {"tas_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tasPk", scope = SgTipoAsociacion.class)
public class SgTipoAsociacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tas_pk", nullable = false)
    private Long tasPk;

    @Size(max = 4)
    @Column(name = "tas_codigo", length = 4)
    @AtributoCodigo
    private String tasCodigo;

    @Size(max = 100)
    @Column(name = "tas_nombre", length = 100)
    @AtributoNormalizable
    private String tasNombre;

    @Size(max = 100)
    @AtributoNombre
    @Column(name = "tas_nombre_busqueda", length = 100)
    private String tasNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "tas_descripcion",length = 255)
    private String tasDescripcion;

    @Column(name = "tas_habilitado")
    @AtributoHabilitado
    private Boolean tasHabilitado;

    @Column(name = "tas_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tasUltModFecha;

    @Size(max = 45)
    @Column(name = "tas_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tasUltModUsuario;

    @Column(name = "tas_version")
    @Version
    private Integer tasVersion;

    public SgTipoAsociacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tasNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tasNombre);
    }

    public Long getTasPk() {
        return tasPk;
    }

    public void setTasPk(Long tasPk) {
        this.tasPk = tasPk;
    }

    public String getTasCodigo() {
        return tasCodigo;
    }

    public void setTasCodigo(String tasCodigo) {
        this.tasCodigo = tasCodigo;
    }

    public String getTasNombre() {
        return tasNombre;
    }

    public void setTasNombre(String tasNombre) {
        this.tasNombre = tasNombre;
    }

    public String getTasNombreBusqueda() {
        return tasNombreBusqueda;
    }

    public void setTasNombreBusqueda(String tasNombreBusqueda) {
        this.tasNombreBusqueda = tasNombreBusqueda;
    }

    public String getTasDescripcion() {
        return tasDescripcion;
    }

    public void setTasDescripcion(String tasDescripcion) {
        this.tasDescripcion = tasDescripcion;
    }

    public Boolean getTasHabilitado() {
        return tasHabilitado;
    }

    public void setTasHabilitado(Boolean tasHabilitado) {
        this.tasHabilitado = tasHabilitado;
    }

    public LocalDateTime getTasUltModFecha() {
        return tasUltModFecha;
    }

    public void setTasUltModFecha(LocalDateTime tasUltModFecha) {
        this.tasUltModFecha = tasUltModFecha;
    }

    public String getTasUltModUsuario() {
        return tasUltModUsuario;
    }

    public void setTasUltModUsuario(String tasUltModUsuario) {
        this.tasUltModUsuario = tasUltModUsuario;
    }

    public Integer getTasVersion() {
        return tasVersion;
    }

    public void setTasVersion(Integer tasVersion) {
        this.tasVersion = tasVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tasPk);
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
        final SgTipoAsociacion other = (SgTipoAsociacion) obj;
        if (!Objects.equals(this.tasPk, other.tasPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesTipoAsociacion{" + "tasPk=" + tasPk + ", tasCodigo=" + tasCodigo + ", tasNombre=" + tasNombre + ", tasNombreBusqueda=" + tasNombreBusqueda + ", tasHabilitado=" + tasHabilitado + ", tasUltModFecha=" + tasUltModFecha + ", tasUltModUsuario=" + tasUltModUsuario + ", tasVersion=" + tasVersion + '}';
    }

}

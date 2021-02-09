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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "sg_tipos_manifestacion", uniqueConstraints = {
    @UniqueConstraint(name = "tma_codigo_uk", columnNames = {"tma_codigo"})
    ,
    @UniqueConstraint(name = "tma_nombre_uk", columnNames = {"tma_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tmaPk", scope = SgTipoManifestacion.class)
public class SgTipoManifestacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tma_pk", nullable = false)
    private Long tmaPk;

    @Size(max = 45)
    @Column(name = "tma_codigo", length = 45)
    @AtributoCodigo
    private String tmaCodigo;

    @Size(max = 255)
    @Column(name = "tma_nombre", length = 255)
    @AtributoNormalizable
    private String tmaNombre;

    @Size(max = 255)
    @Column(name = "tma_nombre_busqueda", length = 255)
    @AtributoNombre
    private String tmaNombreBusqueda;

    @Column(name = "tma_habilitado")
    @AtributoHabilitado
    private Boolean tmaHabilitado;

    @Column(name = "tma_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tmaUltModFecha;

    @Size(max = 45)
    @Column(name = "tma_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tmaUltModUsuario;

    @Column(name = "tma_version")
    @Version
    private Integer tmaVersion;
    
    @JoinColumn(name = "tma_categoria_violencia_fk")
    @ManyToOne
    private SgCategoriaViolencia tmaCategoriaViolencia;

    public SgTipoManifestacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tmaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tmaNombre);
    }

    public Long getTmaPk() {
        return tmaPk;
    }

    public void setTmaPk(Long tmaPk) {
        this.tmaPk = tmaPk;
    }

    public String getTmaCodigo() {
        return tmaCodigo;
    }

    public void setTmaCodigo(String tmaCodigo) {
        this.tmaCodigo = tmaCodigo;
    }

    public String getTmaNombre() {
        return tmaNombre;
    }

    public void setTmaNombre(String tmaNombre) {
        this.tmaNombre = tmaNombre;
    }

    public String getTmaNombreBusqueda() {
        return tmaNombreBusqueda;
    }

    public void setTmaNombreBusqueda(String tmaNombreBusqueda) {
        this.tmaNombreBusqueda = tmaNombreBusqueda;
    }

    public Boolean getTmaHabilitado() {
        return tmaHabilitado;
    }

    public void setTmaHabilitado(Boolean tmaHabilitado) {
        this.tmaHabilitado = tmaHabilitado;
    }

    public LocalDateTime getTmaUltModFecha() {
        return tmaUltModFecha;
    }

    public void setTmaUltModFecha(LocalDateTime tmaUltModFecha) {
        this.tmaUltModFecha = tmaUltModFecha;
    }

    public String getTmaUltModUsuario() {
        return tmaUltModUsuario;
    }

    public void setTmaUltModUsuario(String tmaUltModUsuario) {
        this.tmaUltModUsuario = tmaUltModUsuario;
    }

    public Integer getTmaVersion() {
        return tmaVersion;
    }

    public void setTmaVersion(Integer tmaVersion) {
        this.tmaVersion = tmaVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tmaPk);
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
        final SgTipoManifestacion other = (SgTipoManifestacion) obj;
        if (!Objects.equals(this.tmaPk, other.tmaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoManifestacion{" + "tmaPk=" + tmaPk + ", tmaCodigo=" + tmaCodigo + ", tmaNombre=" + tmaNombre + ", tmaNombreBusqueda=" + tmaNombreBusqueda + ", tmaHabilitado=" + tmaHabilitado + ", tmaUltModFecha=" + tmaUltModFecha + ", tmaUltModUsuario=" + tmaUltModUsuario + ", tmaVersion=" + tmaVersion + '}';
    }

    public SgCategoriaViolencia getTmaCategoriaViolencia() {
        return tmaCategoriaViolencia;
    }

    public void setTmaCategoriaViolencia(SgCategoriaViolencia tmaCategoriaViolencia) {
        this.tmaCategoriaViolencia = tmaCategoriaViolencia;
    }

}

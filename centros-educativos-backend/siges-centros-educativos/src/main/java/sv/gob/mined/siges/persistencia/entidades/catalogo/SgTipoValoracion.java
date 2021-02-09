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
@Table(name = "sg_tipos_valoracion", uniqueConstraints = {
    @UniqueConstraint(name = "tva_codigo_uk", columnNames = {"tva_codigo"})
    ,
    @UniqueConstraint(name = "tva_nombre_uk", columnNames = {"tva_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tvaPk", scope = SgTipoValoracion.class)
public class SgTipoValoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tva_pk", nullable = false)
    private Long tvaPk;

    @Size(max = 45)
    @Column(name = "tva_codigo", length = 45)
    @AtributoCodigo
    private String tvaCodigo;

    @Size(max = 255)
    @Column(name = "tva_nombre", length = 255)
    @AtributoNormalizable
    private String tvaNombre;

    @Size(max = 255)
    @Column(name = "tva_nombre_busqueda", length = 255)
    @AtributoNombre
    private String tvaNombreBusqueda;

    @Column(name = "tva_habilitado")
    @AtributoHabilitado
    private Boolean tvaHabilitado;

    @Column(name = "tva_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tvaUltModFecha;

    @Size(max = 45)
    @Column(name = "tva_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tvaUltModUsuario;

    @Column(name = "tva_version")
    @Version
    private Integer tvaVersion;

    public SgTipoValoracion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tvaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tvaNombre);
    }

    public Long getTvaPk() {
        return tvaPk;
    }

    public void setTvaPk(Long tvaPk) {
        this.tvaPk = tvaPk;
    }

    public String getTvaCodigo() {
        return tvaCodigo;
    }

    public void setTvaCodigo(String tvaCodigo) {
        this.tvaCodigo = tvaCodigo;
    }

    public String getTvaNombre() {
        return tvaNombre;
    }

    public void setTvaNombre(String tvaNombre) {
        this.tvaNombre = tvaNombre;
    }

    public String getTvaNombreBusqueda() {
        return tvaNombreBusqueda;
    }

    public void setTvaNombreBusqueda(String tvaNombreBusqueda) {
        this.tvaNombreBusqueda = tvaNombreBusqueda;
    }

    public Boolean getTvaHabilitado() {
        return tvaHabilitado;
    }

    public void setTvaHabilitado(Boolean tvaHabilitado) {
        this.tvaHabilitado = tvaHabilitado;
    }

    public LocalDateTime getTvaUltModFecha() {
        return tvaUltModFecha;
    }

    public void setTvaUltModFecha(LocalDateTime tvaUltModFecha) {
        this.tvaUltModFecha = tvaUltModFecha;
    }

    public String getTvaUltModUsuario() {
        return tvaUltModUsuario;
    }

    public void setTvaUltModUsuario(String tvaUltModUsuario) {
        this.tvaUltModUsuario = tvaUltModUsuario;
    }

    public Integer getTvaVersion() {
        return tvaVersion;
    }

    public void setTvaVersion(Integer tvaVersion) {
        this.tvaVersion = tvaVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tvaPk);
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
        final SgTipoValoracion other = (SgTipoValoracion) obj;
        if (!Objects.equals(this.tvaPk, other.tvaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoValoracion{" + "tvaPk=" + tvaPk + ", tvaCodigo=" + tvaCodigo + ", tvaNombre=" + tvaNombre + ", tvaNombreBusqueda=" + tvaNombreBusqueda + ", tvaHabilitado=" + tvaHabilitado + ", tvaUltModFecha=" + tvaUltModFecha + ", tvaUltModUsuario=" + tvaUltModUsuario + ", tvaVersion=" + tvaVersion + '}';
    }

}

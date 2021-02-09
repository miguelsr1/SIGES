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
@Table(name = "sg_servicios_infraestructura", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sinPk", scope = SgServicioInfraestructura.class)
@Audited
public class SgServicioInfraestructura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sin_pk", nullable = false)
    private Long sinPk;

    @Size(max = 45)
    @Column(name = "sin_codigo", length = 45)
    @AtributoCodigo
    private String sinCodigo;

    @Size(max = 255)
    @Column(name = "sin_nombre", length = 255)
    @AtributoNormalizable
    private String sinNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "sin_nombre_busqueda", length = 255)
    private String sinNombreBusqueda;

    @Column(name = "sin_habilitado")
    @AtributoHabilitado
    private Boolean sinHabilitado;

    @Column(name = "sin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sinUltModFecha;

    @Size(max = 45)
    @Column(name = "sin_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sinUltModUsuario;

    @Column(name = "sin_version")
    @Version
    private Integer sinVersion;

    public SgServicioInfraestructura() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.sinNombre);
    }

    public Long getSinPk() {
        return sinPk;
    }

    public void setSinPk(Long sinPk) {
        this.sinPk = sinPk;
    }

    public String getSinCodigo() {
        return sinCodigo;
    }

    public void setSinCodigo(String sinCodigo) {
        this.sinCodigo = sinCodigo;
    }

    public String getSinNombre() {
        return sinNombre;
    }

    public void setSinNombre(String sinNombre) {
        this.sinNombre = sinNombre;
    }

    public String getSinNombreBusqueda() {
        return sinNombreBusqueda;
    }

    public void setSinNombreBusqueda(String sinNombreBusqueda) {
        this.sinNombreBusqueda = sinNombreBusqueda;
    }

    public Boolean getSinHabilitado() {
        return sinHabilitado;
    }

    public void setSinHabilitado(Boolean sinHabilitado) {
        this.sinHabilitado = sinHabilitado;
    }

    public LocalDateTime getSinUltModFecha() {
        return sinUltModFecha;
    }

    public void setSinUltModFecha(LocalDateTime sinUltModFecha) {
        this.sinUltModFecha = sinUltModFecha;
    }

    public String getSinUltModUsuario() {
        return sinUltModUsuario;
    }

    public void setSinUltModUsuario(String sinUltModUsuario) {
        this.sinUltModUsuario = sinUltModUsuario;
    }

    public Integer getSinVersion() {
        return sinVersion;
    }

    public void setSinVersion(Integer sinVersion) {
        this.sinVersion = sinVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.sinPk);
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
        final SgServicioInfraestructura other = (SgServicioInfraestructura) obj;
        if (!Objects.equals(this.sinPk, other.sinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgServicioInfraestructura{" + "sinPk=" + sinPk + ", sinCodigo=" + sinCodigo + ", sinNombre=" + sinNombre + ", sinNombreBusqueda=" + sinNombreBusqueda + ", sinHabilitado=" + sinHabilitado + ", sinUltModFecha=" + sinUltModFecha + ", sinUltModUsuario=" + sinUltModUsuario + ", sinVersion=" + sinVersion + '}';
    }
    
    

}

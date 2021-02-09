/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_fomas_adquisicion", uniqueConstraints = {
    @UniqueConstraint(name = "fad_codigo_uk", columnNames = {"fad_codigo"})
    ,
    @UniqueConstraint(name = "fad_nombre_uk", columnNames = {"fad_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "fadPk", resolver = JsonIdentityResolver.class, scope = SgAfFormaAdquisicion.class)
public class SgAfFormaAdquisicion  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fad_pk", nullable = false)
    private Long fadPk;

    @Size(max = 4)
    @Column(name = "fad_codigo", length = 4)
    @AtributoCodigo
    private String fadCodigo;

    @Size(max = 255)
    @Column(name = "fad_nombre", length = 255)
    @AtributoNormalizable
    private String fadNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "fad_nombre_busqueda", length = 255)
    private String fadNombreBusqueda;

    @Column(name = "fad_habilitado")
    @AtributoHabilitado
    private Boolean fadHabilitado;

    @Column(name = "fad_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime fadUltModFecha;

    @Size(max = 45)
    @Column(name = "fad_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String fadUltModUsuario;

    @Column(name = "fad_version")
    @Version
    private Integer fadVersion;

    public SgAfFormaAdquisicion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.fadNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.fadNombre);
    }

    public Long getFadPk() {
        return fadPk;
    }

    public void setFadPk(Long fadPk) {
        this.fadPk = fadPk;
    }

    public String getFadCodigo() {
        return fadCodigo;
    }

    public void setFadCodigo(String fadCodigo) {
        this.fadCodigo = fadCodigo;
    }

    public String getFadNombre() {
        return fadNombre;
    }

    public void setFadNombre(String fadNombre) {
        this.fadNombre = fadNombre;
    }

    public String getFadNombreBusqueda() {
        return fadNombreBusqueda;
    }

    public void setFadNombreBusqueda(String fadNombreBusqueda) {
        this.fadNombreBusqueda = fadNombreBusqueda;
    }

    public Boolean getFadHabilitado() {
        return fadHabilitado;
    }

    public void setFadHabilitado(Boolean fadHabilitado) {
        this.fadHabilitado = fadHabilitado;
    }

    public LocalDateTime getFadUltModFecha() {
        return fadUltModFecha;
    }

    public void setFadUltModFecha(LocalDateTime fadUltModFecha) {
        this.fadUltModFecha = fadUltModFecha;
    }

    public String getFadUltModUsuario() {
        return fadUltModUsuario;
    }

    public void setFadUltModUsuario(String fadUltModUsuario) {
        this.fadUltModUsuario = fadUltModUsuario;
    }

    public Integer getFadVersion() {
        return fadVersion;
    }

    public void setFadVersion(Integer fadVersion) {
        this.fadVersion = fadVersion;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFormaAdquisicion[ fadPk=" + fadPk + " ]";
    }

}

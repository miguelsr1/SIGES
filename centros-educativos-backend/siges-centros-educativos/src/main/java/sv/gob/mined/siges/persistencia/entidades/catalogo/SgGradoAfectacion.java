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
@Table(name = "sg_grados_afectacion", uniqueConstraints = {
    @UniqueConstraint(name = "gaf_codigo_uk", columnNames = {"gaf_codigo"})
    ,
    @UniqueConstraint(name = "gaf_nombre_uk", columnNames = {"gaf_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "gafPk", scope = SgGradoAfectacion.class)
@Audited
public class SgGradoAfectacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gaf_pk", nullable = false)
    private Long gafPk;

    @Size(max = 45)
    @Column(name = "gaf_codigo", length = 45)
    @AtributoCodigo
    private String gafCodigo;

    @Size(max = 255)
    @Column(name = "gaf_nombre", length = 255)
    @AtributoNormalizable
    private String gafNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "gaf_nombre_busqueda", length = 255)
    private String gafNombreBusqueda;

    @Column(name = "gaf_habilitado")
    @AtributoHabilitado
    private Boolean gafHabilitado;

    @Column(name = "gaf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime gafUltModFecha;

    @Size(max = 45)
    @Column(name = "gaf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String gafUltModUsuario;

    @Column(name = "gaf_version")
    @Version
    private Integer gafVersion;

    public SgGradoAfectacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.gafNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.gafNombre);
    }

    public Long getGafPk() {
        return gafPk;
    }

    public void setGafPk(Long gafPk) {
        this.gafPk = gafPk;
    }

    public String getGafCodigo() {
        return gafCodigo;
    }

    public void setGafCodigo(String gafCodigo) {
        this.gafCodigo = gafCodigo;
    }

    public String getGafNombre() {
        return gafNombre;
    }

    public void setGafNombre(String gafNombre) {
        this.gafNombre = gafNombre;
    }

    public String getGafNombreBusqueda() {
        return gafNombreBusqueda;
    }

    public void setGafNombreBusqueda(String gafNombreBusqueda) {
        this.gafNombreBusqueda = gafNombreBusqueda;
    }

    public Boolean getGafHabilitado() {
        return gafHabilitado;
    }

    public void setGafHabilitado(Boolean gafHabilitado) {
        this.gafHabilitado = gafHabilitado;
    }

    public LocalDateTime getGafUltModFecha() {
        return gafUltModFecha;
    }

    public void setGafUltModFecha(LocalDateTime gafUltModFecha) {
        this.gafUltModFecha = gafUltModFecha;
    }

    public String getGafUltModUsuario() {
        return gafUltModUsuario;
    }

    public void setGafUltModUsuario(String gafUltModUsuario) {
        this.gafUltModUsuario = gafUltModUsuario;
    }

    public Integer getGafVersion() {
        return gafVersion;
    }

    public void setGafVersion(Integer gafVersion) {
        this.gafVersion = gafVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.gafPk);
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
        final SgGradoAfectacion other = (SgGradoAfectacion) obj;
        if (!Objects.equals(this.gafPk, other.gafPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgGradoAfectacion{" + "gafPk=" + gafPk + ", gafCodigo=" + gafCodigo + ", gafNombre=" + gafNombre + ", gafNombreBusqueda=" + gafNombreBusqueda + ", gafHabilitado=" + gafHabilitado + ", gafUltModFecha=" + gafUltModFecha + ", gafUltModUsuario=" + gafUltModUsuario + ", gafVersion=" + gafVersion + '}';
    }
    
    

}

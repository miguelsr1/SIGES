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
@Table(name = "sg_estados_datos_contratacion", uniqueConstraints = {
    @UniqueConstraint(name = "edc_codigo_uk", columnNames = {"edc_codigo"})
    ,
    @UniqueConstraint(name = "edc_nombre_uk", columnNames = {"edc_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "edcPk", scope = SgEstadoDatoContratacion.class)
@Audited
public class SgEstadoDatoContratacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "edc_pk", nullable = false)
    private Long edcPk;

    @Size(max = 45)
    @Column(name = "edc_codigo", length = 45)
    @AtributoCodigo
    private String edcCodigo;

    @Size(max = 255)
    @Column(name = "edc_nombre", length = 255)
    @AtributoNormalizable
    private String edcNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "edc_nombre_busqueda", length = 255)
    private String edcNombreBusqueda;

    @Column(name = "edc_habilitado")
    @AtributoHabilitado
    private Boolean edcHabilitado;

    @Column(name = "edc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime edcUltModFecha;

    @Size(max = 45)
    @Column(name = "edc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String edcUltModUsuario;

    @Column(name = "edc_version")
    @Version
    private Integer edcVersion;

    public SgEstadoDatoContratacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.edcNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.edcNombre);
    }

    public Long getEdcPk() {
        return edcPk;
    }

    public void setEdcPk(Long edcPk) {
        this.edcPk = edcPk;
    }

    public String getEdcCodigo() {
        return edcCodigo;
    }

    public void setEdcCodigo(String edcCodigo) {
        this.edcCodigo = edcCodigo;
    }

    public String getEdcNombre() {
        return edcNombre;
    }

    public void setEdcNombre(String edcNombre) {
        this.edcNombre = edcNombre;
    }

    public String getEdcNombreBusqueda() {
        return edcNombreBusqueda;
    }

    public void setEdcNombreBusqueda(String edcNombreBusqueda) {
        this.edcNombreBusqueda = edcNombreBusqueda;
    }

    public Boolean getEdcHabilitado() {
        return edcHabilitado;
    }

    public void setEdcHabilitado(Boolean edcHabilitado) {
        this.edcHabilitado = edcHabilitado;
    }

    public LocalDateTime getEdcUltModFecha() {
        return edcUltModFecha;
    }

    public void setEdcUltModFecha(LocalDateTime edcUltModFecha) {
        this.edcUltModFecha = edcUltModFecha;
    }

    public String getEdcUltModUsuario() {
        return edcUltModUsuario;
    }

    public void setEdcUltModUsuario(String edcUltModUsuario) {
        this.edcUltModUsuario = edcUltModUsuario;
    }

    public Integer getEdcVersion() {
        return edcVersion;
    }

    public void setEdcVersion(Integer edcVersion) {
        this.edcVersion = edcVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.edcPk);
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
        final SgEstadoDatoContratacion other = (SgEstadoDatoContratacion) obj;
        if (!Objects.equals(this.edcPk, other.edcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstadoDatoContratacion{" + "edcPk=" + edcPk + ", edcCodigo=" + edcCodigo + ", edcNombre=" + edcNombre + ", edcNombreBusqueda=" + edcNombreBusqueda + ", edcHabilitado=" + edcHabilitado + ", edcUltModFecha=" + edcUltModFecha + ", edcUltModUsuario=" + edcUltModUsuario + ", edcVersion=" + edcVersion + '}';
    }
    
    

}

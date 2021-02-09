/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_estados_descargo", uniqueConstraints = {
    @UniqueConstraint(name = "ede_codigo_uk", columnNames = {"ede_codigo"})
    ,
    @UniqueConstraint(name = "ede_nombre_uk", columnNames = {"ede_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "edePk", scope = SgAfEstadosDescargo.class)
public class SgAfEstadosDescargo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ede_pk", nullable = false)
    private Long edePk;

    @Size(max = 4)
    @Column(name = "ede_codigo", length = 4)
    @AtributoCodigo
    private String edeCodigo;

    @Size(max = 255)
    @Column(name = "ede_nombre", length = 255)
    @AtributoNormalizable
    private String edeNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ede_nombre_busqueda", length = 255)
    private String edeNombreBusqueda;

    @Column(name = "ede_habilitado")
    @AtributoHabilitado
    private Boolean edeHabilitado;

    @Column(name = "ede_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime edeUltModFecha;

    @Size(max = 45)
    @Column(name = "ede_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String edeUltModUsuario;

    @Column(name = "ede_version")
    @Version
    private Integer edeVersion;

    public SgAfEstadosDescargo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.edeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.edeNombre);
    }

    public Long getEdePk() {
        return edePk;
    }

    public void setEdePk(Long edePk) {
        this.edePk = edePk;
    }

    public String getEdeCodigo() {
        return edeCodigo;
    }

    public void setEdeCodigo(String edeCodigo) {
        this.edeCodigo = edeCodigo;
    }

    public String getEdeNombre() {
        return edeNombre;
    }

    public void setEdeNombre(String edeNombre) {
        this.edeNombre = edeNombre;
    }

    public String getEdeNombreBusqueda() {
        return edeNombreBusqueda;
    }

    public void setEdeNombreBusqueda(String edeNombreBusqueda) {
        this.edeNombreBusqueda = edeNombreBusqueda;
    }

    public Boolean getEdeHabilitado() {
        return edeHabilitado;
    }

    public void setEdeHabilitado(Boolean edeHabilitado) {
        this.edeHabilitado = edeHabilitado;
    }

    public LocalDateTime getEdeUltModFecha() {
        return edeUltModFecha;
    }

    public void setEdeUltModFecha(LocalDateTime edeUltModFecha) {
        this.edeUltModFecha = edeUltModFecha;
    }

    public String getEdeUltModUsuario() {
        return edeUltModUsuario;
    }

    public void setEdeUltModUsuario(String edeUltModUsuario) {
        this.edeUltModUsuario = edeUltModUsuario;
    }

    public Integer getEdeVersion() {
        return edeVersion;
    }

    public void setEdeVersion(Integer edeVersion) {
        this.edeVersion = edeVersion;
    }

    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosDescargo[ edePk=" + edePk + " ]";
    }

}

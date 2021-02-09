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
@Table(name = "sg_dependencias_economica", uniqueConstraints = {
    @UniqueConstraint(name = "dec_codigo_uk", columnNames = {"dec_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "decPk", scope = SgDependenciaEconomica.class)
public class SgDependenciaEconomica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dec_pk")
    private Long decPk;

    @Size(max = 4)
    @Column(name = "dec_codigo", length = 4)
    @AtributoCodigo
    private String decCodigo;

    @Size(max = 255)
    @Column(name = "dec_nombre", length = 255)
    @AtributoNormalizable
    private String decNombre;

    @Size(max = 255)
    @Column(name = "dec_nombre_busqueda", length = 255)
    @AtributoNombre
    private String decNombreBusqueda;

    @Size(max = 255)
    @Column(name = "dec_descripcion", length = 255)
    private String decDescripcion;

    @Column(name = "dec_habilitado")
    @AtributoHabilitado
    private Boolean decHabilitado;

    @Column(name = "dec_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime decUltModFecha;

    @Size(max = 45)
    @Column(name = "dec_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String decUltModUsuario;

    @Column(name = "dec_version")
    @Version
    private Integer decVersion;

    public SgDependenciaEconomica() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.decNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.decNombre);
    }

    public String getDecNombreBusqueda() {
        return decNombreBusqueda;
    }

    public void setDecNombreBusqueda(String decNombreBusqueda) {
        this.decNombreBusqueda = decNombreBusqueda;
    }

    public SgDependenciaEconomica(Long decPk) {
        this.decPk = decPk;
    }

    public Long getDecPk() {
        return decPk;
    }

    public void setDecPk(Long decPk) {
        this.decPk = decPk;
    }

    public String getDecCodigo() {
        return decCodigo;
    }

    public void setDecCodigo(String decCodigo) {
        this.decCodigo = decCodigo;
    }

    public String getDecNombre() {
        return decNombre;
    }

    public void setDecNombre(String decNombre) {
        this.decNombre = decNombre;
    }

    public String getDecDescripcion() {
        return decDescripcion;
    }

    public void setDecDescripcion(String decDescripcion) {
        this.decDescripcion = decDescripcion;
    }

    public Boolean getDecHabilitado() {
        return decHabilitado;
    }

    public void setDecHabilitado(Boolean decHabilitado) {
        this.decHabilitado = decHabilitado;
    }

    public LocalDateTime getDecUltModFecha() {
        return decUltModFecha;
    }

    public void setDecUltModFecha(LocalDateTime decUltModFecha) {
        this.decUltModFecha = decUltModFecha;
    }

    public String getDecUltModUsuario() {
        return decUltModUsuario;
    }

    public void setDecUltModUsuario(String decUltModUsuario) {
        this.decUltModUsuario = decUltModUsuario;
    }

    public Integer getDecVersion() {
        return decVersion;
    }

    public void setDecVersion(Integer decVersion) {
        this.decVersion = decVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (decPk != null ? decPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDependenciaEconomica)) {
            return false;
        }
        SgDependenciaEconomica other = (SgDependenciaEconomica) object;
        if ((this.decPk == null && other.decPk != null) || (this.decPk != null && !this.decPk.equals(other.decPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesDependenciaEconomica[ decPk=" + decPk + " ]";
    }

}

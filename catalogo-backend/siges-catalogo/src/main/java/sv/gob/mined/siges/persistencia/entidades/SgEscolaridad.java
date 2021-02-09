/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
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
@Table(name = "sg_escolaridades", uniqueConstraints = {
    @UniqueConstraint(name = "esc_codigo_uk", columnNames = {"esc_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "escPk", scope = SgEscolaridad.class)
public class SgEscolaridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esc_pk")
    private Long escPk;

    @Size(max = 6)
    @Column(name = "esc_codigo", length = 6)
    @AtributoCodigo
    private String escCodigo;

    @Size(max = 255)
    @Column(name = "esc_nombre", length = 255)
    @AtributoNormalizable
    private String escNombre;

    @Size(max = 255)
    @Column(name = "esc_nombre_busqueda", length = 255)
    @AtributoNombre
    private String escNombreBusqueda;

    @Column(name = "esc_habilitado")
    @AtributoHabilitado
    private Boolean escHabilitado;

    @Column(name = "esc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime escUltModFecha;

    @Size(max = 45)
    @Column(name = "esc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String escUltModUsuario;

    @Column(name = "esc_version")
    @Version
    private Integer escVersion;

    public SgEscolaridad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.escNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.escNombre);
    }

    public String getEscNombreBusqueda() {
        return escNombreBusqueda;
    }

    public void setEscNombreBusqueda(String escNombreBusqueda) {
        this.escNombreBusqueda = escNombreBusqueda;
    }

    public Long getEscPk() {
        return escPk;
    }

    public void setEscPk(Long escPk) {
        this.escPk = escPk;
    }

    public String getEscCodigo() {
        return escCodigo;
    }

    public void setEscCodigo(String escCodigo) {
        this.escCodigo = escCodigo;
    }

    public String getEscNombre() {
        return escNombre;
    }

    public void setEscNombre(String escNombre) {
        this.escNombre = escNombre;
    }

    public Boolean getEscHabilitado() {
        return escHabilitado;
    }

    public void setEscHabilitado(Boolean escHabilitado) {
        this.escHabilitado = escHabilitado;
    }

    public LocalDateTime getEscUltModFecha() {
        return escUltModFecha;
    }

    public void setEscUltModFecha(LocalDateTime escUltModFecha) {
        this.escUltModFecha = escUltModFecha;
    }

    public String getEscUltModUsuario() {
        return escUltModUsuario;
    }

    public void setEscUltModUsuario(String escUltModUsuario) {
        this.escUltModUsuario = escUltModUsuario;
    }

    public Integer getEscVersion() {
        return escVersion;
    }

    public void setEscVersion(Integer escVersion) {
        this.escVersion = escVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escPk != null ? escPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEscolaridad)) {
            return false;
        }
        SgEscolaridad other = (SgEscolaridad) object;
        if ((this.escPk == null && other.escPk != null) || (this.escPk != null && !this.escPk.equals(other.escPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesEscolaridad[ escPk=" + escPk + " ]";
    }

}

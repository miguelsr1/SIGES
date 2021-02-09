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
@Table(name = "sg_motivos_traslado", uniqueConstraints = {
    @UniqueConstraint(name = "mot_codigo_uk", columnNames = {"mot_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "motPk", scope = SgMotivoTraslado.class)
public class SgMotivoTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mot_pk")
    private Long motPk;
    
    @Size(max = 4)
    @Column(name = "mot_codigo", length = 4)
    @AtributoCodigo
    private String motCodigo;
    
    @Column(name = "mot_habilitado")
    @AtributoHabilitado
    private Boolean motHabilitado;
    
    @Size(max = 255)
    @Column(name = "mot_nombre", length = 255)
    @AtributoNormalizable
    private String motNombre;
    
    @Size(max = 255)
    @Column(name = "mot_nombre_busqueda", length = 255)
    @AtributoNombre
    private String motNombreBusqueda;
    
    @Column(name = "mot_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime motUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mot_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String motUltModUsuario;
    
    @Column(name = "mot_version")
    @Version
    private Integer motVersion;

    public SgMotivoTraslado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.motNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.motNombre);
    }

    public SgMotivoTraslado(Long motPk) {
        this.motPk = motPk;
    }

    public Long getMotPk() {
        return motPk;
    }

    public void setMotPk(Long motPk) {
        this.motPk = motPk;
    }

    public String getMotCodigo() {
        return motCodigo;
    }

    public void setMotCodigo(String motCodigo) {
        this.motCodigo = motCodigo;
    }

    public Boolean getMotHabilitado() {
        return motHabilitado;
    }

    public void setMotHabilitado(Boolean motHabilitado) {
        this.motHabilitado = motHabilitado;
    }

    public String getMotNombre() {
        return motNombre;
    }

    public void setMotNombre(String motNombre) {
        this.motNombre = motNombre;
    }

    public String getMotNombreBusqueda() {
        return motNombreBusqueda;
    }

    public void setMotNombreBusqueda(String motNombreBusqueda) {
        this.motNombreBusqueda = motNombreBusqueda;
    }

    public LocalDateTime getMotUltModFecha() {
        return motUltModFecha;
    }

    public void setMotUltModFecha(LocalDateTime motUltModFecha) {
        this.motUltModFecha = motUltModFecha;
    }

    public String getMotUltModUsuario() {
        return motUltModUsuario;
    }

    public void setMotUltModUsuario(String motUltModUsuario) {
        this.motUltModUsuario = motUltModUsuario;
    }

    public Integer getMotVersion() {
        return motVersion;
    }

    public void setMotVersion(Integer motVersion) {
        this.motVersion = motVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (motPk != null ? motPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoTraslado)) {
            return false;
        }
        SgMotivoTraslado other = (SgMotivoTraslado) object;
        if ((this.motPk == null && other.motPk != null) || (this.motPk != null && !this.motPk.equals(other.motPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoTraslado[ motPk=" + motPk + " ]";
    }
    
}

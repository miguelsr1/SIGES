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
@Table(name = "sg_profesiones", uniqueConstraints = {
    @UniqueConstraint(name = "pro_codigo_uk", columnNames = {"pro_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "proPk", scope = SgProfesion.class)
public class SgProfesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pro_pk")
    private Long proPk;

    @Size(max = 4)
    @Column(name = "pro_codigo", length = 4)
    @AtributoCodigo
    private String proCodigo;

    @Size(max = 255)
    @Column(name = "pro_nombre", length = 255)
    @AtributoNormalizable
    private String proNombre;

    @Size(max = 255)
    @Column(name = "pro_nombre_busqueda", length = 255)
    @AtributoNombre
    private String proNombreBusqueda;

    @Column(name = "pro_habilitado")
    @AtributoHabilitado
    private Boolean proHabilitado;

    @Column(name = "pro_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime proUltModFecha;

    @Size(max = 45)
    @Column(name = "pro_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String proUltModUsuario;

    @Column(name = "pro_version")
    @Version
    private Integer proVersion;

    public SgProfesion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.proNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.proNombre);
    }

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public String getProCodigo() {
        return proCodigo;
    }

    public void setProCodigo(String proCodigo) {
        this.proCodigo = proCodigo;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProNombreBusqueda() {
        return proNombreBusqueda;
    }

    public void setProNombreBusqueda(String proNombreBusqueda) {
        this.proNombreBusqueda = proNombreBusqueda;
    }

    public Boolean getProHabilitado() {
        return proHabilitado;
    }

    public void setProHabilitado(Boolean proHabilitado) {
        this.proHabilitado = proHabilitado;
    }

    public LocalDateTime getProUltModFecha() {
        return proUltModFecha;
    }

    public void setProUltModFecha(LocalDateTime proUltModFecha) {
        this.proUltModFecha = proUltModFecha;
    }

    public String getProUltModUsuario() {
        return proUltModUsuario;
    }

    public void setProUltModUsuario(String proUltModUsuario) {
        this.proUltModUsuario = proUltModUsuario;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proPk != null ? proPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProfesion)) {
            return false;
        }
        SgProfesion other = (SgProfesion) object;
        if ((this.proPk == null && other.proPk != null) || (this.proPk != null && !this.proPk.equals(other.proPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProfesion[ proPk=" + proPk + " ]";
    }

}

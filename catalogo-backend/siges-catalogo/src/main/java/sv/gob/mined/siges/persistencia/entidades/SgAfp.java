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

@Entity
@Table(name = "sg_afp", uniqueConstraints = {
    @UniqueConstraint(name = "afp_codigo_uk", columnNames = {"afp_codigo"})
    ,
    @UniqueConstraint(name = "afp_nombre_uk", columnNames = {"afp_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "afpPk", scope = SgAfp.class) 
public class SgAfp implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "afp_pk")
    private Long afpPk;
    
    @Size(max = 45)
    @Column(name = "afp_codigo",length = 45)
    @AtributoCodigo
    private String afpCodigo;
    
    @Size(max = 255)
    @Column(name = "afp_nombre",length = 255)
    @AtributoNormalizable
    private String afpNombre;
    
    @Size(max = 255)
    @Column(name = "afp_nombre_busqueda",length = 255)
    @AtributoNombre
    private String afpNombreBusqueda;
    
    @Column(name = "afp_habilitado")
    @AtributoHabilitado
    private Boolean afpHabilitado;
    
    @Column(name = "afp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime afpUltModFecha;
    
    @Size(max = 45)
    @Column(name = "afp_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String afpUltModUsuario;
    
    @Column(name = "afp_version")
    @Version
    private Integer afpVersion;

    public SgAfp() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.afpNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.afpNombre);
    }

    public SgAfp(Long afpPk) {
        this.afpPk = afpPk;
    }

    public Long getAfpPk() {
        return afpPk;
    }

    public void setAfpPk(Long afpPk) {
        this.afpPk = afpPk;
    }

    public String getAfpCodigo() {
        return afpCodigo;
    }

    public void setAfpCodigo(String afpCodigo) {
        this.afpCodigo = afpCodigo;
    }

    public String getAfpNombre() {
        return afpNombre;
    }

    public void setAfpNombre(String afpNombre) {
        this.afpNombre = afpNombre;
    }

    public String getAfpNombreBusqueda() {
        return afpNombreBusqueda;
    }

    public void setAfpNombreBusqueda(String afpNombreBusqueda) {
        this.afpNombreBusqueda = afpNombreBusqueda;
    }

    public Boolean getAfpHabilitado() {
        return afpHabilitado;
    }

    public void setAfpHabilitado(Boolean afpHabilitado) {
        this.afpHabilitado = afpHabilitado;
    }

    public LocalDateTime getAfpUltModFecha() {
        return afpUltModFecha;
    }

    public void setAfpUltModFecha(LocalDateTime afpUltModFecha) {
        this.afpUltModFecha = afpUltModFecha;
    }

    public String getAfpUltModUsuario() {
        return afpUltModUsuario;
    }

    public void setAfpUltModUsuario(String afpUltModUsuario) {
        this.afpUltModUsuario = afpUltModUsuario;
    }

    public Integer getAfpVersion() {
        return afpVersion;
    }

    public void setAfpVersion(Integer afpVersion) {
        this.afpVersion = afpVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (afpPk != null ? afpPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfp)) {
            return false;
        }
        SgAfp other = (SgAfp) object;
        if ((this.afpPk == null && other.afpPk != null) || (this.afpPk != null && !this.afpPk.equals(other.afpPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfp[ afpPk=" + afpPk + " ]";
    }
    
}

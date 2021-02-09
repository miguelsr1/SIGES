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
@Table(name = "sg_sectores_economicos", uniqueConstraints = {
    @UniqueConstraint(name = "sec_codigo_uk", columnNames = {"sec_codigo"})
    ,
    @UniqueConstraint(name = "sec_nombre_uk", columnNames = {"sec_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "secPk", scope = SgSectorEconomico.class)
public class SgSectorEconomico implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sec_pk")
    private Long secPk;
    
    @Size(max = 45)
    @Column(name = "sec_codigo",length = 45)
    @AtributoCodigo
    private String secCodigo;
    
    @Column(name = "sec_habilitado")
    @AtributoHabilitado
    private Boolean secHabilitado;
    
    @Size(max = 255)
    @Column(name = "sec_nombre",length = 255)
    @AtributoNormalizable
    private String secNombre;
    
    @Size(max = 255)
    @Column(name = "sec_nombre_busqueda",length = 255)
    @AtributoNombre
    private String secNombreBusqueda;
    
    @Column(name = "sec_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime secUltModFecha;
    
    @Size(max = 45)
    @Column(name = "sec_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String secUltModUsuario;
    
    @Column(name = "sec_version")
    @Version
    private Integer secVersion;

    public SgSectorEconomico() {
    }


    @PrePersist
    @PreUpdate
    public void preSave() {
        this.secNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.secNombre);
    }


    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public Boolean getSecHabilitado() {
        return secHabilitado;
    }

    public void setSecHabilitado(Boolean secHabilitado) {
        this.secHabilitado = secHabilitado;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public String getSecNombreBusqueda() {
        return secNombreBusqueda;
    }

    public void setSecNombreBusqueda(String secNombreBusqueda) {
        this.secNombreBusqueda = secNombreBusqueda;
    }

    public LocalDateTime getSecUltModFecha() {
        return secUltModFecha;
    }

    public void setSecUltModFecha(LocalDateTime secUltModFecha) {
        this.secUltModFecha = secUltModFecha;
    }

    public String getSecUltModUsuario() {
        return secUltModUsuario;
    }

    public void setSecUltModUsuario(String secUltModUsuario) {
        this.secUltModUsuario = secUltModUsuario;
    }

    public Integer getSecVersion() {
        return secVersion;
    }

    public void setSecVersion(Integer secVersion) {
        this.secVersion = secVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secPk != null ? secPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSectorEconomico)) {
            return false;
        }
        SgSectorEconomico other = (SgSectorEconomico) object;
        if ((this.secPk == null && other.secPk != null) || (this.secPk != null && !this.secPk.equals(other.secPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSectorEconomico[ secPk=" + secPk + " ]";
    }
    
}

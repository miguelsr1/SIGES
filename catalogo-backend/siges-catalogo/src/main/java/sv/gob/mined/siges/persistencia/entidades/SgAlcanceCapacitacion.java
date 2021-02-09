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
@Table(name = "sg_alcance_capacitacion", uniqueConstraints = {
    @UniqueConstraint(name = "aca_codigo_uk", columnNames = {"aca_codigo"})
    ,
    @UniqueConstraint(name = "aca_nombre_uk", columnNames = {"aca_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "acaPk", scope = SgAlcanceCapacitacion.class)
public class SgAlcanceCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aca_pk")
    private Long acaPk;
    
    @Size(max = 45)
    @Column(name = "aca_codigo",length = 45)
    @AtributoCodigo
    private String acaCodigo;
    
    @Column(name = "aca_habilitado")
    @AtributoHabilitado
    private Boolean acaHabilitado;
    
    @Size(max = 255)
    @Column(name = "aca_nombre",length = 255)
    @AtributoNormalizable
    private String acaNombre;
    
    @Size(max = 255)
    @Column(name = "aca_nombre_busqueda",length = 255)
    @AtributoNombre
    private String acaNombreBusqueda;
    
    @Column(name = "aca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime acaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "aca_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String acaUltModUsuario;
    
    @Column(name = "aca_version")
    @Version
    private Integer acaVersion;

    public SgAlcanceCapacitacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.acaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.acaNombre);
    }

    public SgAlcanceCapacitacion(Long acaPk) {
        this.acaPk = acaPk;
    }

    public Long getAcaPk() {
        return acaPk;
    }

    public void setAcaPk(Long acaPk) {
        this.acaPk = acaPk;
    }

    public String getAcaCodigo() {
        return acaCodigo;
    }

    public void setAcaCodigo(String acaCodigo) {
        this.acaCodigo = acaCodigo;
    }

    public Boolean getAcaHabilitado() {
        return acaHabilitado;
    }

    public void setAcaHabilitado(Boolean acaHabilitado) {
        this.acaHabilitado = acaHabilitado;
    }

    public String getAcaNombre() {
        return acaNombre;
    }

    public void setAcaNombre(String acaNombre) {
        this.acaNombre = acaNombre;
    }

    public String getAcaNombreBusqueda() {
        return acaNombreBusqueda;
    }

    public void setAcaNombreBusqueda(String acaNombreBusqueda) {
        this.acaNombreBusqueda = acaNombreBusqueda;
    }

    public LocalDateTime getAcaUltModFecha() {
        return acaUltModFecha;
    }

    public void setAcaUltModFecha(LocalDateTime acaUltModFecha) {
        this.acaUltModFecha = acaUltModFecha;
    }

    public String getAcaUltModUsuario() {
        return acaUltModUsuario;
    }

    public void setAcaUltModUsuario(String acaUltModUsuario) {
        this.acaUltModUsuario = acaUltModUsuario;
    }

    public Integer getAcaVersion() {
        return acaVersion;
    }

    public void setAcaVersion(Integer acaVersion) {
        this.acaVersion = acaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acaPk != null ? acaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAlcanceCapacitacion)) {
            return false;
        }
        SgAlcanceCapacitacion other = (SgAlcanceCapacitacion) object;
        if ((this.acaPk == null && other.acaPk != null) || (this.acaPk != null && !this.acaPk.equals(other.acaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAlcanceCapacitacion[ acaPk=" + acaPk + " ]";
    }
    
}

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

@Entity
@Table(name = "sg_tipos_capacitacion", uniqueConstraints = {
    @UniqueConstraint(name = "tca_codigo_uk", columnNames = {"tca_codigo"})
    ,
    @UniqueConstraint(name = "tca_nombre_uk", columnNames = {"tca_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tcaPk", scope = SgTipoCapacitacion.class)
public class SgTipoCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tca_pk")
    private Long tcaPk;
    
    @Size(max = 45)
    @Column(name = "tca_codigo",length = 45)
    @AtributoCodigo
    private String tcaCodigo;
    
    @Column(name = "tca_habilitado")
    @AtributoHabilitado
    private Boolean tcaHabilitado;
    
    @Size(max = 255)
    @Column(name = "tca_nombre",length = 255)
    @AtributoNormalizable
    private String tcaNombre;
    
    @Size(max = 255)
    @Column(name = "tca_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tcaNombreBusqueda;
    
    @Column(name = "tca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tcaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tca_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tcaUltModUsuario;
    
    @Column(name = "tca_version")
    @Version
    private Integer tcaVersion;

    public SgTipoCapacitacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcaNombre);
    }

    public SgTipoCapacitacion(Long tcaPk) {
        this.tcaPk = tcaPk;
    }

    public Long getTcaPk() {
        return tcaPk;
    }

    public void setTcaPk(Long tcaPk) {
        this.tcaPk = tcaPk;
    }

    public String getTcaCodigo() {
        return tcaCodigo;
    }

    public void setTcaCodigo(String tcaCodigo) {
        this.tcaCodigo = tcaCodigo;
    }

    public Boolean getTcaHabilitado() {
        return tcaHabilitado;
    }

    public void setTcaHabilitado(Boolean tcaHabilitado) {
        this.tcaHabilitado = tcaHabilitado;
    }

    public String getTcaNombre() {
        return tcaNombre;
    }

    public void setTcaNombre(String tcaNombre) {
        this.tcaNombre = tcaNombre;
    }

    public String getTcaNombreBusqueda() {
        return tcaNombreBusqueda;
    }

    public void setTcaNombreBusqueda(String tcaNombreBusqueda) {
        this.tcaNombreBusqueda = tcaNombreBusqueda;
    }

    public LocalDateTime getTcaUltModFecha() {
        return tcaUltModFecha;
    }

    public void setTcaUltModFecha(LocalDateTime tcaUltModFecha) {
        this.tcaUltModFecha = tcaUltModFecha;
    }

    public String getTcaUltModUsuario() {
        return tcaUltModUsuario;
    }

    public void setTcaUltModUsuario(String tcaUltModUsuario) {
        this.tcaUltModUsuario = tcaUltModUsuario;
    }

    public Integer getTcaVersion() {
        return tcaVersion;
    }

    public void setTcaVersion(Integer tcaVersion) {
        this.tcaVersion = tcaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcaPk != null ? tcaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoCapacitacion)) {
            return false;
        }
        SgTipoCapacitacion other = (SgTipoCapacitacion) object;
        if ((this.tcaPk == null && other.tcaPk != null) || (this.tcaPk != null && !this.tcaPk.equals(other.tcaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoCapacitacion[ tcaPk=" + tcaPk + " ]";
    }
    
}

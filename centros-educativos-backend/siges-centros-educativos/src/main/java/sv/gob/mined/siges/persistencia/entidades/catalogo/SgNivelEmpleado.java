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
@Table(name = "sg_nivel_empleado", uniqueConstraints = {
    @UniqueConstraint(name = "nem_codigo_uk", columnNames = {"nem_codigo"})
    ,
    @UniqueConstraint(name = "nem_nombre_uk", columnNames = {"nem_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nemPk", scope = SgNivelEmpleado.class)
public class SgNivelEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nem_pk")
    private Long nemPk;
    
    @Size(max = 45)
    @Column(name = "nem_codigo",length = 45)
    @AtributoCodigo
    private String nemCodigo;
    
    @Column(name = "nem_habilitado")
    @AtributoHabilitado
    private Boolean nemHabilitado;
    
    @Size(max = 255)
    @Column(name = "nem_nombre",length = 255)
    @AtributoNormalizable
    private String nemNombre;
    
    @Size(max = 255)
    @Column(name = "nem_nombre_busqueda")
    @AtributoNombre
    private String nemNombreBusqueda;
    
    @Column(name = "nem_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nemUltModFecha;
    
    @Size(max = 45)
    @Column(name = "nem_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String nemUltModUsuario;
    
    @Column(name = "nem_version")
    @Version
    private Integer nemVersion;

    public SgNivelEmpleado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nemNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nemNombre);
    }
    
    public SgNivelEmpleado(Long nemPk) {
        this.nemPk = nemPk;
    }

    public Long getNemPk() {
        return nemPk;
    }

    public void setNemPk(Long nemPk) {
        this.nemPk = nemPk;
    }

    public String getNemCodigo() {
        return nemCodigo;
    }

    public void setNemCodigo(String nemCodigo) {
        this.nemCodigo = nemCodigo;
    }

    public Boolean getNemHabilitado() {
        return nemHabilitado;
    }

    public void setNemHabilitado(Boolean nemHabilitado) {
        this.nemHabilitado = nemHabilitado;
    }

    public String getNemNombre() {
        return nemNombre;
    }

    public void setNemNombre(String nemNombre) {
        this.nemNombre = nemNombre;
    }

    public String getNemNombreBusqueda() {
        return nemNombreBusqueda;
    }

    public void setNemNombreBusqueda(String nemNombreBusqueda) {
        this.nemNombreBusqueda = nemNombreBusqueda;
    }

    public LocalDateTime getNemUltModFecha() {
        return nemUltModFecha;
    }

    public void setNemUltModFecha(LocalDateTime nemUltModFecha) {
        this.nemUltModFecha = nemUltModFecha;
    }

    public String getNemUltModUsuario() {
        return nemUltModUsuario;
    }

    public void setNemUltModUsuario(String nemUltModUsuario) {
        this.nemUltModUsuario = nemUltModUsuario;
    }

    public Integer getNemVersion() {
        return nemVersion;
    }

    public void setNemVersion(Integer nemVersion) {
        this.nemVersion = nemVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nemPk != null ? nemPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelEmpleado)) {
            return false;
        }
        SgNivelEmpleado other = (SgNivelEmpleado) object;
        if ((this.nemPk == null && other.nemPk != null) || (this.nemPk != null && !this.nemPk.equals(other.nemPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelEmpleado[ nemPk=" + nemPk + " ]";
    }
    
}

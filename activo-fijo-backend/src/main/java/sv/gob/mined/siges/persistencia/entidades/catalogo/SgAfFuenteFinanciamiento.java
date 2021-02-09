/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_fuente_financiamiento", uniqueConstraints = {
    @UniqueConstraint(name = "ffi_codigo_uk", columnNames = {"ffi_codigo"})
    ,
    @UniqueConstraint(name = "ffi_nombre_uk", columnNames = {"ffi_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ffiPk", resolver = JsonIdentityResolver.class, scope = SgAfFuenteFinanciamiento.class)
public class SgAfFuenteFinanciamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ffi_pk")
    private Long ffiPk;

    @Size(max = 20)
    @Column(name = "ffi_codigo", length = 20)
    @AtributoCodigo
    private String ffiCodigo;


    @Size(max = 255)
    @Column(name = "ffi_nombre", length = 255)
    @AtributoNormalizable
    private String ffiNombre;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ffi_nombre_busqueda",  length = 255)
    private String ffiNombreBusqueda;
    
    @Column(name = "ffi_habilitado")
    @AtributoHabilitado
    private Boolean ffiHabilitado;

    @Column(name = "ffi_requiere_proyecto")
    private Boolean ffiRequiereProyecto;
    
    @Column(name = "ffi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ffiUltModFecha;

    @Size(max = 45)
    @Column(name = "ffi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ffiUltModUsuario;

    @Column(name = "ffi_aplica_para", nullable = true)
    @Type(type = "sv.gob.mined.siges.persistencia.utilidades.StringArrayType")
    private String[] ffiAplicaPara;
    
    @Column(name = "ffi_version")
    @Version
    private Integer ffiVersion;
    
    public SgAfFuenteFinanciamiento() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ffiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ffiNombre);
    }

    public Long getFfiPk() {
        return ffiPk;
    }

    public void setFfiPk(Long ffiPk) {
        this.ffiPk = ffiPk;
    }

    public String getFfiCodigo() {
        return ffiCodigo;
    }

    public void setFfiCodigo(String ffiCodigo) {
        this.ffiCodigo = ffiCodigo;
    }

    public String getFfiNombre() {
        return ffiNombre;
    }

    public void setFfiNombre(String ffiNombre) {
        this.ffiNombre = ffiNombre;
    }

    public String getFfiNombreBusqueda() {
        return ffiNombreBusqueda;
    }

    public void setFfiNombreBusqueda(String ffiNombreBusqueda) {
        this.ffiNombreBusqueda = ffiNombreBusqueda;
    }

    public Boolean getFfiHabilitado() {
        return ffiHabilitado;
    }

    public void setFfiHabilitado(Boolean ffiHabilitado) {
        this.ffiHabilitado = ffiHabilitado;
    }

    public LocalDateTime getFfiUltModFecha() {
        return ffiUltModFecha;
    }

    public void setFfiUltModFecha(LocalDateTime ffiUltModFecha) {
        this.ffiUltModFecha = ffiUltModFecha;
    }

    public String getFfiUltModUsuario() {
        return ffiUltModUsuario;
    }

    public void setFfiUltModUsuario(String ffiUltModUsuario) {
        this.ffiUltModUsuario = ffiUltModUsuario;
    }

    public Integer getFfiVersion() {
        return ffiVersion;
    }

    public void setFfiVersion(Integer ffiVersion) {
        this.ffiVersion = ffiVersion;
    }

    public String[] getFfiAplicaPara() {
        return ffiAplicaPara;
    }

    public void setFfiAplicaPara(String[] ffiAplicaPara) {
        this.ffiAplicaPara = ffiAplicaPara;
    }

    public Boolean getFfiRequiereProyecto() {
        return ffiRequiereProyecto;
    }

    public void setFfiRequiereProyecto(Boolean ffiRequiereProyecto) {
        this.ffiRequiereProyecto = ffiRequiereProyecto;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ffiPk != null ? ffiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfFuenteFinanciamiento)) {
            return false;
        }
        SgAfFuenteFinanciamiento other = (SgAfFuenteFinanciamiento) object;
        if ((this.ffiPk == null && other.ffiPk != null) || (this.ffiPk != null && !this.ffiPk.equals(other.ffiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfFuenteFinanciamiento[ ffiPk=" + ffiPk + " ]";
    }
    
}
/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_motivos_licencia", uniqueConstraints = {
    @UniqueConstraint(name = "mli_codigo_uk", columnNames = {"mli_codigo"})
    ,
    @UniqueConstraint(name = "mli_nombre_uk", columnNames = {"mli_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mliPk", scope = SgMotivoLicencia.class)
public class SgMotivoLicencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mli_pk")
    private Long mliPk;
    
    @Size(max = 4)
    @Column(name = "mli_codigo", length = 4)
    @AtributoCodigo
    private String mliCodigo;
    
    @Column(name = "mli_habilitado")
    @AtributoHabilitado
    private Boolean mliHabilitado;
    
    @Size(max = 100)
    @Column(name = "mli_nombre", length = 100)
    @AtributoNormalizable
    private String mliNombre;
    
    @Size(max = 100)
    @Column(name = "mli_nombre_busqueda", length = 100)
    @AtributoNombre
    private String mliNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "mli_descripcion", length = 255)
    @AtributoDescripcion
    private String mliDescripcion;
    
    @Column(name = "mli_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mliUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mli_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mliUltModUsuario;
    
    @Column(name = "mli_version")
    @Version
    private Integer mliVersion;

    public SgMotivoLicencia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mliNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mliNombre);
    }

    public SgMotivoLicencia(Long mliPk) {
        this.mliPk = mliPk;
    }

    public Long getMliPk() {
        return mliPk;
    }

    public void setMliPk(Long mliPk) {
        this.mliPk = mliPk;
    }

    public String getMliCodigo() {
        return mliCodigo;
    }

    public void setMliCodigo(String mliCodigo) {
        this.mliCodigo = mliCodigo;
    }

    public Boolean getMliHabilitado() {
        return mliHabilitado;
    }

    public void setMliHabilitado(Boolean mliHabilitado) {
        this.mliHabilitado = mliHabilitado;
    }

    public String getMliNombre() {
        return mliNombre;
    }

    public void setMliNombre(String mliNombre) {
        this.mliNombre = mliNombre;
    }

    public String getMliNombreBusqueda() {
        return mliNombreBusqueda;
    }

    public void setMliNombreBusqueda(String mliNombreBusqueda) {
        this.mliNombreBusqueda = mliNombreBusqueda;
    }

    public String getMliDescripcion() {
        return mliDescripcion;
    }

    public void setMliDescripcion(String mliDescripcion) {
        this.mliDescripcion = mliDescripcion;
    }

    public LocalDateTime getMliUltModFecha() {
        return mliUltModFecha;
    }

    public void setMliUltModFecha(LocalDateTime mliUltModFecha) {
        this.mliUltModFecha = mliUltModFecha;
    }

    public String getMliUltModUsuario() {
        return mliUltModUsuario;
    }

    public void setMliUltModUsuario(String mliUltModUsuario) {
        this.mliUltModUsuario = mliUltModUsuario;
    }

    public Integer getMliVersion() {
        return mliVersion;
    }

    public void setMliVersion(Integer mliVersion) {
        this.mliVersion = mliVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mliPk != null ? mliPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoLicencia)) {
            return false;
        }
        SgMotivoLicencia other = (SgMotivoLicencia) object;
        if ((this.mliPk == null && other.mliPk != null) || (this.mliPk != null && !this.mliPk.equals(other.mliPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoLicencia[ mliPk=" + mliPk + " ]";
    }
    
}

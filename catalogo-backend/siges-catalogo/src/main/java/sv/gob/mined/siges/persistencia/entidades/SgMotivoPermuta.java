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
@Table(name = "sg_motivos_permuta", uniqueConstraints = {
    @UniqueConstraint(name = "mpe_codigo_uk", columnNames = {"mpe_codigo"})
    ,
    @UniqueConstraint(name = "mpe_nombre_uk", columnNames = {"mpe_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mpePk", scope = SgMotivoPermuta.class)
public class SgMotivoPermuta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mpe_pk")
    private Long mpePk;
    
    @Size(max = 4)
    @Column(name = "mpe_codigo", length = 4)
    @AtributoCodigo
    private String mpeCodigo;
    
    @Column(name = "mpe_habilitado")
    @AtributoHabilitado
    private Boolean mpeHabilitado;
    
    @Size(max = 100)
    @Column(name = "mpe_nombre", length = 100)
    @AtributoNormalizable
    private String mpeNombre;
    
    @Size(max = 100)
    @Column(name = "mpe_nombre_busqueda", length = 100)
    @AtributoNombre
    private String mpeNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "mpe_descripcion", length = 255)
    @AtributoDescripcion
    private String mpeDescripcion;
    
    @Column(name = "mpe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mpeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mpe_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mpeUltModUsuario;
    
    @Column(name = "mpe_version")
    @Version
    private Integer mpeVersion;

    public SgMotivoPermuta() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mpeNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mpeNombre);
    }

    public SgMotivoPermuta(Long mpePk) {
        this.mpePk = mpePk;
    }

    public Long getMpePk() {
        return mpePk;
    }

    public void setMpePk(Long mpePk) {
        this.mpePk = mpePk;
    }

    public String getMpeCodigo() {
        return mpeCodigo;
    }

    public void setMpeCodigo(String mpeCodigo) {
        this.mpeCodigo = mpeCodigo;
    }

    public Boolean getMpeHabilitado() {
        return mpeHabilitado;
    }

    public void setMpeHabilitado(Boolean mpeHabilitado) {
        this.mpeHabilitado = mpeHabilitado;
    }

    public String getMpeNombre() {
        return mpeNombre;
    }

    public void setMpeNombre(String mpeNombre) {
        this.mpeNombre = mpeNombre;
    }

    public String getMpeNombreBusqueda() {
        return mpeNombreBusqueda;
    }

    public void setMpeNombreBusqueda(String mpeNombreBusqueda) {
        this.mpeNombreBusqueda = mpeNombreBusqueda;
    }

    public String getMpeDescripcion() {
        return mpeDescripcion;
    }

    public void setMpeDescripcion(String mpeDescripcion) {
        this.mpeDescripcion = mpeDescripcion;
    }

    public LocalDateTime getMpeUltModFecha() {
        return mpeUltModFecha;
    }

    public void setMpeUltModFecha(LocalDateTime mpeUltModFecha) {
        this.mpeUltModFecha = mpeUltModFecha;
    }

    public String getMpeUltModUsuario() {
        return mpeUltModUsuario;
    }

    public void setMpeUltModUsuario(String mpeUltModUsuario) {
        this.mpeUltModUsuario = mpeUltModUsuario;
    }

    public Integer getMpeVersion() {
        return mpeVersion;
    }

    public void setMpeVersion(Integer mpeVersion) {
        this.mpeVersion = mpeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpePk != null ? mpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoPermuta)) {
            return false;
        }
        SgMotivoPermuta other = (SgMotivoPermuta) object;
        if ((this.mpePk == null && other.mpePk != null) || (this.mpePk != null && !this.mpePk.equals(other.mpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoPermuta[ mpePk=" + mpePk + " ]";
    }
    
}

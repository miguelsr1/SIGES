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
@Table(name = "sg_modo_pago", uniqueConstraints = {
    @UniqueConstraint(name = "mpa_codigo_uk", columnNames = {"mpa_codigo"})
    ,
    @UniqueConstraint(name = "mpa_nombre_uk", columnNames = {"mpa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mpaPk", scope = SgModoPago.class)
public class SgModoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mpa_pk")
    private Long mpaPk;
    
    @Size(max = 45)
    @Column(name = "mpa_codigo",length = 45)
    @AtributoCodigo
    private String mpaCodigo;
    
    @Column(name = "mpa_habilitado")
    @AtributoHabilitado
    private Boolean mpaHabilitado;
    
    @Size(max = 255)
    @Column(name = "mpa_nombre",length = 255)
    @AtributoNormalizable
    private String mpaNombre;
    
    @Size(max = 255)
    @Column(name = "mpa_nombre_busqueda",length = 255)
    @AtributoNombre
    private String mpaNombreBusqueda;
    
    @Column(name = "mpa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mpaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mpa_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mpaUltModUsuario;
    
    @Column(name = "mpa_version")
    @Version
    private Integer mpaVersion;

    public SgModoPago() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mpaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mpaNombre);
    }

    public SgModoPago(Long mpaPk) {
        this.mpaPk = mpaPk;
    }

    public Long getMpaPk() {
        return mpaPk;
    }

    public void setMpaPk(Long mpaPk) {
        this.mpaPk = mpaPk;
    }

    public String getMpaCodigo() {
        return mpaCodigo;
    }

    public void setMpaCodigo(String mpaCodigo) {
        this.mpaCodigo = mpaCodigo;
    }

    public Boolean getMpaHabilitado() {
        return mpaHabilitado;
    }

    public void setMpaHabilitado(Boolean mpaHabilitado) {
        this.mpaHabilitado = mpaHabilitado;
    }

    public String getMpaNombre() {
        return mpaNombre;
    }

    public void setMpaNombre(String mpaNombre) {
        this.mpaNombre = mpaNombre;
    }

    public String getMpaNombreBusqueda() {
        return mpaNombreBusqueda;
    }

    public void setMpaNombreBusqueda(String mpaNombreBusqueda) {
        this.mpaNombreBusqueda = mpaNombreBusqueda;
    }

    public LocalDateTime getMpaUltModFecha() {
        return mpaUltModFecha;
    }

    public void setMpaUltModFecha(LocalDateTime mpaUltModFecha) {
        this.mpaUltModFecha = mpaUltModFecha;
    }

    public String getMpaUltModUsuario() {
        return mpaUltModUsuario;
    }

    public void setMpaUltModUsuario(String mpaUltModUsuario) {
        this.mpaUltModUsuario = mpaUltModUsuario;
    }

    public Integer getMpaVersion() {
        return mpaVersion;
    }

    public void setMpaVersion(Integer mpaVersion) {
        this.mpaVersion = mpaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpaPk != null ? mpaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModoPago)) {
            return false;
        }
        SgModoPago other = (SgModoPago) object;
        if ((this.mpaPk == null && other.mpaPk != null) || (this.mpaPk != null && !this.mpaPk.equals(other.mpaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModoPago[ mpaPk=" + mpaPk + " ]";
    }
    
}

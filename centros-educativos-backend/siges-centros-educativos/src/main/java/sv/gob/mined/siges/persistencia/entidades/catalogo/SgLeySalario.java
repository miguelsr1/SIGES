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
@Table(name = "sg_ley_salario", uniqueConstraints = {
    @UniqueConstraint(name = "lsa_codigo_uk", columnNames = {"lsa_codigo"})
    ,
    @UniqueConstraint(name = "lsa_nombre_uk", columnNames = {"lsa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "lsaPk", scope = SgLeySalario.class)
public class SgLeySalario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lsa_pk")
    private Long lsaPk;
    
    @Size(max = 45)
    @Column(name = "lsa_codigo",length = 45)
    @AtributoCodigo
    private String lsaCodigo;
    
    @Column(name = "lsa_habilitado")
    @AtributoHabilitado
    private Boolean lsaHabilitado;
    
    @Size(max = 255)
    @Column(name = "lsa_nombre",length = 255)
    @AtributoNormalizable
    private String lsaNombre;
    
    @Size(max = 255)
    @Column(name = "lsa_nombre_busqueda",length = 255)
    @AtributoNombre
    private String lsaNombreBusqueda;
    
    @Column(name = "lsa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime lsaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "lsa_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String lsaUltModUsuario;
    
    @Column(name = "lsa_version")
    @Version
    private Integer lsaVersion;

    public SgLeySalario() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.lsaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.lsaNombre);
    }

    public SgLeySalario(Long lsaPk) {
        this.lsaPk = lsaPk;
    }

    public Long getLsaPk() {
        return lsaPk;
    }

    public void setLsaPk(Long lsaPk) {
        this.lsaPk = lsaPk;
    }

    public String getLsaCodigo() {
        return lsaCodigo;
    }

    public void setLsaCodigo(String lsaCodigo) {
        this.lsaCodigo = lsaCodigo;
    }

    public Boolean getLsaHabilitado() {
        return lsaHabilitado;
    }

    public void setLsaHabilitado(Boolean lsaHabilitado) {
        this.lsaHabilitado = lsaHabilitado;
    }

    public String getLsaNombre() {
        return lsaNombre;
    }

    public void setLsaNombre(String lsaNombre) {
        this.lsaNombre = lsaNombre;
    }

    public String getLsaNombreBusqueda() {
        return lsaNombreBusqueda;
    }

    public void setLsaNombreBusqueda(String lsaNombreBusqueda) {
        this.lsaNombreBusqueda = lsaNombreBusqueda;
    }

    public LocalDateTime getLsaUltModFecha() {
        return lsaUltModFecha;
    }

    public void setLsaUltModFecha(LocalDateTime lsaUltModFecha) {
        this.lsaUltModFecha = lsaUltModFecha;
    }

    public String getLsaUltModUsuario() {
        return lsaUltModUsuario;
    }

    public void setLsaUltModUsuario(String lsaUltModUsuario) {
        this.lsaUltModUsuario = lsaUltModUsuario;
    }

    public Integer getLsaVersion() {
        return lsaVersion;
    }

    public void setLsaVersion(Integer lsaVersion) {
        this.lsaVersion = lsaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lsaPk != null ? lsaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgLeySalario)) {
            return false;
        }
        SgLeySalario other = (SgLeySalario) object;
        if ((this.lsaPk == null && other.lsaPk != null) || (this.lsaPk != null && !this.lsaPk.equals(other.lsaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgLeySalario[ lsaPk=" + lsaPk + " ]";
    }
    
}

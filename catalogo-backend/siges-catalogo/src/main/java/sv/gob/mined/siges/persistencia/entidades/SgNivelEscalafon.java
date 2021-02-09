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
@Table(name = "sg_nivel_escalafon", uniqueConstraints = {
    @UniqueConstraint(name = "nes_codigo_uk", columnNames = {"nes_codigo"})
    ,
    @UniqueConstraint(name = "nes_nombre_uk", columnNames = {"nes_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nesPk", scope = SgNivelEscalafon.class)
public class SgNivelEscalafon implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nes_pk")
    private Long nesPk;
    
    @Size(max = 45)
    @Column(name = "nes_codigo")
    @AtributoCodigo
    private String nesCodigo;
    
    @Size(max = 255)
    @Column(name = "nes_nombre")
    @AtributoNormalizable
    private String nesNombre;
    
    @Size(max = 255)
    @Column(name = "nes_nombre_busqueda")
    @AtributoNombre
    private String nesNombreBusqueda;
    
    @Column(name = "nes_habilitado")
    @AtributoHabilitado
    private Boolean nesHabilitado;
    
    @Column(name = "nes_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nesUltModFecha;
    
    @Size(max = 45)
    @Column(name = "nes_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String nesUltModUsuario;
    
    @Column(name = "nes_version")
    @Version
    private Integer nesVersion;

    public SgNivelEscalafon() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.nesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.nesNombre);
    }

    public SgNivelEscalafon(Long nesPk) {
        this.nesPk = nesPk;
    }

    public Long getNesPk() {
        return nesPk;
    }

    public void setNesPk(Long nesPk) {
        this.nesPk = nesPk;
    }

    public String getNesCodigo() {
        return nesCodigo;
    }

    public void setNesCodigo(String nesCodigo) {
        this.nesCodigo = nesCodigo;
    }

    public String getNesNombre() {
        return nesNombre;
    }

    public void setNesNombre(String nesNombre) {
        this.nesNombre = nesNombre;
    }

    public String getNesNombreBusqueda() {
        return nesNombreBusqueda;
    }

    public void setNesNombreBusqueda(String nesNombreBusqueda) {
        this.nesNombreBusqueda = nesNombreBusqueda;
    }

    public Boolean getNesHabilitado() {
        return nesHabilitado;
    }

    public void setNesHabilitado(Boolean nesHabilitado) {
        this.nesHabilitado = nesHabilitado;
    }

    public LocalDateTime getNesUltModFecha() {
        return nesUltModFecha;
    }

    public void setNesUltModFecha(LocalDateTime nesUltModFecha) {
        this.nesUltModFecha = nesUltModFecha;
    }

    public String getNesUltModUsuario() {
        return nesUltModUsuario;
    }

    public void setNesUltModUsuario(String nesUltModUsuario) {
        this.nesUltModUsuario = nesUltModUsuario;
    }

    public Integer getNesVersion() {
        return nesVersion;
    }

    public void setNesVersion(Integer nesVersion) {
        this.nesVersion = nesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nesPk != null ? nesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivelEscalafon)) {
            return false;
        }
        SgNivelEscalafon other = (SgNivelEscalafon) object;
        if ((this.nesPk == null && other.nesPk != null) || (this.nesPk != null && !this.nesPk.equals(other.nesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNivelEscalafon[ nesPk=" + nesPk + " ]";
    }
    
}

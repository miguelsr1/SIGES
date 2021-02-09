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
@Table(name = "sg_carreras", uniqueConstraints = {
    @UniqueConstraint(name = "crr_codigo_uk", columnNames = {"crr_codigo"})
    ,
    @UniqueConstraint(name = "crr_nombre_uk", columnNames = {"crr_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "crrPk", scope = SgCarrera.class)
public class SgCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "crr_pk")
    private Long crrPk;
    
    @Size(max = 45)
    @Column(name = "crr_codigo",length = 45)
    @AtributoCodigo
    private String crrCodigo;
    
    @Column(name = "crr_habilitado")
    @AtributoHabilitado
    private Boolean crrHabilitado;
    
    @Size(max = 255)
    @Column(name = "crr_nombre",length = 255)
    @AtributoNormalizable
    private String crrNombre;
    
    @Size(max = 255)
    @Column(name = "crr_nombre_busqueda",length = 255)
    @AtributoNombre
    private String crrNombreBusqueda;
    
    @Column(name = "crr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime crrUltModFecha;
    
    @Size(max = 45)
    @Column(name = "crr_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String crrUltModUsuario;
    
    @Column(name = "crr_version")
    @Version
    private Integer crrVersion;

    public SgCarrera() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.crrNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.crrNombre);
    }

    public SgCarrera(Long crrPk) {
        this.crrPk = crrPk;
    }

    public Long getCrrPk() {
        return crrPk;
    }

    public void setCrrPk(Long crrPk) {
        this.crrPk = crrPk;
    }

    public String getCrrCodigo() {
        return crrCodigo;
    }

    public void setCrrCodigo(String crrCodigo) {
        this.crrCodigo = crrCodigo;
    }

    public Boolean getCrrHabilitado() {
        return crrHabilitado;
    }

    public void setCrrHabilitado(Boolean crrHabilitado) {
        this.crrHabilitado = crrHabilitado;
    }

    public String getCrrNombre() {
        return crrNombre;
    }

    public void setCrrNombre(String crrNombre) {
        this.crrNombre = crrNombre;
    }

    public String getCrrNombreBusqueda() {
        return crrNombreBusqueda;
    }

    public void setCrrNombreBusqueda(String crrNombreBusqueda) {
        this.crrNombreBusqueda = crrNombreBusqueda;
    }

    public LocalDateTime getCrrUltModFecha() {
        return crrUltModFecha;
    }

    public void setCrrUltModFecha(LocalDateTime crrUltModFecha) {
        this.crrUltModFecha = crrUltModFecha;
    }

    public String getCrrUltModUsuario() {
        return crrUltModUsuario;
    }

    public void setCrrUltModUsuario(String crrUltModUsuario) {
        this.crrUltModUsuario = crrUltModUsuario;
    }

    public Integer getCrrVersion() {
        return crrVersion;
    }

    public void setCrrVersion(Integer crrVersion) {
        this.crrVersion = crrVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (crrPk != null ? crrPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCarrera)) {
            return false;
        }
        SgCarrera other = (SgCarrera) object;
        if ((this.crrPk == null && other.crrPk != null) || (this.crrPk != null && !this.crrPk.equals(other.crrPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCarrera[ crrPk=" + crrPk + " ]";
    }
    
}

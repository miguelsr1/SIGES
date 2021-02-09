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
import java.util.Objects;
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
@Table(name = "sg_idiomas", uniqueConstraints = {
    @UniqueConstraint(name = "idi_codigo_uk", columnNames = {"idi_codigo"})
    ,
    @UniqueConstraint(name = "idi_nombre_uk", columnNames = {"idi_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "idiPk", scope = SgIdioma.class)
public class SgIdioma implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idi_pk")
    private Long idiPk;
    
    @Size(max = 45)
    @Column(name = "idi_codigo",length = 45)
    @AtributoCodigo
    private String idiCodigo;
    
    @Column(name = "idi_habilitado")
    @AtributoHabilitado
    private Boolean idiHabilitado;
    
    @Size(max = 255)
    @Column(name = "idi_nombre",length = 255)
    @AtributoNormalizable
    private String idiNombre;
    
    @Size(max = 255)
    @Column(name = "idi_nombre_busqueda",length = 255)
    @AtributoNombre
    private String idiNombreBusqueda;
    
    @Column(name = "idi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime idiUltModFecha;
    
    @Size(max = 45)
    @Column(name = "idi_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String idiUltModUsuario;
    
    @Column(name = "idi_version")
    @Version
    private Integer idiVersion;

    public SgIdioma() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.idiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.idiNombre);
    }

    public SgIdioma(Long idiPk) {
        this.idiPk = idiPk;
    }

    public Long getIdiPk() {
        return idiPk;
    }

    public void setIdiPk(Long idiPk) {
        this.idiPk = idiPk;
    }

    public String getIdiCodigo() {
        return idiCodigo;
    }

    public void setIdiCodigo(String idiCodigo) {
        this.idiCodigo = idiCodigo;
    }

    public Boolean getIdiHabilitado() {
        return idiHabilitado;
    }

    public void setIdiHabilitado(Boolean idiHabilitado) {
        this.idiHabilitado = idiHabilitado;
    }

    public String getIdiNombre() {
        return idiNombre;
    }

    public void setIdiNombre(String idiNombre) {
        this.idiNombre = idiNombre;
    }

    public String getIdiNombreBusqueda() {
        return idiNombreBusqueda;
    }

    public void setIdiNombreBusqueda(String idiNombreBusqueda) {
        this.idiNombreBusqueda = idiNombreBusqueda;
    }

    public LocalDateTime getIdiUltModFecha() {
        return idiUltModFecha;
    }

    public void setIdiUltModFecha(LocalDateTime idiUltModFecha) {
        this.idiUltModFecha = idiUltModFecha;
    }

    public String getIdiUltModUsuario() {
        return idiUltModUsuario;
    }

    public void setIdiUltModUsuario(String idiUltModUsuario) {
        this.idiUltModUsuario = idiUltModUsuario;
    }

    public Integer getIdiVersion() {
        return idiVersion;
    }

    public void setIdiVersion(Integer idiVersion) {
        this.idiVersion = idiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idiPk != null ? idiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgIdioma other = (SgIdioma) obj;
        if (!Objects.equals(this.idiPk, other.idiPk)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgIdioma[ idiPk=" + idiPk + " ]";
    }
    
}

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
@Table(name = "sg_tipos_estudio", uniqueConstraints = {
    @UniqueConstraint(name = "tes_codigo_uk", columnNames = {"tes_codigo"})
    ,
    @UniqueConstraint(name = "tes_nombre_uk", columnNames = {"tes_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tesPk", scope = SgTipoEstudio.class)
public class SgTipoEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tes_pk")
    private Long tesPk;
    
    @Size(max = 45)
    @Column(name = "tes_codigo",length = 45)
    @AtributoCodigo
    private String tesCodigo;
    
    @Column(name = "tes_habilitado")
    @AtributoHabilitado
    private Boolean tesHabilitado;
    
    @Size(max = 255)
    @Column(name = "tes_nombre",length = 255)
    @AtributoNormalizable
    private String tesNombre;
    
    @Size(max = 255)
    @Column(name = "tes_nombre_busqueda",length = 255)
    @AtributoNombre
    private String tesNombreBusqueda;
    
    @Column(name = "tes_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tesUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tes_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String tesUltModUsuario;
    
    @Column(name = "tes_version")
    @Version
    private Integer tesVersion;

    public SgTipoEstudio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tesNombre);
    }

    public SgTipoEstudio(Long tesPk) {
        this.tesPk = tesPk;
    }

    public Long getTesPk() {
        return tesPk;
    }

    public void setTesPk(Long tesPk) {
        this.tesPk = tesPk;
    }

    public String getTesCodigo() {
        return tesCodigo;
    }

    public void setTesCodigo(String tesCodigo) {
        this.tesCodigo = tesCodigo;
    }

    public Boolean getTesHabilitado() {
        return tesHabilitado;
    }

    public void setTesHabilitado(Boolean tesHabilitado) {
        this.tesHabilitado = tesHabilitado;
    }

    public String getTesNombre() {
        return tesNombre;
    }

    public void setTesNombre(String tesNombre) {
        this.tesNombre = tesNombre;
    }

    public String getTesNombreBusqueda() {
        return tesNombreBusqueda;
    }

    public void setTesNombreBusqueda(String tesNombreBusqueda) {
        this.tesNombreBusqueda = tesNombreBusqueda;
    }

    public LocalDateTime getTesUltModFecha() {
        return tesUltModFecha;
    }

    public void setTesUltModFecha(LocalDateTime tesUltModFecha) {
        this.tesUltModFecha = tesUltModFecha;
    }

    public String getTesUltModUsuario() {
        return tesUltModUsuario;
    }

    public void setTesUltModUsuario(String tesUltModUsuario) {
        this.tesUltModUsuario = tesUltModUsuario;
    }

    public Integer getTesVersion() {
        return tesVersion;
    }

    public void setTesVersion(Integer tesVersion) {
        this.tesVersion = tesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tesPk != null ? tesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoEstudio)) {
            return false;
        }
        SgTipoEstudio other = (SgTipoEstudio) object;
        if ((this.tesPk == null && other.tesPk != null) || (this.tesPk != null && !this.tesPk.equals(other.tesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoEstudio[ tesPk=" + tesPk + " ]";
    }
    
}

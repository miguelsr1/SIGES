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

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_recursos_educativos", uniqueConstraints = {
    @UniqueConstraint(name = "red_codigo_uk", columnNames = {"red_codigo"})
    ,
    @UniqueConstraint(name = "red_nombre_uk", columnNames = {"red_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "redPk", scope = SgRecursoEducativo.class)
public class SgRecursoEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "red_pk")
    private Long redPk;
    
    @Size(max = 4)
    @Column(name = "red_codigo", length = 4)
    @AtributoCodigo
    private String redCodigo;
    
    @Column(name = "red_habilitado")
    @AtributoHabilitado
    private Boolean redHabilitado;
    
    @Size(max = 255)
    @Column(name = "red_nombre", length = 255)
    @AtributoNormalizable
    private String redNombre;
    
    @Size(max = 255)
    @Column(name = "red_nombre_busqueda", length = 255)
    @AtributoNombre
    private String redNombreBusqueda;
    
    @Column(name = "red_siap_pk")
    private Long redSiapPk;
    
    @Column(name = "red_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime redUltModFecha;
    
    @Size(max = 45)
    @Column(name = "red_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String redUltModUsuario;
    
    @Column(name = "red_version")
    @Version
    private Integer redVersion;

    public SgRecursoEducativo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.redNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.redNombre);
    }

    public SgRecursoEducativo(Long redPk) {
        this.redPk = redPk;
    }

    public Long getRedPk() {
        return redPk;
    }

    public void setRedPk(Long redPk) {
        this.redPk = redPk;
    }

    public String getRedCodigo() {
        return redCodigo;
    }

    public void setRedCodigo(String redCodigo) {
        this.redCodigo = redCodigo;
    }

    public Boolean getRedHabilitado() {
        return redHabilitado;
    }

    public void setRedHabilitado(Boolean redHabilitado) {
        this.redHabilitado = redHabilitado;
    }

    public String getRedNombre() {
        return redNombre;
    }

    public void setRedNombre(String redNombre) {
        this.redNombre = redNombre;
    }

    public String getRedNombreBusqueda() {
        return redNombreBusqueda;
    }

    public void setRedNombreBusqueda(String redNombreBusqueda) {
        this.redNombreBusqueda = redNombreBusqueda;
    }

    public LocalDateTime getRedUltModFecha() {
        return redUltModFecha;
    }

    public void setRedUltModFecha(LocalDateTime redUltModFecha) {
        this.redUltModFecha = redUltModFecha;
    }

    public String getRedUltModUsuario() {
        return redUltModUsuario;
    }

    public void setRedUltModUsuario(String redUltModUsuario) {
        this.redUltModUsuario = redUltModUsuario;
    }

    public Integer getRedVersion() {
        return redVersion;
    }

    public void setRedVersion(Integer redVersion) {
        this.redVersion = redVersion;
    }

    public Long getRedSiapPk() {
        return redSiapPk;
    }

    public void setRedSiapPk(Long redSiapPk) {
        this.redSiapPk = redSiapPk;
    }
    
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (redPk != null ? redPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRecursoEducativo)) {
            return false;
        }
        SgRecursoEducativo other = (SgRecursoEducativo) object;
        if ((this.redPk == null && other.redPk != null) || (this.redPk != null && !this.redPk.equals(other.redPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRecursoEducativo[ redPk=" + redPk + " ]";
    }
    
}

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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_secciones_categoria", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "scaPk", resolver = JsonIdentityResolver.class,scope = SgAfSeccionCategoria.class)
public class SgAfSeccionCategoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sca_pk")
    private Long scaPk;
    
    @Column(name = "sca_seccion", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumSeccionesCargoBienes scaSeccion;
    
    @Column(name = "sca_ult_mod_fecha") 
    @AtributoUltimaModificacion
    private LocalDateTime scaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "sca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String scaUltModUsuario;
    
    @Column(name = "sca_version")
    @Version
    private Integer scaVersion;
    
    @JoinColumn(name = "sca_categoria_fk", referencedColumnName = "cab_pk")
    @ManyToOne
    private SgAfCategoriaBienes scaCategoriaFk;

    public SgAfSeccionCategoria() {
    }

    public SgAfSeccionCategoria(Long scaPk) {
        this.scaPk = scaPk;
    }

    public Long getScaPk() {
        return scaPk;
    }

    public void setScaPk(Long scaPk) {
        this.scaPk = scaPk;
    }

    public LocalDateTime getScaUltModFecha() {
        return scaUltModFecha;
    }

    public void setScaUltModFecha(LocalDateTime scaUltModFecha) {
        this.scaUltModFecha = scaUltModFecha;
    }

    public String getScaUltModUsuario() {
        return scaUltModUsuario;
    }

    public void setScaUltModUsuario(String scaUltModUsuario) {
        this.scaUltModUsuario = scaUltModUsuario;
    }

    public Integer getScaVersion() {
        return scaVersion;
    }

    public void setScaVersion(Integer scaVersion) {
        this.scaVersion = scaVersion;
    }

    public SgAfCategoriaBienes getScaCategoriaFk() {
        return scaCategoriaFk;
    }

    public void setScaCategoriaFk(SgAfCategoriaBienes scaCategoriaFk) {
        this.scaCategoriaFk = scaCategoriaFk;
    }

    public EnumSeccionesCargoBienes getScaSeccion() {
        return scaSeccion;
    }

    public void setScaSeccion(EnumSeccionesCargoBienes scaSeccion) {
        this.scaSeccion = scaSeccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scaPk != null ? scaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfSeccionCategoria)) {
            return false;
        }
        SgAfSeccionCategoria other = (SgAfSeccionCategoria) object;
        if ((this.scaPk == null && other.scaPk != null) || (this.scaPk != null && !this.scaPk.equals(other.scaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfSeccionesCategoria[ scaPk=" + scaPk + " ]";
    }
    
}

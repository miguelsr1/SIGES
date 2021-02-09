/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProyectoFinanciamientoSistemaIntegrado;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_proyecto_financiamiento_sistema_integrado", schema = Constantes.SCHEMA_SISTEMAS_INTEGRADOS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rpsPk", scope = SgRelProyectoFinanciamientoSistemaIntegrado.class)
public class SgRelProyectoFinanciamientoSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rps_pk")
    private Long rpsPk;

    @JoinColumn(name = "rps_sistema_integrado_fk", referencedColumnName = "sin_pk")
    @ManyToOne
    private SgSistemaIntegrado rpsSistemaIntegrado;

    @JoinColumn(name = "rps_proyecto_financiamiento_fk", referencedColumnName = "pfs_pk")
    @ManyToOne
    private SgProyectoFinanciamientoSistemaIntegrado rpsProyectoFinanciamiento;

    @Column(name = "rps_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rpsUltModFecha;

    @Size(max = 45)
    @Column(name = "rps_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rpsUltModUsuario;

    @Column(name = "rps_version")
    @Version
    private Integer rpsVersion;

    public SgRelProyectoFinanciamientoSistemaIntegrado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public SgRelProyectoFinanciamientoSistemaIntegrado(Long rpsPk) {
        this.rpsPk = rpsPk;
    }

    public Long getRpsPk() {
        return rpsPk;
    }

    public void setRpsPk(Long rpsPk) {
        this.rpsPk = rpsPk;
    }

    public SgSistemaIntegrado getRpsSistemaIntegrado() {
        return rpsSistemaIntegrado;
    }

    public void setRpsSistemaIntegrado(SgSistemaIntegrado rpsSistemaIntegrado) {
        this.rpsSistemaIntegrado = rpsSistemaIntegrado;
    }

    public SgProyectoFinanciamientoSistemaIntegrado getRpsProyectoFinanciamiento() {
        return rpsProyectoFinanciamiento;
    }

    public void setRpsProyectoFinanciamiento(SgProyectoFinanciamientoSistemaIntegrado rpsProyectoFinanciamiento) {
        this.rpsProyectoFinanciamiento = rpsProyectoFinanciamiento;
    }

    public LocalDateTime getRpsUltModFecha() {
        return rpsUltModFecha;
    }

    public void setRpsUltModFecha(LocalDateTime rpsUltModFecha) {
        this.rpsUltModFecha = rpsUltModFecha;
    }

    public String getRpsUltModUsuario() {
        return rpsUltModUsuario;
    }

    public void setRpsUltModUsuario(String rpsUltModUsuario) {
        this.rpsUltModUsuario = rpsUltModUsuario;
    }

    public Integer getRpsVersion() {
        return rpsVersion;
    }

    public void setRpsVersion(Integer rpsVersion) {
        this.rpsVersion = rpsVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpsPk != null ? rpsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelProyectoFinanciamientoSistemaIntegrado)) {
            return false;
        }
        SgRelProyectoFinanciamientoSistemaIntegrado other = (SgRelProyectoFinanciamientoSistemaIntegrado) object;
        if ((this.rpsPk == null && other.rpsPk != null) || (this.rpsPk != null && !this.rpsPk.equals(other.rpsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelAcuerdoPropuestaPedagogica[ rpsPk=" + rpsPk + " ]";
    }

}

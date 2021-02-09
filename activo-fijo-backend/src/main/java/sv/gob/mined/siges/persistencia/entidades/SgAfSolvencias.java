/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_solvencias", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "solPk", scope = SgAfSolvencias.class)
public class SgAfSolvencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sol_pk")
    private Long solPk;

    @JoinColumn(name = "sol_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede solSedeFk;

    @JoinColumn(name = "sol_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas solUnidadAdministrativaFk;
    
    @Column(name = "sol_fecha_solvencia")
    private LocalDate solFechaSolvencia;
    
    @Column(name = "sol_anio")
    private Integer solAnio;
    
    @Column(name = "sol_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime solUltModFecha;
    
    @Column(name = "sol_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String solUltModUsuario;
    
    @Column(name = "sol_version")
    @Version
    private Integer solVersion;

    public SgAfSolvencias() {
    }

    public Long getSolPk() {
        return solPk;
    }

    public void setSolPk(Long solPk) {
        this.solPk = solPk;
    }

    public SgSede getSolSedeFk() {
        return solSedeFk;
    }

    public void setSolSedeFk(SgSede solSedeFk) {
        this.solSedeFk = solSedeFk;
    }

    public SgAfUnidadesAdministrativas getSolUnidadAdministrativaFk() {
        return solUnidadAdministrativaFk;
    }

    public void setSolUnidadAdministrativaFk(SgAfUnidadesAdministrativas solUnidadAdministrativaFk) {
        this.solUnidadAdministrativaFk = solUnidadAdministrativaFk;
    }

    public LocalDate getSolFechaSolvencia() {
        return solFechaSolvencia;
    }

    public void setSolFechaSolvencia(LocalDate solFechaSolvencia) {
        this.solFechaSolvencia = solFechaSolvencia;
    }

    public Integer getSolAnio() {
        return solAnio;
    }

    public void setSolAnio(Integer solAnio) {
        this.solAnio = solAnio;
    }

    public LocalDateTime getSolUltModFecha() {
        return solUltModFecha;
    }

    public void setSolUltModFecha(LocalDateTime solUltModFecha) {
        this.solUltModFecha = solUltModFecha;
    }

    public String getSolUltModUsuario() {
        return solUltModUsuario;
    }

    public void setSolUltModUsuario(String solUltModUsuario) {
        this.solUltModUsuario = solUltModUsuario;
    }

    public Integer getSolVersion() {
        return solVersion;
    }

    public void setSolVersion(Integer solVersion) {
        this.solVersion = solVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solPk != null ? solPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfSolvencias)) {
            return false;
        }
        SgAfSolvencias other = (SgAfSolvencias) object;
        if ((this.solPk == null && other.solPk != null) || (this.solPk != null && !this.solPk.equals(other.solPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfSolvencias[ solPk=" + solPk + " ]";
    }
    
}

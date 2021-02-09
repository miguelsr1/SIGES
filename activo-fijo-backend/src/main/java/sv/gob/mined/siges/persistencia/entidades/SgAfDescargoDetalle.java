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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_descargos_detalle", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ddePk", scope = SgAfDescargoDetalle.class)
public class SgAfDescargoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dde_pk")
    private Long ddePk;
    
    @Column(name = "dde_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ddeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dde_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ddeUltModUsuario;
    
    @Column(name = "dde_version")
    @Version
    private Integer ddeVersion;
    
    @JoinColumn(name = "dde_bienes_depreciables_fk", referencedColumnName = "bde_pk")
    @ManyToOne(optional = false)
    private SgAfBienDepreciable ddeBienesDepreciablesFk;
    
    @JoinColumn(name = "dde_descargo_fk", referencedColumnName = "des_pk")
    @ManyToOne(optional = false)
    private SgAfDescargo ddeDescargoFk;

    @Transient
    private String ddeTipoDescargo;
    @Transient
    private String ddeEstadoDescargo;
    
    @Transient
    private LocalDate ddeFechaDescargo;
    
    public SgAfDescargoDetalle() {
    }

    public SgAfDescargoDetalle(Long ddePk) {
        this.ddePk = ddePk;
    }

    public Long getDdePk() {
        return ddePk;
    }

    public void setDdePk(Long ddePk) {
        this.ddePk = ddePk;
    }

    public LocalDateTime getDdeUltModFecha() {
        return ddeUltModFecha;
    }

    public void setDdeUltModFecha(LocalDateTime ddeUltModFecha) {
        this.ddeUltModFecha = ddeUltModFecha;
    }

    public String getDdeUltModUsuario() {
        return ddeUltModUsuario;
    }

    public void setDdeUltModUsuario(String ddeUltModUsuario) {
        this.ddeUltModUsuario = ddeUltModUsuario;
    }

    public Integer getDdeVersion() {
        return ddeVersion;
    }

    public void setDdeVersion(Integer ddeVersion) {
        this.ddeVersion = ddeVersion;
    }

    public SgAfBienDepreciable getDdeBienesDepreciablesFk() {
        return ddeBienesDepreciablesFk;
    }

    public void setDdeBienesDepreciablesFk(SgAfBienDepreciable ddeBienesDepreciablesFk) {
        this.ddeBienesDepreciablesFk = ddeBienesDepreciablesFk;
    }

    public SgAfDescargo getDdeDescargoFk() {
        return ddeDescargoFk;
    }

    public void setDdeDescargoFk(SgAfDescargo ddeDescargoFk) {
        this.ddeDescargoFk = ddeDescargoFk;
    }

    public String getDdeTipoDescargo() {
        if(ddeDescargoFk != null && ddeDescargoFk.getDesTipoDescargoFk() != null) {
            this.ddeTipoDescargo = ddeDescargoFk.getDesTipoDescargoFk().getEdeNombre();
        } else {
            this.ddeTipoDescargo = "";
        }
        return ddeTipoDescargo;
    }

    public void setDdeTipoDescargo(String ddeTipoDescargo) {
        this.ddeTipoDescargo = ddeTipoDescargo;
    }

    public String getDdeEstadoDescargo() {
        if(ddeDescargoFk != null && ddeDescargoFk.getDesEstadoFk() != null) {
            this.ddeEstadoDescargo = ddeDescargoFk.getDesEstadoFk().getEbiNombre();
        } else {
            this.ddeEstadoDescargo = "";
        }
        return ddeEstadoDescargo;
    }

    public void setDdeEstadoDescargo(String ddeEstadoDescargo) {
        this.ddeEstadoDescargo = ddeEstadoDescargo;
    }

    public LocalDate getDdeFechaDescargo() {
        if(ddeDescargoFk != null) {
            this.ddeFechaDescargo = ddeDescargoFk.getDesFechaDescargo();
        } else {
            this.ddeFechaDescargo = null;
        }
        return ddeFechaDescargo;
    }

    public void setDdeFechaDescargo(LocalDate ddeFechaDescargo) {
        this.ddeFechaDescargo = ddeFechaDescargo;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ddePk != null ? ddePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfDescargoDetalle)) {
            return false;
        }
        SgAfDescargoDetalle other = (SgAfDescargoDetalle) object;
        if ((this.ddePk == null && other.ddePk != null) || (this.ddePk != null && !this.ddePk.equals(other.ddePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfDescargosDetalle[ ddePk=" + ddePk + " ]";
    }
    
}

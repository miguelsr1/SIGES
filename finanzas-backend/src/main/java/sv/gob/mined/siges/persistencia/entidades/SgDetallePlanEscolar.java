/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_detalle_plan_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dpePk", scope = SgDetallePlanEscolar.class)
/**
 * Entidad correspondiente a las actividades del plan escolar anual.
 */
public class SgDetallePlanEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dpe_pk")
    private Long dpePk;

    @JoinColumn(name = "dpe_plan_escolar_anual_fk", referencedColumnName = "pea_pk")
    @ManyToOne
    private SgPlanEscolarAnual dpePlanEscolarAnual;

    @Size(max = 500)
    @Column(name = "dpe_actividad", length = 500)
    private String dpeActividad;

    @Size(max = 255)
    @Column(name = "dpe_responsables", length = 255)
    private String dpeResponsables;

    @Column(name = "dpe_costo_estimado")
    private Double dpeCostoEstimado;

    @Column(name = "dpe_fecha_inicio")
    private LocalDate dpeFechaInicio;

    @Column(name = "dpe_fecha_fin")
    private LocalDate dpeFechaFin;

    @Column(name = "dpe_validado")
    private Boolean dpeValidado;

    @Column(name = "dpe_version")
    @Version
    private Integer dpeVersion;

    public SgDetallePlanEscolar() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public SgDetallePlanEscolar(Long dpePk) {
        this.dpePk = dpePk;
    }

    public Long getDpePk() {
        return dpePk;
    }

    public void setDpePk(Long dpePk) {
        this.dpePk = dpePk;
    }

    public SgPlanEscolarAnual getDpePlanEscolarAnual() {
        return dpePlanEscolarAnual;
    }

    public void setDpePlanEscolarAnual(SgPlanEscolarAnual dpePlanEscolarAnual) {
        this.dpePlanEscolarAnual = dpePlanEscolarAnual;
    }

    public String getDpeActividad() {
        return dpeActividad;
    }

    public void setDpeActividad(String dpeActividad) {
        this.dpeActividad = dpeActividad;
    }

    public String getDpeResponsables() {
        return dpeResponsables;
    }

    public void setDpeResponsables(String dpeResponsables) {
        this.dpeResponsables = dpeResponsables;
    }

    public Double getDpeCostoEstimado() {
        return dpeCostoEstimado;
    }

    public void setDpeCostoEstimado(Double dpeCostoEstimado) {
        this.dpeCostoEstimado = dpeCostoEstimado;
    }

    public LocalDate getDpeFechaInicio() {
        return dpeFechaInicio;
    }

    public void setDpeFechaInicio(LocalDate dpeFechaInicio) {
        this.dpeFechaInicio = dpeFechaInicio;
    }

    public LocalDate getDpeFechaFin() {
        return dpeFechaFin;
    }

    public void setDpeFechaFin(LocalDate dpeFechaFin) {
        this.dpeFechaFin = dpeFechaFin;
    }

    public Integer getDpeVersion() {
        return dpeVersion;
    }

    public void setDpeVersion(Integer dpeVersion) {
        this.dpeVersion = dpeVersion;
    }

    public Boolean getDpeValidado() {
        return dpeValidado;
    }

    public void setDpeValidado(Boolean dpeValidado) {
        this.dpeValidado = dpeValidado;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.dpePk);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgDetallePlanEscolar)) {
            return false;
        }
        SgDetallePlanEscolar other = (SgDetallePlanEscolar) object;
        if ((this.dpePk == null && other.dpePk != null) || (this.dpePk != null && !this.dpePk.equals(other.dpePk))) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar[ dpePk=" + dpePk + " ]";
    }
    // </editor-fold>
}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_habilitacion_periodo_matricula", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hmpPk", scope = SgHabilitacionPeriodoMatricula.class)
@Audited
public class SgHabilitacionPeriodoMatricula implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hmp_pk", nullable = false)
    private Long hmpPk;
    
    @Column(name = "hmp_fecha_solicitud")
    private LocalDate hmpFechaSolicitud;

    @Column(name = "hmp_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoHabilitacionPeriodoMatricula hmpEstado;
    
    @Size(max = 500)
    @Column(name = "hmp_observacion", length = 500)
    private String hmpObservacion;
    
    @Column(name = "hmp_fecha_desde")
    private LocalDate hmpFechaDesde;
    
    @Column(name = "hmp_fecha_hasta")
    private LocalDate hmpFechaHasta;
    
    @Size(max = 500)
    @Column(name = "hmp_observacion_aprobacion_rechazo", length = 500)
    private String hmpObservacionAprobacionRechazo;    
    

    @Column(name = "hmp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime hmpUltModFecha;

    @Size(max = 45)
    @Column(name = "hmp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String hmpUltModUsuario;

    @Column(name = "hmp_version")
    @Version
    private Integer hmpVersion;
    
    @JoinColumn(name = "hmp_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSede hmpSedeFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rhnHabilitacionPeriodoMatriculaFk")
    private List<SgRelHabilitacionMatriculaNivel> hmpRelHabilitacionMatriculaNivel;

    public SgHabilitacionPeriodoMatricula() {
    }


    public Long getHmpPk() {
        return hmpPk;
    }

    public void setHmpPk(Long hmpPk) {
        this.hmpPk = hmpPk;
    }

    public LocalDate getHmpFechaSolicitud() {
        return hmpFechaSolicitud;
    }

    public void setHmpFechaSolicitud(LocalDate hmpFechaSolicitud) {
        this.hmpFechaSolicitud = hmpFechaSolicitud;
    }

    public EnumEstadoHabilitacionPeriodoMatricula getHmpEstado() {
        return hmpEstado;
    }

    public void setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula hmpEstado) {
        this.hmpEstado = hmpEstado;
    }

    public String getHmpObservacion() {
        return hmpObservacion;
    }

    public void setHmpObservacion(String hmpObservacion) {
        this.hmpObservacion = hmpObservacion;
    }

    public LocalDate getHmpFechaDesde() {
        return hmpFechaDesde;
    }

    public void setHmpFechaDesde(LocalDate hmpFechaDesde) {
        this.hmpFechaDesde = hmpFechaDesde;
    }

    public LocalDate getHmpFechaHasta() {
        return hmpFechaHasta;
    }

    public void setHmpFechaHasta(LocalDate hmpFechaHasta) {
        this.hmpFechaHasta = hmpFechaHasta;
    }

    public String getHmpObservacionAprobacionRechazo() {
        return hmpObservacionAprobacionRechazo;
    }

    public void setHmpObservacionAprobacionRechazo(String hmpObservacionAprobacionRechazo) {
        this.hmpObservacionAprobacionRechazo = hmpObservacionAprobacionRechazo;
    }

    public SgSede getHmpSedeFk() {
        return hmpSedeFk;
    }

    public void setHmpSedeFk(SgSede hmpSedeFk) {
        this.hmpSedeFk = hmpSedeFk;
    }

    public List<SgRelHabilitacionMatriculaNivel> getHmpRelHabilitacionMatriculaNivel() {
        return hmpRelHabilitacionMatriculaNivel;
    }

    public void setHmpRelHabilitacionMatriculaNivel(List<SgRelHabilitacionMatriculaNivel> hmpRelHabilitacionMatriculaNivel) {
        this.hmpRelHabilitacionMatriculaNivel = hmpRelHabilitacionMatriculaNivel;
    }

   
    public LocalDateTime getHmpUltModFecha() {
        return hmpUltModFecha;
    }

    public void setHmpUltModFecha(LocalDateTime hmpUltModFecha) {
        this.hmpUltModFecha = hmpUltModFecha;
    }

    public String getHmpUltModUsuario() {
        return hmpUltModUsuario;
    }

    public void setHmpUltModUsuario(String hmpUltModUsuario) {
        this.hmpUltModUsuario = hmpUltModUsuario;
    }

    public Integer getHmpVersion() {
        return hmpVersion;
    }

    public void setHmpVersion(Integer hmpVersion) {
        this.hmpVersion = hmpVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.hmpPk);
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
        final SgHabilitacionPeriodoMatricula other = (SgHabilitacionPeriodoMatricula) obj;
        if (!Objects.equals(this.hmpPk, other.hmpPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgHabilitacionPeriodoMatricula{" + "hmpPk=" + hmpPk + ", hmpFechaSolicitud=" + hmpFechaSolicitud + ", hmpEstado=" + hmpEstado + ", hmpObservacion=" + hmpObservacion + ", hmpFechaDesde=" + hmpFechaDesde + ", hmpFechaHasta=" + hmpFechaHasta + ", hmpObservacionAprobacionRechazo=" + hmpObservacionAprobacionRechazo + ", hmpUltModFecha=" + hmpUltModFecha + ", hmpUltModUsuario=" + hmpUltModUsuario + ", hmpVersion=" + hmpVersion + ", hmpSedeFk=" + hmpSedeFk + ", hmpRelHabilitacionMatriculaNivel=" + hmpRelHabilitacionMatriculaNivel + '}';
    }

        @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hmpPk", -1L);
        }
    }

}

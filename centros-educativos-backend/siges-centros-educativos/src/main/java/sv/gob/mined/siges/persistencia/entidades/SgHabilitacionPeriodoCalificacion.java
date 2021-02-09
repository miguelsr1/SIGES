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
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_habilitacion_periodo_calificacion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hpcPk", scope = SgHabilitacionPeriodoCalificacion.class)
@Audited
public class SgHabilitacionPeriodoCalificacion implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hpc_pk", nullable = false)
    private Long hpcPk;

    @JoinColumn(name = "hpc_matricula_fk", referencedColumnName = "mat_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgMatricula hpcMatriculaFk;
    
    @Column(name = "hpc_fecha_solicitud")
    private LocalDate hpcFechaSolicitud;
    
    @Column(name = "hpc_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoHabilitacionPeriodoCalificacion hpcEstado;

    @Size(max = 500)
    @Column(name = "hpc_observacion", length = 500)
    private String hpcObservacion;
    
    @JoinColumn(name = "hpc_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante hpcEstudianteFk;
    
    @Size(max = 500)
    @Column(name = "hpc_observacion_aprobacion_rechazo", length = 500)
    private String hpcObservacionAprobacionRechazo;      
  

    @Column(name = "hpc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime hpcUltModFecha;

    @Size(max = 45)
    @Column(name = "hpc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String hpcUltModUsuario;

    @Column(name = "hpc_version")
    @Version
    private Integer hpcVersion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rphHabilitacionPeriodoCalificacionFk")
    private List<SgRelPeriodosHabilitacionPeriodo> relPeriodosHabilitacionPeriodo;

    public SgHabilitacionPeriodoCalificacion() {
    }


    public LocalDate getHpcFechaSolicitud() {
        return hpcFechaSolicitud;
    }

    public void setHpcFechaSolicitud(LocalDate hpcFechaSolicitud) {
        this.hpcFechaSolicitud = hpcFechaSolicitud;
    }

    public EnumEstadoHabilitacionPeriodoCalificacion getHpcEstado() {
        return hpcEstado;
    }

    public void setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion hpcEstado) {
        this.hpcEstado = hpcEstado;
    }

    public String getHpcObservacion() {
        return hpcObservacion;
    }

    public void setHpcObservacion(String hpcObservacion) {
        this.hpcObservacion = hpcObservacion;
    }

    public SgEstudiante getHpcEstudianteFk() {
        return hpcEstudianteFk;
    }

    public void setHpcEstudianteFk(SgEstudiante hpcEstudianteFk) {
        this.hpcEstudianteFk = hpcEstudianteFk;
    }


    public String getHpcObservacionAprobacionRechazo() {
        return hpcObservacionAprobacionRechazo;
    }

    public void setHpcObservacionAprobacionRechazo(String hpcObservacionAprobacionRechazo) {
        this.hpcObservacionAprobacionRechazo = hpcObservacionAprobacionRechazo;
    } 

    public Long getHpcPk() {
        return hpcPk;
    }

    public void setHpcPk(Long hpcPk) {
        this.hpcPk = hpcPk;
    }

  
    public LocalDateTime getHpcUltModFecha() {
        return hpcUltModFecha;
    }

    public void setHpcUltModFecha(LocalDateTime hpcUltModFecha) {
        this.hpcUltModFecha = hpcUltModFecha;
    }

    public String getHpcUltModUsuario() {
        return hpcUltModUsuario;
    }

    public void setHpcUltModUsuario(String hpcUltModUsuario) {
        this.hpcUltModUsuario = hpcUltModUsuario;
    }

    public Integer getHpcVersion() {
        return hpcVersion;
    }

    public void setHpcVersion(Integer hpcVersion) {
        this.hpcVersion = hpcVersion;
    }

    public List<SgRelPeriodosHabilitacionPeriodo> getRelPeriodosHabilitacionPeriodo() {
        return relPeriodosHabilitacionPeriodo;
    }

    public void setRelPeriodosHabilitacionPeriodo(List<SgRelPeriodosHabilitacionPeriodo> relPeriodosHabilitacionPeriodo) {
        this.relPeriodosHabilitacionPeriodo = relPeriodosHabilitacionPeriodo;
    }

    public SgMatricula getHpcMatriculaFk() {
        return hpcMatriculaFk;
    }

    public void setHpcMatriculaFk(SgMatricula hpcMatriculaFk) {
        this.hpcMatriculaFk = hpcMatriculaFk;
    }
    
        @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcMatriculaFk.matSeccion.secServicioEducativo.sduSede.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hpcPk", -1L);
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.hpcPk);
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
        final SgHabilitacionPeriodoCalificacion other = (SgHabilitacionPeriodoCalificacion) obj;
        if (!Objects.equals(this.hpcPk, other.hpcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgHabilitacionPeriodoCalificacion{" + "hpcPk=" + hpcPk + '}';
    }


}

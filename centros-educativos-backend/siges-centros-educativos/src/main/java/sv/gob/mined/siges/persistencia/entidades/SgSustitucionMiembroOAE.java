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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumEstadoSustitucionMiembroOAE;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sustitucion_miembro_oae", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "smoPk", scope = SgSustitucionMiembroOAE.class)
@Audited
public class SgSustitucionMiembroOAE implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "smo_pk", nullable = false)
    private Long smoPk;

    @Column(name = "smo_fecha")
    private LocalDate smoFecha;
    
    @JoinColumn(name = "smo_oae_fk", referencedColumnName = "oae_pk")
    @ManyToOne
    private SgOrganismoAdministracionEscolar smoOaeFk;
    
    @Column(name = "smo_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSustitucionMiembroOAE smoEstado;

    @Column(name = "smo_numero_resolucion")
    private String smoNumeroResolucion;
    
    @Column(name = "smo_motivo_rechazo")
    private String smoMotivoRechazo;

    @Column(name = "smo_fecha_resolucion")
    private LocalDate smoFechaResolucion;

    @Column(name = "smo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime smoUltModFecha;

    @Size(max = 45)
    @Column(name = "smo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String smoUltModUsuario;

    @Column(name = "smo_version")
    @Version
    private Integer smoVersion;
    
    @OneToMany(mappedBy = "rsmSustitucionMiembroFk")
    private List<SgRelSustitucionMiembroOAE> smoRelSustitucionMiembroOAE;

    @Transient
    private Integer cantidadMiembrosaSustituir;
    
    public SgSustitucionMiembroOAE() {
    }


    public Long getSmoPk() {
        return smoPk;
    }

    public void setSmoPk(Long smoPk) {
        this.smoPk = smoPk;
    }

    public LocalDate getSmoFecha() {
        return smoFecha;
    }

    public void setSmoFecha(LocalDate smoFecha) {
        this.smoFecha = smoFecha;
    }

    public SgOrganismoAdministracionEscolar getSmoOaeFk() {
        return smoOaeFk;
    }

    public void setSmoOaeFk(SgOrganismoAdministracionEscolar smoOaeFk) {
        this.smoOaeFk = smoOaeFk;
    }

    public EnumEstadoSustitucionMiembroOAE getSmoEstado() {
        return smoEstado;
    }

    public void setSmoEstado(EnumEstadoSustitucionMiembroOAE smoEstado) {
        this.smoEstado = smoEstado;
    }

    public String getSmoNumeroResolucion() {
        return smoNumeroResolucion;
    }

    public void setSmoNumeroResolucion(String smoNumeroResolucion) {
        this.smoNumeroResolucion = smoNumeroResolucion;
    }

    public String getSmoMotivoRechazo() {
        return smoMotivoRechazo;
    }

    public void setSmoMotivoRechazo(String smoMotivoRechazo) {
        this.smoMotivoRechazo = smoMotivoRechazo;
    }

    public LocalDate getSmoFechaResolucion() {
        return smoFechaResolucion;
    }

    public void setSmoFechaResolucion(LocalDate smoFechaResolucion) {
        this.smoFechaResolucion = smoFechaResolucion;
    }
  
   
    public LocalDateTime getSmoUltModFecha() {
        return smoUltModFecha;
    }

    public void setSmoUltModFecha(LocalDateTime smoUltModFecha) {
        this.smoUltModFecha = smoUltModFecha;
    }

    public String getSmoUltModUsuario() {
        return smoUltModUsuario;
    }

    public void setSmoUltModUsuario(String smoUltModUsuario) {
        this.smoUltModUsuario = smoUltModUsuario;
    }

    public Integer getSmoVersion() {
        return smoVersion;
    }

    public void setSmoVersion(Integer smoVersion) {
        this.smoVersion = smoVersion;
    }

    public List<SgRelSustitucionMiembroOAE> getSmoRelSustitucionMiembroOAE() {
        return smoRelSustitucionMiembroOAE;
    }

    public void setSmoRelSustitucionMiembroOAE(List<SgRelSustitucionMiembroOAE> smoRelSustitucionMiembroOAE) {
        this.smoRelSustitucionMiembroOAE = smoRelSustitucionMiembroOAE;
    }

    public Integer getCantidadMiembrosaSustituir() {
        return cantidadMiembrosaSustituir;
    }

    public void setCantidadMiembrosaSustituir(Integer cantidadMiembrosaSustituir) {
        this.cantidadMiembrosaSustituir = cantidadMiembrosaSustituir;
    }
    
       @Override
    public String securityAmbitCreate() {
        return null;
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
            return CriteriaTOUtils.createORTO(
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.sedPk", o.getContext()),
                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaeSede.sedSedeAdscritaDe.sedPk", o.getContext())
            );
        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
            return null;
        } else {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "smoOaeFk.oaePk", -1L);
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.smoPk);
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
        final SgSustitucionMiembroOAE other = (SgSustitucionMiembroOAE) obj;
        if (!Objects.equals(this.smoPk, other.smoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSustitucionMiembroOAE{" + "smoPk=" + smoPk  + ", smoUltModFecha=" + smoUltModFecha + ", smoUltModUsuario=" + smoUltModUsuario + ", smoVersion=" + smoVersion + '}';
    }
    
    

}

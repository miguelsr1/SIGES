/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_organismo_administracion_escolar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "oaePk", scope = SgOrganismoAdministracionEscolar.class)
@Audited
/**
 * Entidad correspondiente a los organismos de administración escolar.
 */
public class SgOrganismoAdministracionEscolar implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oae_pk", nullable = false)
    private Long oaePk;

    @Size(max = 255)
    @Column(name = "oae_nombre", length = 255)
    private String oaeNombre;

    @JoinColumn(name = "oae_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede oaeSede;

    @Size(max = 10)
    @Column(name = "oae_acta_integracion", length = 10)
    private String oaeActaIntegracion;

    @Column(name = "oae_fecha_acta_integracion")
    private LocalDate oaeFechaActaIntegracion;

    @Column(name = "oae_fecha_vencimiento")
    private LocalDate oaeFechaVencimiento;

    @Column(name = "oae_fecha_legalizacion")
    private LocalDate oaeFechaLegalizacion;

    @Column(name = "oae_fecha_cierre")
    private LocalDate oaeFechaCierre;

    @Column(name = "oae_acuerdo_cierre")
    private String oaeAcuerdoCierre;

    @Column(name = "oae_miembros_vigentes")
    private Boolean oaeMiembrosVigentes;

    @Column(name = "oae_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoOrganismoAdministrativo oaeEstado;

    @Column(name = "oae_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime oaeUltModFecha;

    @Size(max = 45)
    @Column(name = "oae_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String oaeUltModUsuario;

    @Column(name = "oae_version")
    @Version
    private Integer oaeVersion;

    @Size(max = 20)
    @Column(name = "oae_numero_acuerdo", length = 20)
    private String oaeNumeroAcuerdo;

    @Column(name = "oae_fecha_acuerdo")
    private LocalDate oaeFechaAcuerdo;

    @Size(max = 255)
    @Column(name = "oae_ampliar_datos", length = 255)
    private String oaeAmpliarDatos;

    @Size(max = 255)
    @Column(name = "oae_motivo_rechazo", length = 255)
    private String oaeMotivoRechazo;

    public SgOrganismoAdministracionEscolar() {
    }

    public Long getOaePk() {
        return oaePk;
    }

    public void setOaePk(Long oaePk) {
        this.oaePk = oaePk;
    }

    public LocalDateTime getOaeUltModFecha() {
        return oaeUltModFecha;
    }

    public void setOaeUltModFecha(LocalDateTime oaeUltModFecha) {
        this.oaeUltModFecha = oaeUltModFecha;
    }

    public String getOaeUltModUsuario() {
        return oaeUltModUsuario;
    }

    public void setOaeUltModUsuario(String oaeUltModUsuario) {
        this.oaeUltModUsuario = oaeUltModUsuario;
    }

    public Integer getOaeVersion() {
        return oaeVersion;
    }

    public void setOaeVersion(Integer oaeVersion) {
        this.oaeVersion = oaeVersion;
    }

    public SgSede getOaeSede() {
        return oaeSede;
    }

    public void setOaeSede(SgSede oaeSede) {
        this.oaeSede = oaeSede;
    }

    public String getOaeActaIntegracion() {
        return oaeActaIntegracion;
    }

    public void setOaeActaIntegracion(String oaeActaIntegracion) {
        this.oaeActaIntegracion = oaeActaIntegracion;
    }

    public LocalDate getOaeFechaActaIntegracion() {
        return oaeFechaActaIntegracion;
    }

    public void setOaeFechaActaIntegracion(LocalDate oaeFechaActaIntegracion) {
        this.oaeFechaActaIntegracion = oaeFechaActaIntegracion;
    }

    public LocalDate getOaeFechaVencimiento() {
        return oaeFechaVencimiento;
    }

    public void setOaeFechaVencimiento(LocalDate oaeFechaVencimiento) {
        this.oaeFechaVencimiento = oaeFechaVencimiento;
    }

    public EnumEstadoOrganismoAdministrativo getOaeEstado() {
        return oaeEstado;
    }

    public void setOaeEstado(EnumEstadoOrganismoAdministrativo oaeEstado) {
        this.oaeEstado = oaeEstado;
    }

    public Boolean getOaeMiembrosVigentes() {
        return oaeMiembrosVigentes;
    }

    public void setOaeMiembrosVigentes(Boolean oaeMiembrosVigentes) {
        this.oaeMiembrosVigentes = oaeMiembrosVigentes;
    }

    public String getOaeNumeroAcuerdo() {
        return oaeNumeroAcuerdo;
    }

    public void setOaeNumeroAcuerdo(String oaeNumeroAcuerdo) {
        this.oaeNumeroAcuerdo = oaeNumeroAcuerdo;
    }

    public LocalDate getOaeFechaAcuerdo() {
        return oaeFechaAcuerdo;
    }

    public void setOaeFechaAcuerdo(LocalDate oaeFechaAcuerdo) {
        this.oaeFechaAcuerdo = oaeFechaAcuerdo;
    }

    public String getOaeAmpliarDatos() {
        return oaeAmpliarDatos;
    }

    public void setOaeAmpliarDatos(String oaeAmpliarDatos) {
        this.oaeAmpliarDatos = oaeAmpliarDatos;
    }

    public String getOaeMotivoRechazo() {
        return oaeMotivoRechazo;
    }

    public void setOaeMotivoRechazo(String oaeMotivoRechazo) {
        this.oaeMotivoRechazo = oaeMotivoRechazo;
    }

    public String getOaeNombre() {
        return oaeNombre;
    }

    public void setOaeNombre(String oaeNombre) {
        this.oaeNombre = oaeNombre;
    }

    public LocalDate getOaeFechaLegalizacion() {
        return oaeFechaLegalizacion;
    }

    public void setOaeFechaLegalizacion(LocalDate oaeFechaLegalizacion) {
        this.oaeFechaLegalizacion = oaeFechaLegalizacion;
    }

    public LocalDate getOaeFechaCierre() {
        return oaeFechaCierre;
    }

    public void setOaeFechaCierre(LocalDate oaeFechaCierre) {
        this.oaeFechaCierre = oaeFechaCierre;
    }

    public String getOaeAcuerdoCierre() {
        return oaeAcuerdoCierre;
    }

    public void setOaeAcuerdoCierre(String oaeAcuerdoCierre) {
        this.oaeAcuerdoCierre = oaeAcuerdoCierre;
    }

    @Override
    public String securityAmbitCreate() {
        return "oaeSede";
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation daoop) {
        return null;
//        if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.DEPARTAMENTAL.name())) {
//            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedDireccion.dirDepartamento.depPk", o.getContext());
//        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.SEDE.name())) {
//            return CriteriaTOUtils.createORTO(
//                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedPk", o.getContext()),
//                    CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedSedeAdscritaDe.sedPk", o.getContext())
//            );
//        } else if (o.getAmbit().equalsIgnoreCase(SeguridadAmbitos.MINED.name())) {
//            return null;
//        } else {
//            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", -1L);
//        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.oaePk);
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
        final SgOrganismoAdministracionEscolar other = (SgOrganismoAdministracionEscolar) obj;
        if (!Objects.equals(this.oaePk, other.oaePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganismoAdministracionEscolar{" + "oaePk=" + oaePk + ", oaeUltModFecha=" + oaeUltModFecha + ", oaeUltModUsuario=" + oaeUltModUsuario + ", oaeVersion=" + oaeVersion + '}';
    }

}

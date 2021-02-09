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
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumTransferenciaEstado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * 
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_transferencia_direccion_departamental", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tddPk", scope = SgTransferenciaDireccionDep.class)
@Audited
/**
 * Entidad correspondiente a las transferencias de las direccioens
 * departamentales.
 */
public class SgTransferenciaDireccionDep implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tdd_pk", nullable = false)
    private Long tddPk;
    
    @OneToOne
    @JoinColumn(name = "tdd_transferencia_fk", referencedColumnName = "tc_id")
    private SsTransferenciaComponente tddTransferenciaFk;

    @JoinColumn(name = "tdd_direccion_dep_fk", referencedColumnName = "ded_pk")
    @ManyToOne
    private SgDireccionDepartamental tddDireccionDepFk;

    @Column(name = "tdd_monto_autorizado")
    private BigDecimal tddMontoAutorizado;

    @Column(name = "tdd_monto_solicitado")
    private BigDecimal tddMontoSolicitado;

    @Column(name = "tdd_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumTransferenciaEstado tddEstado;
    
    @Column(name = "tdd_beneficiarios")
    private Integer tddBeneficiarios;

    @Column(name = "tdd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tddUltModFecha;

    @Size(max = 45)
    @Column(name = "tdd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tddUltModUsuario;

    @Column(name = "tdd_version")
    @Version
    private Integer tddVersion;
    
    @OneToMany(mappedBy = "tacTransferenciaDireccionDepFk", fetch = FetchType.LAZY)
    private List<SgTransferenciaACed> transferenciaACeds;
    

    public SgTransferenciaDireccionDep() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getTddPk() {
        return tddPk;
    }

    public void setTddPk(Long tddPk) {
        this.tddPk = tddPk;
    }

    public SsTransferenciaComponente getTddTransferenciaFk() {
        return tddTransferenciaFk;
    }

    public void setTddTransferenciaFk(SsTransferenciaComponente tddTransferenciaFk) {
        this.tddTransferenciaFk = tddTransferenciaFk;
    }

    public SgDireccionDepartamental getTddDireccionDepFk() {
        return tddDireccionDepFk;
    }

    public void setTddDireccionDepFk(SgDireccionDepartamental tddDireccionDepFk) {
        this.tddDireccionDepFk = tddDireccionDepFk;
    }

    public BigDecimal getTddMontoAutorizado() {
        return tddMontoAutorizado;
    }

    public void setTddMontoAutorizado(BigDecimal tddMontoAutorizado) {
        this.tddMontoAutorizado = tddMontoAutorizado;
    }

    public BigDecimal getTddMontoSolicitado() {
        return tddMontoSolicitado;
    }

    public void setTddMontoSolicitado(BigDecimal tddMontoSolicitado) {
        this.tddMontoSolicitado = tddMontoSolicitado;
    }

    public EnumTransferenciaEstado getTddEstado() {
        return tddEstado;
    }

    public void setTddEstado(EnumTransferenciaEstado tddEstado) {
        this.tddEstado = tddEstado;
    }

    public LocalDateTime getTddUltModFecha() {
        return tddUltModFecha;
    }

    public void setTddUltModFecha(LocalDateTime tddUltModFecha) {
        this.tddUltModFecha = tddUltModFecha;
    }

    public String getTddUltModUsuario() {
        return tddUltModUsuario;
    }

    public void setTddUltModUsuario(String tddUltModUsuario) {
        this.tddUltModUsuario = tddUltModUsuario;
    }

    public Integer getTddVersion() {
        return tddVersion;
    }

    public void setTddVersion(Integer tddVersion) {
        this.tddVersion = tddVersion;
    }

    public Integer getTddBeneficiarios() {
        return tddBeneficiarios;
    }

    public void setTddBeneficiarios(Integer tddBeneficiarios) {
        this.tddBeneficiarios = tddBeneficiarios;
    }    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tddPk);
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
        final SgTransferenciaDireccionDep other = (SgTransferenciaDireccionDep) obj;
        if (!Objects.equals(this.tddPk, other.tddPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTransferenciaDireccionDep{" + "tddPk=" + tddPk + ", tddTransferenciaFk=" + tddTransferenciaFk + ", tddDireccionDepFk=" + tddDireccionDepFk + ", tddMontoAutorizado=" + tddMontoAutorizado + ", tddMontoSolicitado=" + tddMontoSolicitado + ", tddEstado=" + tddEstado + ", tddUltModFecha=" + tddUltModFecha + ", tddUltModUsuario=" + tddUltModUsuario + ", tddVersion=" + tddVersion + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "tddDireccionDepFk.dedDepartamentoFk.depPk", o.getContext());
        } else {
            return null;
        }
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTransferenciaEstado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsTransferenciaComponente;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad que almacena las trasnferencias a las sedes educativas.
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_transferencias_a_ced", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tacPk", scope = SgTransferenciaACed.class)
@Audited
public class SgTransferenciaACed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tac_pk", nullable = false)
    private Long tacPk;

    @JoinColumn(name = "tac_ced_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede tacCedFk;

    @JoinColumn(name = "tac_transferencia_fk", referencedColumnName = "tc_id")
    @ManyToOne
    private SsTransferenciaComponente tacTransferenciaFk;

    @JoinColumn(name = "tac_transferencia_direccion_dep_fk", referencedColumnName = "tdd_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk;

    @OneToOne(mappedBy = "recTransferencia")
    private SgRecibos recibo;

    @Column(name = "tac_monto_autorizado")
    private BigDecimal tacMontoAutorizado;

    @Column(name = "tac_monto_solicitado")
    private BigDecimal tacMontoSolicitado;

    @Column(name = "tac_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumTransferenciaEstado tacEstado;
    
    @Column(name = "tac_beneficiarios")
    private Integer tacBeneficiarios;

    @Column(name = "tac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tacUltModFecha;

    @Size(max = 45)
    @Column(name = "tac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tacUltModUsuario;

    @Column(name = "tac_version")
    @Version
    private Integer tacVersion;

    @OneToOne(mappedBy = "rfcTransferenciaCedFk", optional = true)
    private SgReqFondoCed tacReqFondoCed;
    
    public SgTransferenciaACed() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public SgSede getTacCedFk() {
        return tacCedFk;
    }

    public void setTacCedFk(SgSede tacCedFk) {
        this.tacCedFk = tacCedFk;
    }

    public SgTransferenciaDireccionDep getTacTransferenciaDireccionDepFk() {
        return tacTransferenciaDireccionDepFk;
    }

    public void setTacTransferenciaDireccionDepFk(SgTransferenciaDireccionDep tacTransferenciaDireccionDepFk) {
        this.tacTransferenciaDireccionDepFk = tacTransferenciaDireccionDepFk;
    }

    public SsTransferenciaComponente getTacTransferenciaFk() {
        return tacTransferenciaFk;
    }

    public void setTacTransferenciaFk(SsTransferenciaComponente tacTransferenciaFk) {
        this.tacTransferenciaFk = tacTransferenciaFk;
    }

    public SgRecibos getRecibo() {
        return recibo;
    }

    public void setRecibo(SgRecibos recibo) {
        this.recibo = recibo;
    }

    public BigDecimal getTacMontoAutorizado() {
        return tacMontoAutorizado;
    }

    public void setTacMontoAutorizado(BigDecimal tacMontoAutorizado) {
        this.tacMontoAutorizado = tacMontoAutorizado;
    }

    public BigDecimal getTacMontoSolicitado() {
        return tacMontoSolicitado;
    }

    public void setTacMontoSolicitado(BigDecimal tacMontoSolicitado) {
        this.tacMontoSolicitado = tacMontoSolicitado;
    }

    public EnumTransferenciaEstado getTacEstado() {
        return tacEstado;
    }

    public void setTacEstado(EnumTransferenciaEstado tacEstado) {
        this.tacEstado = tacEstado;
    }

    public LocalDateTime getTacUltModFecha() {
        return tacUltModFecha;
    }

    public void setTacUltModFecha(LocalDateTime tacUltModFecha) {
        this.tacUltModFecha = tacUltModFecha;
    }

    public String getTacUltModUsuario() {
        return tacUltModUsuario;
    }

    public void setTacUltModUsuario(String tacUltModUsuario) {
        this.tacUltModUsuario = tacUltModUsuario;
    }

    public Integer getTacVersion() {
        return tacVersion;
    }

    public void setTacVersion(Integer tacVersion) {
        this.tacVersion = tacVersion;
    }

    public SgReqFondoCed getTacReqFondoCed() {
        return tacReqFondoCed;
    }

    public void setTacReqFondoCed(SgReqFondoCed tacReqFondoCed) {
        this.tacReqFondoCed = tacReqFondoCed;
    }

    public Integer getTacBeneficiarios() {
        return tacBeneficiarios;
    }

    public void setTacBeneficiarios(Integer tacBeneficiarios) {
        this.tacBeneficiarios = tacBeneficiarios;
    }
    
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tacPk);
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
        final SgTransferenciaACed other = (SgTransferenciaACed) obj;
        if (!Objects.equals(this.tacPk, other.tacPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTransferenciaACed{" + "tacPk=" + tacPk + '}';
    }

}

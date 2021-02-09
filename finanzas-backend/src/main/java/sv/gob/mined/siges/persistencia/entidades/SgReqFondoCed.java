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
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_req_fond_ced", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rfcPk", scope = SgReqFondoCed.class)
@Audited
/**
 * Entidad correspondiente a los requerimientos de fondos.
 */
public class SgReqFondoCed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rfc_pk", nullable = false)
    private Long rfcPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfc_transferencia_ced_fk", referencedColumnName = "tac_pk")
    private SgTransferenciaACed rfcTransferenciaCedFk;

    @JoinColumn(name = "rfc_sol_transferencia_fk", referencedColumnName = "str_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgRequerimientoFondo rfcSolTransferenciaFk;
    
    @Column(name = "rfc_monto")
    private BigDecimal rfcMonto;

    @Column(name = "rfc_habilitado")
    @AtributoHabilitado
    private Boolean rfcHabilitado;

    @Column(name = "rfc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rfcUltModFecha;

    @Size(max = 45)
    @Column(name = "rfc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rfcUltModUsuario;

    @Column(name = "rfc_version")
    @Version
    private Integer rfcVersion;

    public SgReqFondoCed() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Long getRfcPk() {
        return rfcPk;
    }

    public void setRfcPk(Long rfcPk) {
        this.rfcPk = rfcPk;
    }

    public SgTransferenciaACed getRfcTransferenciaCedFk() {
        return rfcTransferenciaCedFk;
    }

    public void setRfcTransferenciaCedFk(SgTransferenciaACed rfcTransferenciaCedFk) {
        this.rfcTransferenciaCedFk = rfcTransferenciaCedFk;
    }

    public SgRequerimientoFondo getRfcSolTransferenciaFk() {
        return rfcSolTransferenciaFk;
    }

    public void setRfcSolTransferenciaFk(SgRequerimientoFondo rfcSolTransferenciaFk) {
        this.rfcSolTransferenciaFk = rfcSolTransferenciaFk;
    }
    
    public BigDecimal getRfcMonto() {
        return rfcMonto;
    }

    public void setRfcMonto(BigDecimal rfcMonto) {
        this.rfcMonto = rfcMonto;
    }

    public Boolean getRfcHabilitado() {
        return rfcHabilitado;
    }

    public void setRfcHabilitado(Boolean rfcHabilitado) {
        this.rfcHabilitado = rfcHabilitado;
    }

    public LocalDateTime getRfcUltModFecha() {
        return rfcUltModFecha;
    }

    public void setRfcUltModFecha(LocalDateTime rfcUltModFecha) {
        this.rfcUltModFecha = rfcUltModFecha;
    }

    public String getRfcUltModUsuario() {
        return rfcUltModUsuario;
    }

    public void setRfcUltModUsuario(String rfcUltModUsuario) {
        this.rfcUltModUsuario = rfcUltModUsuario;
    }

    public Integer getRfcVersion() {
        return rfcVersion;
    }

    public void setRfcVersion(Integer rfcVersion) {
        this.rfcVersion = rfcVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.rfcPk);
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
        final SgReqFondoCed other = (SgReqFondoCed) obj;
        if (!Objects.equals(this.rfcPk, other.rfcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgReqFondoCed{" + "rfcPk=" + rfcPk + ", rfcTransferenciaCedFk=" + rfcTransferenciaCedFk + ", rfcSolTransferenciaFk=" + rfcSolTransferenciaFk + ", rfcMonto=" + rfcMonto + ", rfcHabilitado=" + rfcHabilitado + ", rfcUltModFecha=" + rfcUltModFecha + ", rfcUltModUsuario=" + rfcUltModUsuario + ", rfcVersion=" + rfcVersion + '}';
    }

}

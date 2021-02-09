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
import sv.gob.mined.siges.enumerados.EnumTipoMovimientoCajaChica;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_movimiento_caja_chica", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mccPk", scope = SgMovimientoCajaChica.class)
@Audited
/**
 * Entidad correspondiente a los movimientos de la caja chica de los centros
 * educativos.
 */
public class SgMovimientoCajaChica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mcc_pk", nullable = false)
    private Long mccPk;

    @JoinColumn(name = "mcc_cuenta_fk", referencedColumnName = "bcc_pk", nullable = false)
    @ManyToOne
    private SgCajaChica mccCuentaFK;

    @Column(name = "mcc_fecha", nullable = false)
    private LocalDateTime mccFecha;

    @Size(max = 255)
    @Column(name = "mcc_detalle", length = 255, nullable = false)
    @AtributoNormalizable
    private String mccDetalle;

    @Column(name = "mcc_monto", nullable = false)
    private BigDecimal mccMonto;

    @Column(name = "mcc_saldo", nullable = false)
    private BigDecimal mccSaldo;

    @Column(name = "mcc_tipo", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento mccTipoMovimiento;

    @Column(name = "mcc_tipo_ingreso")
    @Enumerated(EnumType.STRING)
    private EnumTipoMovimientoCajaChica mccTipoIngreso;

    @Column(name = "mcc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mccUltModFecha;

    @Size(max = 45)
    @Column(name = "mcc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mccUltModUsuario;

    @Column(name = "mcc_version")
    @Version
    private Integer mccVersion;

    @OneToOne(mappedBy = "pgsMovimientoCCFk")
    private SgPago mccPagoFk;
    
    @OneToOne(mappedBy = "mlqMovimientoCcPk")
    private SgMovimientoLiquidacion movimientoLiquidacion;

    @JoinColumn(name = "mcc_chequera_fk", referencedColumnName = "che_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgChequera mccChequeraFK;

    @Column(name = "mcc_cheque")
    private Integer mccCheque;

    public SgMovimientoCajaChica() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getMccPk() {
        return mccPk;
    }

    public void setMccPk(Long mccPk) {
        this.mccPk = mccPk;
    }

    public SgCajaChica getMccCuentaFK() {
        return mccCuentaFK;
    }

    public void setMccCuentaFK(SgCajaChica mccCuentaFK) {
        this.mccCuentaFK = mccCuentaFK;
    }

    public LocalDateTime getMccFecha() {
        return mccFecha;
    }

    public void setMccFecha(LocalDateTime mccFecha) {
        this.mccFecha = mccFecha;
    }

    public String getMccDetalle() {
        return mccDetalle;
    }

    public void setMccDetalle(String mccDetalle) {
        this.mccDetalle = mccDetalle;
    }

    public BigDecimal getMccMonto() {
        return mccMonto;
    }

    public void setMccMonto(BigDecimal mccMonto) {
        this.mccMonto = mccMonto;
    }

    public BigDecimal getMccSaldo() {
        return mccSaldo;
    }

    public void setMccSaldo(BigDecimal mccSaldo) {
        this.mccSaldo = mccSaldo;
    }

    public TipoMovimiento getMccTipoMovimiento() {
        return mccTipoMovimiento;
    }

    public void setMccTipoMovimiento(TipoMovimiento mccTipoMovimiento) {
        this.mccTipoMovimiento = mccTipoMovimiento;
    }

    public LocalDateTime getMccUltModFecha() {
        return mccUltModFecha;
    }

    public void setMccUltModFecha(LocalDateTime mccUltModFecha) {
        this.mccUltModFecha = mccUltModFecha;
    }

    public String getMccUltModUsuario() {
        return mccUltModUsuario;
    }

    public void setMccUltModUsuario(String mccUltModUsuario) {
        this.mccUltModUsuario = mccUltModUsuario;
    }

    public Integer getMccVersion() {
        return mccVersion;
    }

    public void setMccVersion(Integer mccVersion) {
        this.mccVersion = mccVersion;
    }

    public SgPago getMccPagoFk() {
        return mccPagoFk;
    }

    public void setMccPagoFk(SgPago mccPagoFk) {
        this.mccPagoFk = mccPagoFk;
    }

    public SgChequera getMccChequeraFK() {
        return mccChequeraFK;
    }

    public void setMccChequeraFK(SgChequera mccChequeraFK) {
        this.mccChequeraFK = mccChequeraFK;
    }

    public Integer getMccCheque() {
        return mccCheque;
    }

    public void setMccCheque(Integer mccCheque) {
        this.mccCheque = mccCheque;
    }

    public EnumTipoMovimientoCajaChica getMccTipoIngreso() {
        return mccTipoIngreso;
    }

    public void setMccTipoIngreso(EnumTipoMovimientoCajaChica mccTipoIngreso) {
        this.mccTipoIngreso = mccTipoIngreso;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.mccPk);
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
        final SgMovimientoCajaChica other = (SgMovimientoCajaChica) obj;
        if (!Objects.equals(this.mccPk, other.mccPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMovimientoCajaChica{" + "mccPk=" + mccPk + " ,mccMonto= " + mccMonto + " ,mccSaldo= " + mccSaldo + ", mccUltModFecha=" + mccUltModFecha + ", mccUltModUsuario=" + mccUltModUsuario + ", mccVersion=" + mccVersion + '}';
    }

}

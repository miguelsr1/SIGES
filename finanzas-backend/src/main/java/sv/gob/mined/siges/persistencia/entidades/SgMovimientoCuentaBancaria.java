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
import javax.persistence.CascadeType;
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
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente a los movimientos de las cuentas bancarias
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_movimiento_cuenta_bancaria", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mcbPk", scope = SgMovimientoCuentaBancaria.class)
@Audited
/**
 * Entidad correspondiente a los movimientos de las cuentas bancarias.
 */
public class SgMovimientoCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mcb_pk", nullable = false)
    private Long mcbPk;

    @JoinColumn(name = "mcb_cuenta_fk", referencedColumnName = "cbc_pk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SgCuentasBancarias mcbCuentaFK;

    @Column(name = "mcb_fecha", nullable = false)
    private LocalDateTime mcbFecha;

    @Size(max = 255)
    @Column(name = "mcb_detalle", length = 255, nullable = false)
    @AtributoNormalizable
    private String mcbDetalle;

    @Column(name = "mcb_monto", nullable = false)
    private BigDecimal mcbMonto;

    @Column(name = "mcb_saldo", nullable = false)
    private BigDecimal mcbSaldo;

    @Column(name = "mcb_tipo", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento mcbTipoMovimiento;

    @Column(name = "mcb_tipo_retiro", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumTipoRetiroMovimientoCB mcbTipoRetiro;

    @Column(name = "mcb_estado_liquidacion", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoLiquidacion mcbEstadoLiquidacion;

    @NotAudited
    @OneToOne(mappedBy = "pgsMovimientoCBFk", fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.MERGE})
    private SgPago mcbPagoFk;
    //private List<SgPago> mcbPagoFk;

    @OneToOne(mappedBy = "mlqMovimientoPk")
    private SgMovimientoLiquidacion movimientoLiquidacion;

    @OneToOne(mappedBy = "mloMovimientoFk")
    private SgMovimientoLiquidacionOtro movimientoLiquidacionOtro;

    @JoinColumn(name = "mcb_mov_fuente_ingresos_fk", referencedColumnName = "mov_pk", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private SgMovimientos mcbMovFuenteIngresosFk;

    @Column(name = "mcb_cheque_cb", nullable = false)
    private Integer mcbChequeCb;

    @Column(name = "mcb_cheque_cobrado")
    private Boolean mcbChequeCobrado;

    @Column(name = "mcb_aplica_conciliacion")
    private Boolean mcbAplicaConciliacion;

    @Column(name = "mcb_cheque_anulado")
    private Boolean mcbChequeAnulado;

    @JoinColumn(name = "mcb_desembolso_ced_fk", referencedColumnName = "dce_pk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SgDesembolsoCed mcbDesembolsoCedFk;

    @Size(max = 45)
    @Column(name = "mcb_transaccion", length = 45)
    private String mcbTransaccion;

    @Size(max = 500)
    @Column(name = "mcb_proveedor", length = 500)
    private String mcbProveedor;

    @Column(name = "mcb_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mcbUltModFecha;

    @Size(max = 45)
    @Column(name = "mcb_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mcbUltModUsuario;

    @Column(name = "mcb_version")
    @Version
    private Integer mcbVersion;

    public SgMovimientoCuentaBancaria() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        mcbSaldo = (mcbSaldo == null) ? BigDecimal.ZERO : mcbSaldo;
    }

// <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getMcbPk() {
        return mcbPk;
    }

    public void setMcbPk(Long mcbPk) {
        this.mcbPk = mcbPk;
    }

    public Integer getMcbChequeCb() {
        return mcbChequeCb;
    }

    public void setMcbChequeCb(Integer mcbChequeCb) {
        this.mcbChequeCb = mcbChequeCb;
    }

    public SgCuentasBancarias getMcbCuentaFK() {
        return mcbCuentaFK;
    }

    public void setMcbCuentaFK(SgCuentasBancarias mcbCuentaFK) {
        this.mcbCuentaFK = mcbCuentaFK;
    }

    public LocalDateTime getMcbFecha() {
        return mcbFecha;
    }

    public void setMcbFecha(LocalDateTime mcbFecha) {
        this.mcbFecha = mcbFecha;
    }

    public String getMcbDetalle() {
        return mcbDetalle;
    }

    public void setMcbDetalle(String mcbDetalle) {
        this.mcbDetalle = mcbDetalle;
    }

    public BigDecimal getMcbMonto() {
        mcbMonto = mcbMonto == null ? BigDecimal.ZERO : mcbMonto;
        return mcbMonto;
    }

    public void setMcbMonto(BigDecimal mcbMonto) {
        this.mcbMonto = mcbMonto;
    }

    public BigDecimal getMcbSaldo() {
        return mcbSaldo;
    }

    public void setMcbSaldo(BigDecimal mcbSaldo) {
        this.mcbSaldo = mcbSaldo;
    }

    public TipoMovimiento getMcbTipoMovimiento() {
        return mcbTipoMovimiento;
    }

    public void setMcbTipoMovimiento(TipoMovimiento mcbTipoMovimiento) {
        this.mcbTipoMovimiento = mcbTipoMovimiento;
    }

    public LocalDateTime getMcbUltModFecha() {
        return mcbUltModFecha;
    }

    public void setMcbUltModFecha(LocalDateTime mcbUltModFecha) {
        this.mcbUltModFecha = mcbUltModFecha;
    }

    public String getMcbUltModUsuario() {
        return mcbUltModUsuario;
    }

    public void setMcbUltModUsuario(String mcbUltModUsuario) {
        this.mcbUltModUsuario = mcbUltModUsuario;
    }

    public Integer getMcbVersion() {
        return mcbVersion;
    }

    public EnumTipoRetiroMovimientoCB getMcbTipoRetiro() {
        return mcbTipoRetiro;
    }

    public void setMcbTipoRetiro(EnumTipoRetiroMovimientoCB mcbTipoRetiro) {
        this.mcbTipoRetiro = mcbTipoRetiro;
    }

    public SgPago getMcbPagoFk() {
        return mcbPagoFk;
    }

    public void setMcbPagoFk(SgPago mcbPagoFk) {
        this.mcbPagoFk = mcbPagoFk;
    }

    public Boolean getMcbChequeCobrado() {
        return mcbChequeCobrado;
    }

    public void setMcbChequeCobrado(Boolean mcbChequeCobrado) {
        this.mcbChequeCobrado = mcbChequeCobrado;
    }

    public SgMovimientos getMcbMovFuenteIngresosFk() {
        return mcbMovFuenteIngresosFk;
    }

    public void setMcbMovFuenteIngresosFk(SgMovimientos mcbMovFuenteIngresosFk) {
        this.mcbMovFuenteIngresosFk = mcbMovFuenteIngresosFk;
    }

    public void setMcbVersion(Integer mcbVersion) {
        this.mcbVersion = mcbVersion;
    }

    public String getMcbTransaccion() {
        return mcbTransaccion;
    }

    public void setMcbTransaccion(String mcbTransaccion) {
        this.mcbTransaccion = mcbTransaccion;
    }

    public SgDesembolsoCed getMcbDesembolsoCedFk() {
        return mcbDesembolsoCedFk;
    }

    public void setMcbDesembolsoCedFk(SgDesembolsoCed mcbDesembolsoCedFk) {
        this.mcbDesembolsoCedFk = mcbDesembolsoCedFk;
    }

    public EnumEstadoLiquidacion getMcbEstadoLiquidacion() {
        return mcbEstadoLiquidacion;
    }

    public void setMcbEstadoLiquidacion(EnumEstadoLiquidacion mcbEstadoLiquidacion) {
        this.mcbEstadoLiquidacion = mcbEstadoLiquidacion;
    }

    public SgMovimientoLiquidacion getMovimientoLiquidacion() {
        return movimientoLiquidacion;
    }

    public void setMovimientoLiquidacion(SgMovimientoLiquidacion movimientoLiquidacion) {
        this.movimientoLiquidacion = movimientoLiquidacion;
    }

    public Boolean getMcbAplicaConciliacion() {
        return mcbAplicaConciliacion;
    }

    public void setMcbAplicaConciliacion(Boolean mcbAplicaConciliacion) {
        this.mcbAplicaConciliacion = mcbAplicaConciliacion;
    }

    public Boolean getMcbChequeAnulado() {
        return mcbChequeAnulado;
    }

    public void setMcbChequeAnulado(Boolean mcbChequeAnulado) {
        this.mcbChequeAnulado = mcbChequeAnulado;
    }

    public String getMcbProveedor() {
        return mcbProveedor;
    }

    public void setMcbProveedor(String mcbProveedor) {
        this.mcbProveedor = mcbProveedor;
    }

    public SgMovimientoLiquidacionOtro getMovimientoLiquidacionOtro() {
        return movimientoLiquidacionOtro;
    }

    public void setMovimientoLiquidacionOtro(SgMovimientoLiquidacionOtro movimientoLiquidacionOtro) {
        this.movimientoLiquidacionOtro = movimientoLiquidacionOtro;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mcbPk);
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
        final SgMovimientoCuentaBancaria other = (SgMovimientoCuentaBancaria) obj;
        if (!Objects.equals(this.mcbPk, other.mcbPk)) {
            return false;
        }
        return true;
    }      // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "SgMovimientoCuentaBancaria{" + "mcbPk=" + mcbPk + ", mcbUltModFecha=" + mcbUltModFecha + ", mcbUltModUsuario=" + mcbUltModUsuario + ", mcbVersion=" + mcbVersion + '}';
    }
    // </editor-fold>
}

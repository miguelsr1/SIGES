/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mcbPk", scope = SgMovimientoCuentaBancaria.class)
public class SgMovimientoCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mcbPk;

    private SgCuentasBancarias mcbCuentaFK;

    private LocalDateTime mcbFecha;

    private String mcbDetalle;

    private BigDecimal mcbMonto;

    private BigDecimal mcbSaldo;

    private BigDecimal mcbSaldoFinal;

    private TipoMovimiento mcbTipoMovimiento;

    private SgMovimientos mcbMovFuenteIngresosFk;

    private SgPago mcbPagoFk;

    private Integer mcbChequeCb;

    private Boolean mcbChequeCobrado;

    private SgDesembolsoCed mcbDesembolsoCedFk;

    private String mcbTransaccion;

    private LocalDateTime mcbUltModFecha;

    private String mcbUltModUsuario;

    private Integer mcbVersion;

    private EnumTipoRetiroMovimientoCB mcbTipoRetiro;

    private EnumEstadoLiquidacion mcbEstadoLiquidacion;

    private SgMovimientoLiquidacion movimientoLiquidacion;

    private SgMovimientoLiquidacionOtro movimientoLiquidacionOtro;

    private Boolean seleccionado;

    private Boolean correcto;

    private Boolean noCorrecto;

    private String motivo;

    private Boolean mcbAplicaConciliacion;

    private Boolean mcbChequeAnulado;

    private String mcbProveedor;

    public SgMovimientoCuentaBancaria() {
        mcbMonto = BigDecimal.ZERO;
    }

    @JsonIgnore
    public String getDatosChequePagoTooltip() {
        if (this.mcbPagoFk != null) {
            return "Factura Número : " + this.mcbPagoFk.getPgsFactura().getFacNumero();
        }
        return null;
    }

    @JsonIgnore
    public String getDatosChequeRetiroTooltip() {
        if (this.mcbTipoMovimiento.equals(TipoMovimiento.DEBE)) {
            return this.mcbTipoMovimiento.DEBE.getText();
        }
        return null;
    }

// <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Long getMcbPk() {
        return mcbPk;
    }

    public void setMcbPk(Long mcbPk) {
        this.mcbPk = mcbPk;
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

    public void setMcbVersion(Integer mcbVersion) {
        this.mcbVersion = mcbVersion;
    }

    public EnumTipoRetiroMovimientoCB getMcbTipoRetiro() {
        return mcbTipoRetiro;
    }

    public void setMcbTipoRetiro(EnumTipoRetiroMovimientoCB mcbTipoRetiro) {
        this.mcbTipoRetiro = mcbTipoRetiro;
    }

    public Integer getMcbChequeCb() {
        return mcbChequeCb;
    }

    public void setMcbChequeCb(Integer mcbChequeCb) {
        this.mcbChequeCb = mcbChequeCb;
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

    public SgPago getMcbPagoFk() {
        return mcbPagoFk;
    }

    public void setMcbPagoFk(SgPago mcbPagoFk) {
        this.mcbPagoFk = mcbPagoFk;
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

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public Boolean getCorrecto() {
        return correcto;
    }

    public void setCorrecto(Boolean correcto) {
        this.correcto = correcto;
    }

    public Boolean getNoCorrecto() {
        return noCorrecto;
    }

    public void setNoCorrecto(Boolean noCorrecto) {
        this.noCorrecto = noCorrecto;
    }

    public SgMovimientoLiquidacionOtro getMovimientoLiquidacionOtro() {
        return movimientoLiquidacionOtro;
    }

    public void setMovimientoLiquidacionOtro(SgMovimientoLiquidacionOtro movimientoLiquidacionOtro) {
        this.movimientoLiquidacionOtro = movimientoLiquidacionOtro;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public BigDecimal getMcbSaldoFinal() {
        return mcbSaldoFinal;
    }

    public void setMcbSaldoFinal(BigDecimal mcbSaldoFinal) {
        this.mcbSaldoFinal = mcbSaldoFinal;
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mcbPk != null ? mcbPk.hashCode() : 0);
        return hash;
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
        return "com.sofis.entidades.SgMovimientoCuentaBancaria[ mcbPk=" + mcbPk + " ]";
    }
    // </editor-fold>

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumTipoMovimientoCajaChica;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mccPk", scope = SgMovimientoCajaChica.class)
public class SgMovimientoCajaChica implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long mccPk;

    private SgCajaChica mccCuentaFK;

    private LocalDateTime mccFecha;

    private String mccDetalle;

    private BigDecimal mccMonto;

    private BigDecimal mccSaldo;

    private BigDecimal mccSaldoFinal;

    private TipoMovimiento mccTipoMovimiento;

    private EnumTipoMovimientoCajaChica mccTipoIngreso;

    private LocalDateTime mccUltModFecha;

    private String mccUltModUsuario;

    private Integer mccVersion;

    private SgPago mccPagoFk;

    private SgChequera mccChequeraFK;

    private Integer mccCheque;

    public SgMovimientoCajaChica() {
        mccMonto = BigDecimal.ZERO;
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

    public BigDecimal getMccSaldoFinal() {
        return mccSaldoFinal;
    }

    public void setMccSaldoFinal(BigDecimal mccSaldoFinal) {
        this.mccSaldoFinal = mccSaldoFinal;
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
        int hash = 0;
        hash += (mccPk != null ? mccPk.hashCode() : 0);
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
        final SgMovimientoCajaChica other = (SgMovimientoCajaChica) obj;
        if (!Objects.equals(this.mccPk, other.mccPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgMovimientoCajaChica[ mccPk=" + mccPk + " ]";
    }

}

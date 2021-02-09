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
import sv.gob.mined.siges.web.enumerados.EnumModoPago;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pgsPk", scope = SgPago.class)
public class SgPago implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pgsPk;

    private SgFactura pgsFactura;

    private EnumModoPago pgsModoPago;

    private SgMovimientoCuentaBancaria pgsMovimientoCBFk;

    private SgMovimientoCajaChica pgsMovimientoCCFk;

    private Integer pgsNumeroCheque;

    private LocalDateTime pgsFechaPago;

    private BigDecimal pgsImporte;

    private String pgsComentario;

    private LocalDateTime pgsUltModFecha;

    private String pgsUltModUsuario;

    private Integer pgsVersion;

    private SgCuentasBancarias pgsCuentaBancaria;

    private SgCajaChica pgsCuentaBancariaCC;

    private SgChequera pgsChequeraFk;

    public SgPago() {
    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getPgsPk() {
        return pgsPk;
    }

    public void setPgsPk(Long pgsPk) {
        this.pgsPk = pgsPk;
    }

    public SgFactura getPgsFactura() {
        return pgsFactura;
    }

    public void setPgsFactura(SgFactura pgsFactura) {
        this.pgsFactura = pgsFactura;
    }

    public EnumModoPago getPgsModoPago() {
        return pgsModoPago;
    }

    public void setPgsModoPago(EnumModoPago pgsModoPago) {
        this.pgsModoPago = pgsModoPago;
    }

    public SgMovimientoCuentaBancaria getPgsMovimientoCBFk() {
        return pgsMovimientoCBFk;
    }

    public void setPgsMovimientoCBFk(SgMovimientoCuentaBancaria pgsMovimientoCBFk) {
        this.pgsMovimientoCBFk = pgsMovimientoCBFk;
    }

    public SgMovimientoCajaChica getPgsMovimientoCCFk() {
        return pgsMovimientoCCFk;
    }

    public void setPgsMovimientoCCFk(SgMovimientoCajaChica pgsMovimientoCCFk) {
        this.pgsMovimientoCCFk = pgsMovimientoCCFk;
    }

    public Integer getPgsNumeroCheque() {
        return pgsNumeroCheque;
    }

    public void setPgsNumeroCheque(Integer pgsNumeroCheque) {
        this.pgsNumeroCheque = pgsNumeroCheque;
    }

    public LocalDateTime getPgsFechaPago() {
        return pgsFechaPago;
    }

    public void setPgsFechaPago(LocalDateTime pgsFechaPago) {
        this.pgsFechaPago = pgsFechaPago;
    }

    public BigDecimal getPgsImporte() {
        return pgsImporte;
    }

    public void setPgsImporte(BigDecimal pgsImporte) {
        this.pgsImporte = pgsImporte;
    }

    public String getPgsComentario() {
        return pgsComentario;
    }

    public void setPgsComentario(String pgsComentario) {
        this.pgsComentario = pgsComentario;
    }

    public LocalDateTime getPgsUltModFecha() {
        return pgsUltModFecha;
    }

    public void setPgsUltModFecha(LocalDateTime pgsUltModFecha) {
        this.pgsUltModFecha = pgsUltModFecha;
    }

    public String getPgsUltModUsuario() {
        return pgsUltModUsuario;
    }

    public void setPgsUltModUsuario(String pgsUltModUsuario) {
        this.pgsUltModUsuario = pgsUltModUsuario;
    }

    public Integer getPgsVersion() {
        return pgsVersion;
    }

    public void setPgsVersion(Integer pgsVersion) {
        this.pgsVersion = pgsVersion;
    }

    public SgCuentasBancarias getPgsCuentaBancaria() {
        return pgsCuentaBancaria;
    }

    public void setPgsCuentaBancaria(SgCuentasBancarias pgsCuentaBancaria) {
        this.pgsCuentaBancaria = pgsCuentaBancaria;
    }

    public SgChequera getPgsChequeraFk() {
        return pgsChequeraFk;
    }

    public void setPgsChequeraFk(SgChequera pgsChequeraFk) {
        this.pgsChequeraFk = pgsChequeraFk;
    }

    public SgCajaChica getPgsCuentaBancariaCC() {
        return pgsCuentaBancariaCC;
    }

    public void setPgsCuentaBancariaCC(SgCajaChica pgsCuentaBancariaCC) {
        this.pgsCuentaBancariaCC = pgsCuentaBancariaCC;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pgsPk != null ? pgsPk.hashCode() : 0);
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
        final SgPago other = (SgPago) obj;
        if (!Objects.equals(this.pgsPk, other.pgsPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPago[ pgsPk=" + pgsPk + " ]";
    }
    // </editor-fold>
}

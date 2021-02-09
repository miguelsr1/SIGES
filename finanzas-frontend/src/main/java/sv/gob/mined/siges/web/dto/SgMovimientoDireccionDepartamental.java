/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mddPk", scope = SgMovimientoDireccionDepartamental.class)
public class SgMovimientoDireccionDepartamental implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mddPk;

    private SgCuentasBancariasDD mddCuentaFK;
    
    private SgDetalleDesembolso mddDetDesembolsoFk;
    
    private String mddTransaccion;
    
    private LocalDateTime mddFecha;
    
    private String mddDetalle;
    
    private BigDecimal mddMonto;
    
    private BigDecimal mddSaldo;
    
    private TipoMovimiento mddTipoMovimiento;

    private LocalDateTime mddUltModFecha;

    private String mddUltModUsuario;

    private Integer mddVersion;
    
    
    public SgMovimientoDireccionDepartamental() {
        mddMonto = BigDecimal.ZERO;
    }

    public Long getMddPk() {
        return mddPk;
    }

    public void setMddPk(Long mddPk) {
        this.mddPk = mddPk;
    }

    public SgCuentasBancariasDD getMddCuentaFK() {
        return mddCuentaFK;
    }

    public void setMddCuentaFK(SgCuentasBancariasDD mddCuentaFK) {
        this.mddCuentaFK = mddCuentaFK;
    }

    public SgDetalleDesembolso getMddDetDesembolsoFk() {
        return mddDetDesembolsoFk;
    }

    public String getMddTransaccion() {
        return mddTransaccion;
    }

    public void setMddTransaccion(String mddTransaccion) {
        this.mddTransaccion = mddTransaccion;
    }
   

    public void setMddDetDesembolsoFk(SgDetalleDesembolso mddDetDesembolsoFk) {
        this.mddDetDesembolsoFk = mddDetDesembolsoFk;
    }
    
    public LocalDateTime getMddFecha() {
        return mddFecha;
    }

    public void setMddFecha(LocalDateTime mddFecha) {
        this.mddFecha = mddFecha;
    }

    public String getMddDetalle() {
        return mddDetalle;
    }

    public void setMddDetalle(String mddDetalle) {
        this.mddDetalle = mddDetalle;
    }

    public BigDecimal getMddMonto() {
        return mddMonto;
    }

    public void setMddMonto(BigDecimal mddMonto) {
        this.mddMonto = mddMonto;
    }

    public BigDecimal getMddSaldo() {
        return mddSaldo;
    }

    public void setMddSaldo(BigDecimal mddSaldo) {
        this.mddSaldo = mddSaldo;
    }
    
    public TipoMovimiento getMddTipoMovimiento() {
        return mddTipoMovimiento;
    }

    public void setMddTipoMovimiento(TipoMovimiento mddTipoMovimiento) {
        this.mddTipoMovimiento = mddTipoMovimiento;
    }

    public LocalDateTime getMddUltModFecha() {
        return mddUltModFecha;
    }

    public void setMddUltModFecha(LocalDateTime mddUltModFecha) {
        this.mddUltModFecha = mddUltModFecha;
    }

    public String getMddUltModUsuario() {
        return mddUltModUsuario;
    }

    public void setMddUltModUsuario(String mddUltModUsuario) {
        this.mddUltModUsuario = mddUltModUsuario;
    }

    public Integer getMddVersion() {
        return mddVersion;
    }

    public void setMddVersion(Integer mddVersion) {
        this.mddVersion = mddVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mddPk != null ? mddPk.hashCode() : 0);
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
        final SgMovimientoDireccionDepartamental other = (SgMovimientoDireccionDepartamental) obj;
        if (!Objects.equals(this.mddPk, other.mddPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMovimientoCuentaBancaria[ mddPk=" + mddPk + " ]";
    }
    
}

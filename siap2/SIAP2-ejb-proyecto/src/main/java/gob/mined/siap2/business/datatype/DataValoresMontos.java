/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author bruno
 */
public class DataValoresMontos {
    private BigDecimal monto = new  BigDecimal(BigInteger.ZERO);
    private BigDecimal montoAprobado = new  BigDecimal(BigInteger.ZERO);

    public DataValoresMontos(BigDecimal monto, BigDecimal montoAprobado) {
        this.monto = monto;
        this.montoAprobado = montoAprobado;
    }

    
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }
    
    
}

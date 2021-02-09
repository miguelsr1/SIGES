/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class DataNodeDistribuccionCategorias {
    String nombre;
    BigDecimal monto;
    BigDecimal usado;
    BigDecimal saldo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getUsado() {
        return usado;
    }

    public void setUsado(BigDecimal usado) {
        this.usado = usado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
}

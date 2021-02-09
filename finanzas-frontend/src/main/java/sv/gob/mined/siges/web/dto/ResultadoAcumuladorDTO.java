/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.math.BigDecimal;

/**
 *
 * @author jgiron
 */
public class ResultadoAcumuladorDTO {

    private String acum1;
    private String acum2;
    private String acum3;
    private BigDecimal valor;
    private BigDecimal valorAprobado;

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public String getAcum1() {
        return acum1;
    }

    public void setAcum1(String acum1) {
        this.acum1 = acum1;
    }

    public String getAcum2() {
        return acum2;
    }

    public void setAcum2(String acum2) {
        this.acum2 = acum2;
    }

    public String getAcum3() {
        return acum3;
    }

    public void setAcum3(String acum3) {
        this.acum3 = acum3;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorAprobado() {
        return valorAprobado;
    }

    public void setValorAprobado(BigDecimal valorAprobado) {
        this.valorAprobado = valorAprobado;
    }

    // </editor-fold>
}

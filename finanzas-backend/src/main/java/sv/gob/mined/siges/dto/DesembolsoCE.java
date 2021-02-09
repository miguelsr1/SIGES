/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Clase auxiliar para guardar los desembolsos de Dirección Dep.
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesembolsoCE implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long tacPk;
    private Long reqFondoCedPk;
    private Long sedPk;
    private String sedCodigo;
    private String sedNombre;
    private Boolean sedHabilitado;
    private Boolean recibo;
    private Boolean oaeMiembrosVigente;
    private LocalDate oeaFechaVencimiento;
    private String convenio;
    private String ccf;
    private BigDecimal montoAutorizado;
    private BigDecimal montoDesembolsado;
    private BigDecimal saldo;
    private BigDecimal montoADesembolsar;
    private Long dedPk;

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public Long getReqFondoCedPk() {
        return reqFondoCedPk;
    }

    public void setReqFondoCedPk(Long reqFondoCedId) {
        this.reqFondoCedPk = reqFondoCedId;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public Boolean getRecibo() {
        return recibo;
    }

    public void setRecibo(Boolean recibo) {
        this.recibo = recibo;
    }

    public Boolean getOaeMiembrosVigente() {
        return oaeMiembrosVigente;
    }

    public void setOaeMiembrosVigente(Boolean oaeMiembrosVigente) {
        this.oaeMiembrosVigente = oaeMiembrosVigente;
    }

    public LocalDate getOeaFechaVencimiento() {
        return oeaFechaVencimiento;
    }

    public void setOeaFechaVencimiento(LocalDate oeaFechaVencimiento) {
        this.oeaFechaVencimiento = oeaFechaVencimiento;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getCcf() {
        return ccf;
    }

    public void setCcf(String ccf) {
        this.ccf = ccf;
    }

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(BigDecimal montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public BigDecimal getMontoDesembolsado() {
        return montoDesembolsado;
    }

    public void setMontoDesembolsado(BigDecimal montoDesembolsado) {
        this.montoDesembolsado = montoDesembolsado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getMontoADesembolsar() {
        return montoADesembolsar;
    }

    public void setMontoADesembolsar(BigDecimal montoADesembolsar) {
        this.montoADesembolsar = montoADesembolsar;
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }
 
    
    
    
    
}

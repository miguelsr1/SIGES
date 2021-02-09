/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase auxiliar para la obtención del transferecias a centros educativos agrupadas por centro
 * @author sofis-iquezada
 */
public class TransferenciaCedAgrup {
    
    private Long sedePk;
    private String codSede;
    private String nomSede;
    private Boolean habilitado;
    private BigDecimal montoTotal;
    private Boolean oeaMiembrosVigente;
    private LocalDate oeaFechaVencimiento;
    private String convenio;
    private String ccf;
    private Boolean recibo;
    private String porcentaje;
    
    
    
    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }
        
    public String getCodSede() {
        return codSede;
    }

    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Boolean getOeaMiembrosVigente() {
        return oeaMiembrosVigente;
    }

    public void setOeaMiembrosVigente(Boolean oeaMiembrosVigente) {
        this.oeaMiembrosVigente = oeaMiembrosVigente;
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

    public Boolean getRecibo() {
        return recibo;
    }

    public void setRecibo(Boolean recibo) {
        this.recibo = recibo;
    }
    
    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequerimientoPorRecurso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long reqPk;
    private String codDep;
    private String nomDep;
    private String compromiso;
    private String sacUfi;
    private String sacGoes;
    private String reqEstado;
    private String finaciamiento;
    private Long recursoPk;
    private String recurso;
    private BigDecimal totalRecurso;
    private BigDecimal montoDesembolso;
    private Double porcentaje;

    public RequerimientoPorRecurso() {
    }

    public Long getReqPk() {
        return reqPk;
    }

    public void setReqPk(Long reqPk) {
        this.reqPk = reqPk;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getNomDep() {
        return nomDep;
    }

    public void setNomDep(String nomDep) {
        this.nomDep = nomDep;
    }

    public String getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(String compromiso) {
        this.compromiso = compromiso;
    }

    public String getSacUfi() {
        return sacUfi;
    }

    public void setSacUfi(String sacUfi) {
        this.sacUfi = sacUfi;
    }

    public String getSacGoes() {
        return sacGoes;
    }

    public void setSacGoes(String sacGoes) {
        this.sacGoes = sacGoes;
    }

    public String getReqEstado() {
        return reqEstado;
    }

    public void setReqEstado(String reqEstado) {
        this.reqEstado = reqEstado;
    }

    public String getFinaciamiento() {
        return finaciamiento;
    }

    public void setFinaciamiento(String finaciamiento) {
        this.finaciamiento = finaciamiento;
    }

    public Long getRecursoPk() {
        return recursoPk;
    }

    public void setRecursoPk(Long recursoPk) {
        this.recursoPk = recursoPk;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public BigDecimal getTotalRecurso() {
        return totalRecurso;
    }

    public void setTotalRecurso(BigDecimal totalRecurso) {
        this.totalRecurso = totalRecurso;
    }

    public BigDecimal getMontoDesembolso() {
        return montoDesembolso;
    }

    public void setMontoDesembolso(BigDecimal montoDesembolso) {
        this.montoDesembolso = montoDesembolso;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

}

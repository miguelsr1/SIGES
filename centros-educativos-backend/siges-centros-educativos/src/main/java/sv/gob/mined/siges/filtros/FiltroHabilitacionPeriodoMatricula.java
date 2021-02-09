/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;

/**
 *
 * @author usuario
 */
public class FiltroHabilitacionPeriodoMatricula implements Serializable {
    
    private Long hmpDepartamentoFk;
    private Long hmpSedeFk;
    private EnumEstadoHabilitacionPeriodoMatricula hmpEstado;
    private Long hmpNivel;
    private Boolean incluyendoNivel;
    
    private LocalDate hmpFechaDesde;
    private LocalDate hmpFechaHasta;
    
    private EnumAmbito ambito;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    
    public FiltroHabilitacionPeriodoMatricula() {
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getHmpDepartamentoFk() {
        return hmpDepartamentoFk;
    }

    public void setHmpDepartamentoFk(Long hmpDepartamentoFk) {
        this.hmpDepartamentoFk = hmpDepartamentoFk;
    }

    public Long getHmpSedeFk() {
        return hmpSedeFk;
    }

    public void setHmpSedeFk(Long hmpSedeFk) {
        this.hmpSedeFk = hmpSedeFk;
    }

    public EnumEstadoHabilitacionPeriodoMatricula getHmpEstado() {
        return hmpEstado;
    }

    public void setHmpEstado(EnumEstadoHabilitacionPeriodoMatricula hmpEstado) {
        this.hmpEstado = hmpEstado;
    }

    public Long getHmpNivel() {
        return hmpNivel;
    }

    public void setHmpNivel(Long hmpNivel) {
        this.hmpNivel = hmpNivel;
    }

    
    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Boolean getIncluyendoNivel() {
        return incluyendoNivel;
    }

    public void setIncluyendoNivel(Boolean incluyendoNivel) {
        this.incluyendoNivel = incluyendoNivel;
    }

    public LocalDate getHmpFechaDesde() {
        return hmpFechaDesde;
    }

    public void setHmpFechaDesde(LocalDate hmpFechaDesde) {
        this.hmpFechaDesde = hmpFechaDesde;
    }

    public LocalDate getHmpFechaHasta() {
        return hmpFechaHasta;
    }

    public void setHmpFechaHasta(LocalDate hmpFechaHasta) {
        this.hmpFechaHasta = hmpFechaHasta;
    }

  
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;

/**
 *
 * @author usuario
 */
public class FiltroHabilitacionPeriodoCalificacion implements Serializable {
    
    private Long hpcDepartamentoFk;
    private Long hpcSedeFk;
    private EnumEstadoHabilitacionPeriodoCalificacion hpcEstado;
    private Long hpcNie;
    
    private EnumAmbito ambito;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroHabilitacionPeriodoCalificacion() {
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

    public Long getHpcDepartamentoFk() {
        return hpcDepartamentoFk;
    }

    public void setHpcDepartamentoFk(Long hpcDepartamentoFk) {
        this.hpcDepartamentoFk = hpcDepartamentoFk;
    }

    public Long getHpcSedeFk() {
        return hpcSedeFk;
    }

    public void setHpcSedeFk(Long hpcSedeFk) {
        this.hpcSedeFk = hpcSedeFk;
    }

    public EnumEstadoHabilitacionPeriodoCalificacion getHpcEstado() {
        return hpcEstado;
    }

    public void setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion hpcEstado) {
        this.hpcEstado = hpcEstado;
    }

    public Long getHpcNie() {
        return hpcNie;
    }

    public void setHpcNie(Long hpcNie) {
        this.hpcNie = hpcNie;
    }

    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }

  
    
}

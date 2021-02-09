/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class FiltroSelloFirmaTitular implements Serializable {
    

    private Boolean sftHabilitado;
    private String sftNombreCompletoPersona;
    private Long sftCargoTitular;
    private String sftCodigoCargo;
    private Long sftDescartandoCargo;
    private Boolean esTitular;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroSelloFirmaTitular() {
        this.first = 0L;
    }

    public Boolean getSftHabilitado() {
        return sftHabilitado;
    }

    public void setSftHabilitado(Boolean sftHabilitado) {
        this.sftHabilitado = sftHabilitado;
    }

    public String getSftNombreCompletoPersona() {
        return sftNombreCompletoPersona;
    }

    public void setSftNombreCompletoPersona(String sftNombreCompletoPersona) {
        this.sftNombreCompletoPersona = sftNombreCompletoPersona;
    }

    public Long getSftCargoTitular() {
        return sftCargoTitular;
    }

    public void setSftCargoTitular(Long sftCargoTitular) {
        this.sftCargoTitular = sftCargoTitular;
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

    public String getSftCodigoCargo() {
        return sftCodigoCargo;
    }

    public void setSftCodigoCargo(String sftCodigoCargo) {
        this.sftCodigoCargo = sftCodigoCargo;
    }

    public Long getSftDescartandoCargo() {
        return sftDescartandoCargo;
    }

    public void setSftDescartandoCargo(Long sftDescartandoCargo) {
        this.sftDescartandoCargo = sftDescartandoCargo;
    }

    public Boolean getEsTitular() {
        return esTitular;
    }

    public void setEsTitular(Boolean esTitular) {
        this.esTitular = esTitular;
    }

    
    
}

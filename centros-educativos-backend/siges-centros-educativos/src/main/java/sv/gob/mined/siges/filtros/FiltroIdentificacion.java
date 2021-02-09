/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;


import java.io.Serializable;

public class FiltroIdentificacion implements Serializable{
    
    private Long idePk;    
    private String ideNumeroDocumento;
    private Long idePersona;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroIdentificacion() {
    }

    public Long getIdePk() {
        return idePk;
    }

    public void setIdePk(Long idePk) {
        this.idePk = idePk;
    }

    public String getIdeNumeroDocumento() {
        return ideNumeroDocumento;
    }

    public void setIdeNumeroDocumento(String ideNumeroDocumento) {
        this.ideNumeroDocumento = ideNumeroDocumento;
    }

    public Long getIdePersona() {
        return idePersona;
    }

    public void setIdePersona(Long idePersona) {
        this.idePersona = idePersona;
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
    
    
    
    

    
}

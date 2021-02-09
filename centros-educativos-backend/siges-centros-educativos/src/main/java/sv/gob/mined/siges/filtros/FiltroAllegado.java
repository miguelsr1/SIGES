/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author usuario
 */
public class FiltroAllegado implements Serializable {
    
    private Long allPk;
    private Long allPersonaPk;
    private Long allPersonaReferenciadaPk;
    private Boolean allEsReferente;
    private Boolean allEsReferenteOContactoEmergencia;
    private List<Long> allPersonasReferenciadasPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;
    
    public FiltroAllegado() {
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

    public Long getAllPk() {
        return allPk;
    }

    public void setAllPk(Long allPk) {
        this.allPk = allPk;
    }

    public Long getAllPersonaPk() {
        return allPersonaPk;
    }

    public void setAllPersonaPk(Long allPersonaPk) {
        this.allPersonaPk = allPersonaPk;
    }

    public Long getAllPersonaReferenciadaPk() {
        return allPersonaReferenciadaPk;
    }

    public void setAllPersonaReferenciadaPk(Long allPersonaReferenciadaPk) {
        this.allPersonaReferenciadaPk = allPersonaReferenciadaPk;
    }

    public Boolean getAllEsReferente() {
        return allEsReferente;
    }

    public void setAllEsReferente(Boolean allEsReferente) {
        this.allEsReferente = allEsReferente;
    }

    public Boolean getAllEsReferenteOContactoEmergencia() {
        return allEsReferenteOContactoEmergencia;
    }

    public void setAllEsReferenteOContactoEmergencia(Boolean allEsReferenteOContactoEmergencia) {
        this.allEsReferenteOContactoEmergencia = allEsReferenteOContactoEmergencia;
    }

    public List<Long> getAllPersonasReferenciadasPk() {
        return allPersonasReferenciadasPk;
    }

    public void setAllPersonasReferenciadasPk(List<Long> allPersonasReferenciadasPk) {
        this.allPersonasReferenciadasPk = allPersonasReferenciadasPk;
    }
    
}

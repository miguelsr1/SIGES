/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroAllegado implements Serializable {

    private Long allPk;
    private Long allPersonaPk;
    private Long allPersonaReferenciadaPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private Boolean inicializarProgramaEducativo;
    private String[] incluirCampos;

    public FiltroAllegado() {
        inicializarProgramaEducativo = Boolean.TRUE;
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

    public Boolean getInicializarProgramaEducativo() {
        return inicializarProgramaEducativo;
    }

    public void setInicializarProgramaEducativo(Boolean inicializarProgramaEducativo) {
        this.inicializarProgramaEducativo = inicializarProgramaEducativo;
    }

    public Long getAllPersonaReferenciadaPk() {
        return allPersonaReferenciadaPk;
    }

    public void setAllPersonaReferenciadaPk(Long allPersonaReferenciadaPk) {
        this.allPersonaReferenciadaPk = allPersonaReferenciadaPk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

/**
 *
 * @author bruno
 */
public class FiltroDatoSaludEstudiante implements Serializable {
    private Long dseEstudianteId;
    private Long dseAnio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDatoSaludEstudiante() {
    }

    public Long getDseEstudianteId() {
        return dseEstudianteId;
    }

    public void setDseEstudianteId(Long dseEstudianteId) {
        this.dseEstudianteId = dseEstudianteId;
    }

    public Long getDseAnio() {
        return dseAnio;
    }

    public void setDseAnio(Long dseAnio) {
        this.dseAnio = dseAnio;
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

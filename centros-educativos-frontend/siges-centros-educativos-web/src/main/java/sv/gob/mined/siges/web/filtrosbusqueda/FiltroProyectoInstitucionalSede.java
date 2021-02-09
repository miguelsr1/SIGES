/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroProyectoInstitucionalSede implements Serializable {
    
    private Long sede;
    private Integer anio;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private Boolean inicializarProgramaEducativo;
    
    public FiltroProyectoInstitucionalSede() {
        inicializarProgramaEducativo=Boolean.TRUE;
    }

    public Long getSede() {
        return sede;
    }

    public void setSede(Long sede) {
        this.sede = sede;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    
    
//<editor-fold defaultstate="collapsed" desc="GET & SET default">
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
//</editor-fold>
       
}

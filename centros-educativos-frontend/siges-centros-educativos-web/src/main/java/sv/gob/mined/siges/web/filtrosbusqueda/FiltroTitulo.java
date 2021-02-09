/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;

public class FiltroTitulo implements Serializable {
    
    private String titHash;
    private Long titNie;
    private Boolean titNoAnulada;
    private EnumEstadoSolicitudImpresion titEstadoSolicitudImp;
    private String titNombreEstudiante;
    private Boolean titAnulada;
    private Long departamento;
    private Long municipio;
    private String dui;
    private Long titPlanEstudioFk;
    private Long titGradoFk;
   
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroTitulo() {
    }

    public String getTitHash() {
        return titHash;
    }

    public void setTitHash(String titHash) {
        this.titHash = titHash;
    }

    public Long getTitNie() {
        return titNie;
    }

    public void setTitNie(Long titNie) {
        this.titNie = titNie;
    }       

    public Boolean getTitNoAnulada() {
        return titNoAnulada;
    }

    public void setTitNoAnulada(Boolean titNoAnulada) {
        this.titNoAnulada = titNoAnulada;
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

    public EnumEstadoSolicitudImpresion getTitEstadoSolicitudImp() {
        return titEstadoSolicitudImp;
    }

    public void setTitEstadoSolicitudImp(EnumEstadoSolicitudImpresion titEstadoSolicitudImp) {
        this.titEstadoSolicitudImp = titEstadoSolicitudImp;
    }


    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public String getTitNombreEstudiante() {
        return titNombreEstudiante;
    }

    public void setTitNombreEstudiante(String titNombreEstudiante) {
        this.titNombreEstudiante = titNombreEstudiante;
    }

    public Boolean getTitAnulada() {
        return titAnulada;
    }

    public void setTitAnulada(Boolean titAnulada) {
        this.titAnulada = titAnulada;
    }

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public Long getTitPlanEstudioFk() {
        return titPlanEstudioFk;
    }

    public void setTitPlanEstudioFk(Long titPlanEstudioFk) {
        this.titPlanEstudioFk = titPlanEstudioFk;
    }

    public Long getTitGradoFk() {
        return titGradoFk;
    }

    public void setTitGradoFk(Long titGradoFk) {
        this.titGradoFk = titGradoFk;
    }
    
}

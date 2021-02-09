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
    private Long titEstudiante;
    private Long titSede;
    private Integer titAnio;
    private Long titDefinicionTitulo;
    private EnumEstadoSolicitudImpresion titEstadoSolicitudImp;
    private Long titSolicitudImpresion;
    
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

    public Long getTitEstudiante() {
        return titEstudiante;
    }

    public void setTitEstudiante(Long titEstudiante) {
        this.titEstudiante = titEstudiante;
    }

    public Long getTitSede() {
        return titSede;
    }

    public void setTitSede(Long titSede) {
        this.titSede = titSede;
    }

    public Integer getTitAnio() {
        return titAnio;
    }

    public void setTitAnio(Integer titAnio) {
        this.titAnio = titAnio;
    }

    public Long getTitDefinicionTitulo() {
        return titDefinicionTitulo;
    }

    public void setTitDefinicionTitulo(Long titDefinicionTitulo) {
        this.titDefinicionTitulo = titDefinicionTitulo;
    }

    public EnumEstadoSolicitudImpresion getTitEstadoSolicitudImp() {
        return titEstadoSolicitudImp;
    }

    public void setTitEstadoSolicitudImp(EnumEstadoSolicitudImpresion titEstadoSolicitudImp) {
        this.titEstadoSolicitudImp = titEstadoSolicitudImp;
    }

    public Long getTitSolicitudImpresion() {
        return titSolicitudImpresion;
    }

    public void setTitSolicitudImpresion(Long titSolicitudImpresion) {
        this.titSolicitudImpresion = titSolicitudImpresion;
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

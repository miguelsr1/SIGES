/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroEtiqueta implements Serializable {
    
    private Long etiPk;
    private String etiApellido;
    private String etiNombre;
    private String etiDui;    
    private String etiNie;    
    private Integer etiLibro;    
    private Integer etiPagina;
    private String etiNombreSede;    
    private Integer etiAnio;
    private Boolean etiHabilitado;
    private Long pagPk;
    private Long nivPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private Boolean inicializarProgramaEducativo;
    
    public FiltroEtiqueta() {
        inicializarProgramaEducativo=Boolean.TRUE;
    }

    public Long getEtiPk() {
        return etiPk;
    }

    public void setEtiPk(Long etiPk) {
        this.etiPk = etiPk;
    }

    public String getEtiApellido() {
        return etiApellido;
    }

    public void setEtiApellido(String etiApellido) {
        this.etiApellido = etiApellido;
    }

    public String getEtiNombre() {
        return etiNombre;
    }

    public void setEtiNombre(String etiNombre) {
        this.etiNombre = etiNombre;
    }

    public String getEtiDui() {
        return etiDui;
    }

    public void setEtiDui(String etiDui) {
        this.etiDui = etiDui;
    }

    public String getEtiNie() {
        return etiNie;
    }

    public void setEtiNie(String etiNie) {
        this.etiNie = etiNie;
    }

    public Integer getEtiLibro() {
        return etiLibro;
    }

    public void setEtiLibro(Integer etiLibro) {
        this.etiLibro = etiLibro;
    }

    public Integer getEtiPagina() {
        return etiPagina;
    }

    public void setEtiPagina(Integer etiPagina) {
        this.etiPagina = etiPagina;
    }

    public String getEtiNombreSede() {
        return etiNombreSede;
    }

    public void setEtiNombreSede(String etiNombreSede) {
        this.etiNombreSede = etiNombreSede;
    }

    public Integer getEtiAnio() {
        return etiAnio;
    }

    public void setEtiAnio(Integer etiAnio) {
        this.etiAnio = etiAnio;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getEtiHabilitado() {
        return etiHabilitado;
    }

    public void setEtiHabilitado(Boolean etiHabilitado) {
        this.etiHabilitado = etiHabilitado;
    }

    public Long getPagPk() {
        return pagPk;
    }

    public void setPagPk(Long pagPk) {
        this.pagPk = pagPk;
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
    }
    
    
}

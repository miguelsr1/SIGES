/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroPagina implements Serializable {
    
    private Long pagPk; 
    private Integer pagLibro;    
    private Integer pagPagina;   
    private Integer pagAnio;    
    private Long pagNivelPk;   
    private String pagNombreLibro;      
    private String pagRuta;     
    private Boolean pagHabilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private Boolean inicializarProgramaEducativo;
    
    public FiltroPagina() {
        inicializarProgramaEducativo=Boolean.TRUE;
    }

    public Long getPagPk() {
        return pagPk;
    }

    public void setPagPk(Long pagPk) {
        this.pagPk = pagPk;
    }

    public Integer getPagLibro() {
        return pagLibro;
    }

    public void setPagLibro(Integer pagLibro) {
        this.pagLibro = pagLibro;
    }

    public Integer getPagPagina() {
        return pagPagina;
    }

    public void setPagPagina(Integer pagPagina) {
        this.pagPagina = pagPagina;
    }

    public Integer getPagAnio() {
        return pagAnio;
    }

    public void setPagAnio(Integer pagAnio) {
        this.pagAnio = pagAnio;
    }

    public Long getPagNivelPk() {
        return pagNivelPk;
    }

    public void setPagNivelPk(Long pagNivelPk) {
        this.pagNivelPk = pagNivelPk;
    }

    public String getPagNombreLibro() {
        return pagNombreLibro;
    }

    public void setPagNombreLibro(String pagNombreLibro) {
        this.pagNombreLibro = pagNombreLibro;
    }

    public String getPagRuta() {
        return pagRuta;
    }

    public void setPagRuta(String pagRuta) {
        this.pagRuta = pagRuta;
    }

    public Boolean getPagHabilitado() {
        return pagHabilitado;
    }

    public void setPagHabilitado(Boolean pagHabilitado) {
        this.pagHabilitado = pagHabilitado;
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
      
}

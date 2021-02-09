package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;
import java.time.LocalDate;

public class FiltroControlAsistenciaPersonalCabezal implements Serializable {
    
    private Long cpcPk;    
    private Long cpcSede;
    private LocalDate cpcFecha;
    private LocalDate cpcDesde;
    private LocalDate cpcHasta;
    private Long cpcDepartamento;
    private Long cpcMunicipio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroControlAsistenciaPersonalCabezal() {
    }

    public Long getCpcPk() {
        return cpcPk;
    }

    public void setCpcPk(Long cpcPk) {
        this.cpcPk = cpcPk;
    }

    public Long getCpcSede() {
        return cpcSede;
    }

    public void setCpcSede(Long cpcSede) {
        this.cpcSede = cpcSede;
    }

    public LocalDate getCpcFecha() {
        return cpcFecha;
    }

    public void setCpcFecha(LocalDate cpcFecha) {
        this.cpcFecha = cpcFecha;
    }

    public LocalDate getCpcDesde() {
        return cpcDesde;
    }

    public void setCpcDesde(LocalDate cpcDesde) {
        this.cpcDesde = cpcDesde;
    }

    public LocalDate getCpcHasta() {
        return cpcHasta;
    }

    public void setCpcHasta(LocalDate cpcHasta) {
        this.cpcHasta = cpcHasta;
    }

    public Long getCpcDepartamento() {
        return cpcDepartamento;
    }

    public void setCpcDepartamento(Long cpcDepartamento) {
        this.cpcDepartamento = cpcDepartamento;
    }

    public Long getCpcMunicipio() {
        return cpcMunicipio;
    }

    public void setCpcMunicipio(Long cpcMunicipio) {
        this.cpcMunicipio = cpcMunicipio;
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

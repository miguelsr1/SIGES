package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSustitucionMiembroOAE;

public class FiltroSustitucionMiembroOAE implements Serializable {

    private Long smoOaeFk;
    private EnumEstadoSustitucionMiembroOAE smoEstado;
    private Long smoDepartamento;
    private Long smoSede;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long smoTipoOrganismoAdministrativo;
    private Boolean incluirCantidadMiembros;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroSustitucionMiembroOAE() {
    }

    public Long getSmoOaeFk() {
        return smoOaeFk;
    }

    public void setSmoOaeFk(Long smoOaeFk) {
        this.smoOaeFk = smoOaeFk;
    }

    public EnumEstadoSustitucionMiembroOAE getSmoEstado() {
        return smoEstado;
    }

    public void setSmoEstado(EnumEstadoSustitucionMiembroOAE smoEstado) {
        this.smoEstado = smoEstado;
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

    public Long getSmoDepartamento() {
        return smoDepartamento;
    }

    public void setSmoDepartamento(Long smoDepartamento) {
        this.smoDepartamento = smoDepartamento;
    }

    public Long getSmoSede() {
        return smoSede;
    }

    public void setSmoSede(Long smoSede) {
        this.smoSede = smoSede;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Long getSmoTipoOrganismoAdministrativo() {
        return smoTipoOrganismoAdministrativo;
    }

    public void setSmoTipoOrganismoAdministrativo(Long smoTipoOrganismoAdministrativo) {
        this.smoTipoOrganismoAdministrativo = smoTipoOrganismoAdministrativo;
    }

    public Boolean getIncluirCantidadMiembros() {
        return incluirCantidadMiembros;
    }

    public void setIncluirCantidadMiembros(Boolean incluirCantidadMiembros) {
        this.incluirCantidadMiembros = incluirCantidadMiembros;
    }

  
    
}

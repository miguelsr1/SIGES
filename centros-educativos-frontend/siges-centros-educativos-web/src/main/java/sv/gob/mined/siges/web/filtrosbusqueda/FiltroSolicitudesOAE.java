package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudOAE;
import sv.gob.mined.siges.web.enumerados.TipoSede;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroSolicitudesOAE implements Serializable {

    private Long sedeFk;
    private Long dpjOaeFk;
    private EnumEstadoSolicitudOAE estado;
    private Long departamento;
    private Long municipio;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private TipoSede tipoSede;
    
    private String[] incluirCampos;
    private String securityOperation;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroSolicitudesOAE() {
        this.first = 0L;
    }

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Long getDpjOaeFk() {
        return dpjOaeFk;
    }

    public void setDpjOaeFk(Long dpjOaeFk) {
        this.dpjOaeFk = dpjOaeFk;
    }

    public EnumEstadoSolicitudOAE getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoSolicitudOAE estado) {
        this.estado = estado;
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

    public TipoSede getTipoSede() {
        return tipoSede;
    }

    public void setTipoSede(TipoSede tipoSede) {
        this.tipoSede = tipoSede;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

}

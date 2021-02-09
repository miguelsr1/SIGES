package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.TipoSede;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroOrganismoAdministrativoEscolar implements Serializable {

    private Long oaeSedeFk;
    private EnumEstadoOrganismoAdministrativo oaeEstado;
    private Long oaeDepartamento;
    private Long oaeMunicipio;
    private LocalDate oaeFechaVencimientoDesde;
    private LocalDate oaeFechaVencimientoHasta;
    private TipoSede oaeTipoSede;
    private EnumEstadoOrganismoAdministrativo oaeSinEstado;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroOrganismoAdministrativoEscolar() {

    }

    public Long getOaeSedeFk() {
        return oaeSedeFk;
    }

    public void setOaeSedeFk(Long oaeSedeFk) {
        this.oaeSedeFk = oaeSedeFk;
    }

    public EnumEstadoOrganismoAdministrativo getOaeEstado() {
        return oaeEstado;
    }

    public void setOaeEstado(EnumEstadoOrganismoAdministrativo oaeEstado) {
        this.oaeEstado = oaeEstado;
    }

    public Long getOaeDepartamento() {
        return oaeDepartamento;
    }

    public void setOaeDepartamento(Long oaeDepartamento) {
        this.oaeDepartamento = oaeDepartamento;
    }

    public Long getOaeMunicipio() {
        return oaeMunicipio;
    }

    public void setOaeMunicipio(Long oaeMunicipio) {
        this.oaeMunicipio = oaeMunicipio;
    }

    public LocalDate getOaeFechaVencimientoDesde() {
        return oaeFechaVencimientoDesde;
    }

    public void setOaeFechaVencimientoDesde(LocalDate oaeFechaVencimientoDesde) {
        this.oaeFechaVencimientoDesde = oaeFechaVencimientoDesde;
    }

    public LocalDate getOaeFechaVencimientoHasta() {
        return oaeFechaVencimientoHasta;
    }

    public void setOaeFechaVencimientoHasta(LocalDate oaeFechaVencimientoHasta) {
        this.oaeFechaVencimientoHasta = oaeFechaVencimientoHasta;
    }

    public TipoSede getOaeTipoSede() {
        return oaeTipoSede;
    }

    public void setOaeTipoSede(TipoSede oaeTipoSede) {
        this.oaeTipoSede = oaeTipoSede;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public EnumEstadoOrganismoAdministrativo getOaeSinEstado() {
        return oaeSinEstado;
    }

    public void setOaeSinEstado(EnumEstadoOrganismoAdministrativo oaeSinEstado) {
        this.oaeSinEstado = oaeSinEstado;
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

 

}

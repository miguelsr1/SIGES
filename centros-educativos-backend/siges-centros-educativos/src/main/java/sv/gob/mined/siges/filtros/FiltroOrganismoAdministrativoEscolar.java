package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.enumerados.TipoSede;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroOrganismoAdministrativoEscolar implements Serializable {

    private Long oaeSedeFk;
    private EnumEstadoOrganismoAdministrativo oaeEstado;
    private List<EnumEstadoOrganismoAdministrativo> oaeEstados;
    private Long oaeDepartamento;
    private Long oaeMunicipio;
    private LocalDate oaeFechaVencimientoDesde;
    private LocalDate oaeFechaVencimientoHasta;
    private TipoSede oaeTipoSede;
    private EnumEstadoOrganismoAdministrativo oaeSinEstado;
    private Boolean oaeSubvencionado;
    private String oaeNumeroAcuerdo;
    private Long oaeTipoOAE;
    private Long oaeRenovarMiembroPadre;
    private Long oaePk;
    
    private String[] incluirCampos;
    private String securityOperation;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroOrganismoAdministrativoEscolar() {

    }

    public Long getOaePk() {
        return oaePk;
    }

    public void setOaePk(Long oaePk) {
        this.oaePk = oaePk;
    }

    public Long getOaeSedeFk() {
        return oaeSedeFk;
    }

    public void setOaeSedeFk(Long oaeSedeFk) {
        this.oaeSedeFk = oaeSedeFk;
    }

    public Long getOaeRenovarMiembroPadre() {
        return oaeRenovarMiembroPadre;
    }

    public void setOaeRenovarMiembroPadre(Long oaeRenovarMiembroPadre) {
        this.oaeRenovarMiembroPadre = oaeRenovarMiembroPadre;
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

    public EnumEstadoOrganismoAdministrativo getOaeSinEstado() {
        return oaeSinEstado;
    }

    public void setOaeSinEstado(EnumEstadoOrganismoAdministrativo oaeSinEstado) {
        this.oaeSinEstado = oaeSinEstado;
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

    public Boolean getOaeSubvencionado() {
        return oaeSubvencionado;
    }

    public void setOaeSubvencionado(Boolean oaeSubvencionado) {
        this.oaeSubvencionado = oaeSubvencionado;
    }

    public String getOaeNumeroAcuerdo() {
        return oaeNumeroAcuerdo;
    }

    public void setOaeNumeroAcuerdo(String oaeNumeroAcuerdo) {
        this.oaeNumeroAcuerdo = oaeNumeroAcuerdo;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Long getOaeTipoOAE() {
        return oaeTipoOAE;
    }

    public void setOaeTipoOAE(Long oaeTipoOAE) {
        this.oaeTipoOAE = oaeTipoOAE;
    }

    public List<EnumEstadoOrganismoAdministrativo> getOaeEstados() {
        return oaeEstados;
    }

    public void setOaeEstados(List<EnumEstadoOrganismoAdministrativo> oaeEstados) {
        this.oaeEstados = oaeEstados;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroTipoAcuerdo implements Serializable {
    
    private Long tipoAcuerdoPk;
    private Long excluirTipoAcuerdoPk;
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private Boolean habilitado;

    private Boolean taoTramiteRevocatoriaSede;
    private Boolean taoTramiteCreacionSede;
    private Boolean taoTramiteNominacionSede;
    private Boolean taoTramiteCambioDomicilioSede;
    private Boolean taoTramiteAmpliacionServicios;
    private Boolean taoTramiteDisminucionServicios;
    
    private Boolean realizarORConTramites;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroTipoAcuerdo() {
    }
     

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public Boolean getTaoTramiteRevocatoriaSede() {
        return taoTramiteRevocatoriaSede;
    }

    public void setTaoTramiteRevocatoriaSede(Boolean taoTramiteRevocatoriaSede) {
        this.taoTramiteRevocatoriaSede = taoTramiteRevocatoriaSede;
    }

    public Boolean getTaoTramiteCreacionSede() {
        return taoTramiteCreacionSede;
    }

    public void setTaoTramiteCreacionSede(Boolean taoTramiteCreacionSede) {
        this.taoTramiteCreacionSede = taoTramiteCreacionSede;
    }

    public Boolean getTaoTramiteNominacionSede() {
        return taoTramiteNominacionSede;
    }

    public void setTaoTramiteNominacionSede(Boolean taoTramiteNominacionSede) {
        this.taoTramiteNominacionSede = taoTramiteNominacionSede;
    }

    public Boolean getTaoTramiteCambioDomicilioSede() {
        return taoTramiteCambioDomicilioSede;
    }

    public void setTaoTramiteCambioDomicilioSede(Boolean taoTramiteCambioDomicilioSede) {
        this.taoTramiteCambioDomicilioSede = taoTramiteCambioDomicilioSede;
    }

    public Boolean getTaoTramiteAmpliacionServicios() {
        return taoTramiteAmpliacionServicios;
    }

    public void setTaoTramiteAmpliacionServicios(Boolean taoTramiteAmpliacionServicios) {
        this.taoTramiteAmpliacionServicios = taoTramiteAmpliacionServicios;
    }

    public Boolean getTaoTramiteDisminucionServicios() {
        return taoTramiteDisminucionServicios;
    }

    public void setTaoTramiteDisminucionServicios(Boolean taoTramiteDisminucionServicios) {
        this.taoTramiteDisminucionServicios = taoTramiteDisminucionServicios;
    }

    public Long getTipoAcuerdoPk() {
        return tipoAcuerdoPk;
    }

    public void setTipoAcuerdoPk(Long tipoAcuerdoPk) {
        this.tipoAcuerdoPk = tipoAcuerdoPk;
    }

    public Long getExcluirTipoAcuerdoPk() {
        return excluirTipoAcuerdoPk;
    }

    public void setExcluirTipoAcuerdoPk(Long excluirTipoAcuerdoPk) {
        this.excluirTipoAcuerdoPk = excluirTipoAcuerdoPk;
    }

    public Boolean getRealizarORConTramites() {
        return realizarORConTramites;
    }

    public void setRealizarORConTramites(Boolean realizarORConTramites) {
        this.realizarORConTramites = realizarORConTramites;
    }

    
    

      
}

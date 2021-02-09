/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.TipoSede;

public class FiltroSedes implements Serializable {

    private Long sedPk;
    private String sedNombre;
    private String sedCodigo;
    private String sedCodigoONombre;
    private Boolean sedLegalizada;
    private Long sedDepartamentoId;
    private Long sedMunicipioId;
    private Long sedZonaId;
    private TipoSede sedTipo;
    private Long sedTipoCalendario;
    private Boolean priSubvencionada;
    private Long sedServicioEducativo;
    private Boolean cedFineDeLucro;
    private Long cedTipoOrganismoAdministrativoPk;
    private Boolean sedHabilitado;
    private Boolean sedRevisado;

    private Long sedNivel;
    private Long sedCiclo;
    private Long sedModalidadEducativa;
    private Long sedOpcion;
    private Long sedGrado;
    private Long sedProgramaEducativo;
    private Long sedModalidadAtencion;
    private Long sedSubModalidad;
    private Long sedAdscritaPk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
     private String securityOperation;
    private Boolean incluirSiTieneAdscritas;

    public FiltroSedes() {
        this.first = 0L;
        this.incluirSiTieneAdscritas = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_SEDES;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public Long getSedTipoCalendario() {
        return sedTipoCalendario;
    }

    public void setSedTipoCalendario(Long sedTipoCalendario) {
        this.sedTipoCalendario = sedTipoCalendario;
    }

    public Boolean getPriSubvencionada() {
        return priSubvencionada;
    }

    public void setPriSubvencionada(Boolean priSubvencionada) {
        this.priSubvencionada = priSubvencionada;
    }

    public Long getSedServicioEducativo() {
        return sedServicioEducativo;
    }

    public void setSedServicioEducativo(Long sedServicioEducativo) {
        this.sedServicioEducativo = sedServicioEducativo;
    }

    public Boolean getCedFineDeLucro() {
        return cedFineDeLucro;
    }

    public void setCedFineDeLucro(Boolean cedFineDeLucro) {
        this.cedFineDeLucro = cedFineDeLucro;
    }

    public Long getCedTipoOrganismoAdministrativoPk() {
        return cedTipoOrganismoAdministrativoPk;
    }

    public void setCedTipoOrganismoAdministrativoPk(Long cedTipoOrganismoAdministrativoPk) {
        this.cedTipoOrganismoAdministrativoPk = cedTipoOrganismoAdministrativoPk;
    }

    public Boolean getSedRevisado() {
        return sedRevisado;
    }

    public void setSedRevisado(Boolean sedRevisado) {
        this.sedRevisado = sedRevisado;
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

    public Boolean getSedLegalizada() {
        return sedLegalizada;
    }

    public void setSedLegalizada(Boolean sedLegalizada) {
        this.sedLegalizada = sedLegalizada;
    }

    public Long getSedDepartamentoId() {
        return sedDepartamentoId;
    }

    public void setSedDepartamentoId(Long sedDepartamentoId) {
        this.sedDepartamentoId = sedDepartamentoId;
    }

    public Long getSedMunicipioId() {
        return sedMunicipioId;
    }

    public void setSedMunicipioId(Long sedMunicipioId) {
        this.sedMunicipioId = sedMunicipioId;
    }

    public Long getSedZonaId() {
        return sedZonaId;
    }

    public void setSedZonaId(Long sedZonaId) {
        this.sedZonaId = sedZonaId;
    }

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }

    public String getSedCodigoONombre() {
        return sedCodigoONombre;
    }

    public void setSedCodigoONombre(String sedCodigoONombre) {
        this.sedCodigoONombre = sedCodigoONombre;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public Long getSedNivel() {
        return sedNivel;
    }

    public void setSedNivel(Long sedNivel) {
        this.sedNivel = sedNivel;
    }

    public Long getSedCiclo() {
        return sedCiclo;
    }

    public void setSedCiclo(Long sedCiclo) {
        this.sedCiclo = sedCiclo;
    }

    public Long getSedModalidadEducativa() {
        return sedModalidadEducativa;
    }

    public void setSedModalidadEducativa(Long sedModalidadEducativa) {
        this.sedModalidadEducativa = sedModalidadEducativa;
    }

    public Long getSedOpcion() {
        return sedOpcion;
    }

    public void setSedOpcion(Long sedOpcion) {
        this.sedOpcion = sedOpcion;
    }

    public Long getSedGrado() {
        return sedGrado;
    }

    public void setSedGrado(Long sedGrado) {
        this.sedGrado = sedGrado;
    }

    public Long getSedProgramaEducativo() {
        return sedProgramaEducativo;
    }

    public void setSedProgramaEducativo(Long sedProgramaEducativo) {
        this.sedProgramaEducativo = sedProgramaEducativo;
    }

    public Long getSedModalidadAtencion() {
        return sedModalidadAtencion;
    }

    public void setSedModalidadAtencion(Long sedModalidadAtencion) {
        this.sedModalidadAtencion = sedModalidadAtencion;
    }

    public Long getSedSubModalidad() {
        return sedSubModalidad;
    }

    public void setSedSubModalidad(Long sedSubModalidad) {
        this.sedSubModalidad = sedSubModalidad;
    }

    public Long getSedAdscritaPk() {
        return sedAdscritaPk;
    }

    public void setSedAdscritaPk(Long sedAdscritaPk) {
        this.sedAdscritaPk = sedAdscritaPk;
    }

    public Boolean getIncluirSiTieneAdscritas() {
        return incluirSiTieneAdscritas;
    }

    public void setIncluirSiTieneAdscritas(Boolean incluirSiTieneAdscritas) {
        this.incluirSiTieneAdscritas = incluirSiTieneAdscritas;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }
    
    

}

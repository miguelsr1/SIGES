/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    private List<TipoSede> sedTipos;
    private Boolean sedPrivadaSubvencionada;

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

    public List<TipoSede> getSedTipos() {
        return sedTipos;
    }

    public void setSedTipos(List<TipoSede> sedTipos) {
        this.sedTipos = sedTipos;
    }

    public Boolean getSedPrivadaSubvencionada() {
        return sedPrivadaSubvencionada;
    }

    public void setSedPrivadaSubvencionada(Boolean sedPrivadaSubvencionada) {
        this.sedPrivadaSubvencionada = sedPrivadaSubvencionada;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.sedPk);
        hash = 41 * hash + Objects.hashCode(this.sedNombre);
        hash = 41 * hash + Objects.hashCode(this.sedCodigo);
        hash = 41 * hash + Objects.hashCode(this.sedCodigoONombre);
        hash = 41 * hash + Objects.hashCode(this.sedLegalizada);
        hash = 41 * hash + Objects.hashCode(this.sedDepartamentoId);
        hash = 41 * hash + Objects.hashCode(this.sedMunicipioId);
        hash = 41 * hash + Objects.hashCode(this.sedZonaId);
        hash = 41 * hash + Objects.hashCode(this.sedTipo);
        hash = 41 * hash + Objects.hashCode(this.sedTipoCalendario);
        hash = 41 * hash + Objects.hashCode(this.priSubvencionada);
        hash = 41 * hash + Objects.hashCode(this.sedServicioEducativo);
        hash = 41 * hash + Objects.hashCode(this.cedFineDeLucro);
        hash = 41 * hash + Objects.hashCode(this.cedTipoOrganismoAdministrativoPk);
        hash = 41 * hash + Objects.hashCode(this.sedHabilitado);
        hash = 41 * hash + Objects.hashCode(this.sedRevisado);
        hash = 41 * hash + Objects.hashCode(this.sedTipos);
        hash = 41 * hash + Objects.hashCode(this.sedPrivadaSubvencionada);
        hash = 41 * hash + Objects.hashCode(this.sedNivel);
        hash = 41 * hash + Objects.hashCode(this.sedCiclo);
        hash = 41 * hash + Objects.hashCode(this.sedModalidadEducativa);
        hash = 41 * hash + Objects.hashCode(this.sedOpcion);
        hash = 41 * hash + Objects.hashCode(this.sedGrado);
        hash = 41 * hash + Objects.hashCode(this.sedProgramaEducativo);
        hash = 41 * hash + Objects.hashCode(this.sedModalidadAtencion);
        hash = 41 * hash + Objects.hashCode(this.sedSubModalidad);
        hash = 41 * hash + Objects.hashCode(this.sedAdscritaPk);
        hash = 41 * hash + Objects.hashCode(this.first);
        hash = 41 * hash + Objects.hashCode(this.maxResults);
        hash = 41 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 41 * hash + Arrays.hashCode(this.ascending);
        hash = 41 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 41 * hash + Objects.hashCode(this.securityOperation);
        hash = 41 * hash + Objects.hashCode(this.incluirSiTieneAdscritas);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroSedes other = (FiltroSedes) obj;
        if (!Objects.equals(this.sedNombre, other.sedNombre)) {
            return false;
        }
        if (!Objects.equals(this.sedCodigo, other.sedCodigo)) {
            return false;
        }
        if (!Objects.equals(this.sedCodigoONombre, other.sedCodigoONombre)) {
            return false;
        }
        if (!Objects.equals(this.securityOperation, other.securityOperation)) {
            return false;
        }
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        if (!Objects.equals(this.sedLegalizada, other.sedLegalizada)) {
            return false;
        }
        if (!Objects.equals(this.sedDepartamentoId, other.sedDepartamentoId)) {
            return false;
        }
        if (!Objects.equals(this.sedMunicipioId, other.sedMunicipioId)) {
            return false;
        }
        if (!Objects.equals(this.sedZonaId, other.sedZonaId)) {
            return false;
        }
        if (this.sedTipo != other.sedTipo) {
            return false;
        }
        if (!Objects.equals(this.sedTipoCalendario, other.sedTipoCalendario)) {
            return false;
        }
        if (!Objects.equals(this.priSubvencionada, other.priSubvencionada)) {
            return false;
        }
        if (!Objects.equals(this.sedServicioEducativo, other.sedServicioEducativo)) {
            return false;
        }
        if (!Objects.equals(this.cedFineDeLucro, other.cedFineDeLucro)) {
            return false;
        }
        if (!Objects.equals(this.cedTipoOrganismoAdministrativoPk, other.cedTipoOrganismoAdministrativoPk)) {
            return false;
        }
        if (!Objects.equals(this.sedHabilitado, other.sedHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.sedRevisado, other.sedRevisado)) {
            return false;
        }
        if (!Objects.equals(this.sedTipos, other.sedTipos)) {
            return false;
        }
        if (!Objects.equals(this.sedPrivadaSubvencionada, other.sedPrivadaSubvencionada)) {
            return false;
        }
        if (!Objects.equals(this.sedNivel, other.sedNivel)) {
            return false;
        }
        if (!Objects.equals(this.sedCiclo, other.sedCiclo)) {
            return false;
        }
        if (!Objects.equals(this.sedModalidadEducativa, other.sedModalidadEducativa)) {
            return false;
        }
        if (!Objects.equals(this.sedOpcion, other.sedOpcion)) {
            return false;
        }
        if (!Objects.equals(this.sedGrado, other.sedGrado)) {
            return false;
        }
        if (!Objects.equals(this.sedProgramaEducativo, other.sedProgramaEducativo)) {
            return false;
        }
        if (!Objects.equals(this.sedModalidadAtencion, other.sedModalidadAtencion)) {
            return false;
        }
        if (!Objects.equals(this.sedSubModalidad, other.sedSubModalidad)) {
            return false;
        }
        if (!Objects.equals(this.sedAdscritaPk, other.sedAdscritaPk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        if (!Objects.equals(this.incluirSiTieneAdscritas, other.incluirSiTieneAdscritas)) {
            return false;
        }
        return true;
    }
    
    

}

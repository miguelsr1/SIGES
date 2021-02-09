/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

package sv.gob.mined.siges.filtros;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.TipoSede;

public class FiltroSedes implements Serializable {
    
    private Long sedPk;
    private String sedNombre;
    private String sedCodigo;  
    private String sedCodigoExacto;  
    private String sedCodigoONombre;
    private Boolean sedLegalizada;
    private Long sedDepartamentoId;
    private Long sedMunicipioId;
    private Long sedZonaId;
    private TipoSede sedTipo;
    private List<TipoSede> sedTipos;
    private Long sedTipoCalendario;
    private Boolean priSubvencionada;
    private Long sedServicioEducativo;
    private Boolean cedFineDeLucro;
    private Long cedTipoOrganismoAdministrativoPk; 
    private Boolean sedHabilitado;
    private Long sedAdscritaPk;
    private Boolean sedRevisado;
    private Boolean sedHabilitadaOLegalizada;
    private Long sedImplementadora;
    private Boolean sedPrivadaSubvencionada;
    private Long sedSistemaIntegradoPk;

    private String sedSistemaIntegradoCodigo;
    private String sedSistemaIntegradoNombre;
    private Long sedSiPromotor;
    private String sedAlfAlfabetizadorNombre;
    private String sedAlfPromotorNombre;
    private FiltroNombreCompleto sedAlfAlfabetizadorNombreCompleto;
    private FiltroNombreCompleto sedAlfPromotorNombreCompleto;
    private Boolean sedTieneSistemaIntegrado;
    
    private Long sedNivel;
    private Long sedCiclo;
    private Long sedModalidadEducativa;
    private Long sedOpcion;
    private Long sedGrado;
    private Long sedProgramaEducativo;
    private Long sedModalidadAtencion;
    private Long sedSubModalidad;
    private List<Long> sedesPk;
    
    private EnumAmbito ambito;
    private Boolean soloModalidadesFlexibles;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
        
    private String[] incluirCampos;
    private String securityOperation;
    private Boolean incluirSiTieneAdscritas;
    private Boolean incluirCantidadMatriculasValidadasYNoValidadasAnioCorriente;
    private Boolean incluirCantidadSeccionesAbiertasAnioCorriente;
    private Boolean utilizarFiltrosEnSedePadre;
    private Boolean incluirAdscritas; 
    private Boolean incluirJornadas;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroSedes() {
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_SEDES;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public Boolean getIncluirJornadas() {
        return incluirJornadas;
    }

    public void setIncluirJornadas(Boolean incluirJornadas) {
        this.incluirJornadas = incluirJornadas;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
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

    public List<Long> getSedesPk() {
        return sedesPk;
    }

    public void setSedesPk(List<Long> sedesPk) {
        this.sedesPk = sedesPk;
    }

    public Boolean getUtilizarFiltrosEnSedePadre() {
        return utilizarFiltrosEnSedePadre;
    }

    public void setUtilizarFiltrosEnSedePadre(Boolean utilizarFiltrosEnSedePadre) {
        this.utilizarFiltrosEnSedePadre = utilizarFiltrosEnSedePadre;
    }

    public Boolean getIncluirAdscritas() {
        return incluirAdscritas;
    }

    public void setIncluirAdscritas(Boolean incluirAdscritas) {
        this.incluirAdscritas = incluirAdscritas;
    }

    public List<TipoSede> getSedTipos() {
        return sedTipos;
    }

    public void setSedTipos(List<TipoSede> sedTipos) {
        this.sedTipos = sedTipos;
    }

    public Boolean getSedHabilitadaOLegalizada() {
        return sedHabilitadaOLegalizada;
    }

    public void setSedHabilitadaOLegalizada(Boolean sedHabilitadaOLegalizada) {
        this.sedHabilitadaOLegalizada = sedHabilitadaOLegalizada;
    }

    public Long getSedImplementadora() {
        return sedImplementadora;
    }

    public void setSedImplementadora(Long sedImplementadora) {
        this.sedImplementadora = sedImplementadora;
    }

    public Boolean getSedPrivadaSubvencionada() {
        return sedPrivadaSubvencionada;
    }

    public void setSedPrivadaSubvencionada(Boolean sedPrivadaSubvencionada) {
        this.sedPrivadaSubvencionada = sedPrivadaSubvencionada;
    }
    
    
    public String getSedSistemaIntegradoCodigo() {
        return sedSistemaIntegradoCodigo;
    }

    public void setSedSistemaIntegradoCodigo(String sedSistemaIntegradoCodigo) {
        this.sedSistemaIntegradoCodigo = sedSistemaIntegradoCodigo;
    }

    public String getSedSistemaIntegradoNombre() {
        return sedSistemaIntegradoNombre;
    }

    public void setSedSistemaIntegradoNombre(String sedSistemaIntegradoNombre) {
        this.sedSistemaIntegradoNombre = sedSistemaIntegradoNombre;
    }

    public Long getSedSiPromotor() {
        return sedSiPromotor;
    }

    public void setSedSiPromotor(Long sedSiPromotor) {
        this.sedSiPromotor = sedSiPromotor;
    }

    public String getSedAlfAlfabetizadorNombre() {
        return sedAlfAlfabetizadorNombre;
    }

    public void setSedAlfAlfabetizadorNombre(String sedAlfAlfabetizadorNombre) {
        this.sedAlfAlfabetizadorNombre = sedAlfAlfabetizadorNombre;
    }

    public String getSedAlfPromotorNombre() {
        return sedAlfPromotorNombre;
    }

    public void setSedAlfPromotorNombre(String sedAlfPromotorNombre) {
        this.sedAlfPromotorNombre = sedAlfPromotorNombre;
    }

    public FiltroNombreCompleto getSedAlfAlfabetizadorNombreCompleto() {
        return sedAlfAlfabetizadorNombreCompleto;
    }

    public void setSedAlfAlfabetizadorNombreCompleto(FiltroNombreCompleto sedAlfAlfabetizadorNombreCompleto) {
        this.sedAlfAlfabetizadorNombreCompleto = sedAlfAlfabetizadorNombreCompleto;
    }

    public FiltroNombreCompleto getSedAlfPromotorNombreCompleto() {
        return sedAlfPromotorNombreCompleto;
    }

    public void setSedAlfPromotorNombreCompleto(FiltroNombreCompleto sedAlfPromotorNombreCompleto) {
        this.sedAlfPromotorNombreCompleto = sedAlfPromotorNombreCompleto;
    }

    public Long getSedSistemaIntegradoPk() {
        return sedSistemaIntegradoPk;
    }

    public void setSedSistemaIntegradoPk(Long sedSistemaIntegradoPk) {
        this.sedSistemaIntegradoPk = sedSistemaIntegradoPk;
    }

    public Boolean getSedTieneSistemaIntegrado() {
        return sedTieneSistemaIntegrado;
    }

    public void setSedTieneSistemaIntegrado(Boolean sedTieneSistemaIntegrado) {
        this.sedTieneSistemaIntegrado = sedTieneSistemaIntegrado;
    }

    public String getSedCodigoExacto() {
        return sedCodigoExacto;
    }

    public void setSedCodigoExacto(String sedCodigoExacto) {
        this.sedCodigoExacto = sedCodigoExacto;
    }

    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }

    public Boolean getIncluirCantidadMatriculasValidadasYNoValidadasAnioCorriente() {
        return incluirCantidadMatriculasValidadasYNoValidadasAnioCorriente;
    }

    public void setIncluirCantidadMatriculasValidadasYNoValidadasAnioCorriente(Boolean incluirCantidadMatriculasValidadasYNoValidadasAnioCorriente) {
        this.incluirCantidadMatriculasValidadasYNoValidadasAnioCorriente = incluirCantidadMatriculasValidadasYNoValidadasAnioCorriente;
    }

    public Boolean getIncluirCantidadSeccionesAbiertasAnioCorriente() {
        return incluirCantidadSeccionesAbiertasAnioCorriente;
    }

    public void setIncluirCantidadSeccionesAbiertasAnioCorriente(Boolean incluirCantidadSeccionesAbiertasAnioCorriente) {
        this.incluirCantidadSeccionesAbiertasAnioCorriente = incluirCantidadSeccionesAbiertasAnioCorriente;
    }

    public Boolean getSoloModalidadesFlexibles() {
        return soloModalidadesFlexibles;
    }

    public void setSoloModalidadesFlexibles(Boolean soloModalidadesFlexibles) {
        this.soloModalidadesFlexibles = soloModalidadesFlexibles;
    }

    @Override
    public String toString() {
        return "FiltroSedes{" + "sedPk=" + sedPk + ", sedNombre=" + sedNombre + ", sedCodigo=" + sedCodigo + ", sedCodigoONombre=" + sedCodigoONombre + ", sedLegalizada=" + sedLegalizada + ", sedDepartamentoId=" + sedDepartamentoId + ", sedMunicipioId=" + sedMunicipioId + ", sedZonaId=" + sedZonaId + ", sedTipo=" + sedTipo + ", sedTipoCalendario=" + sedTipoCalendario + ", priSubvencionada=" + priSubvencionada + ", sedServicioEducativo=" + sedServicioEducativo + ", cedFineDeLucro=" + cedFineDeLucro + ", cedTipoOrganismoAdministrativoPk=" + cedTipoOrganismoAdministrativoPk + ", sedHabilitado=" + sedHabilitado + ", sedAdscritaPk=" + sedAdscritaPk + ", sedRevisado=" + sedRevisado + ", sedNivel=" + sedNivel + ", sedCiclo=" + sedCiclo + ", sedModalidadEducativa=" + sedModalidadEducativa + ", sedOpcion=" + sedOpcion + ", sedGrado=" + sedGrado + ", sedProgramaEducativo=" + sedProgramaEducativo + ", sedModalidadAtencion=" + sedModalidadAtencion + ", sedSubModalidad=" + sedSubModalidad + ", sedesPk=" + sedesPk + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", incluirCampos=" + Arrays.deepToString(incluirCampos) + ", securityOperation=" + securityOperation + ", incluirSiTieneAdscritas=" + incluirSiTieneAdscritas + ", utilizarFiltrosEnSedePadre=" + utilizarFiltrosEnSedePadre + ", incluirAdscritas=" + incluirAdscritas + ", incluirJornadas=" + incluirJornadas + ", seNecesitaDistinct=" + seNecesitaDistinct + '}';
    }
    
    

}

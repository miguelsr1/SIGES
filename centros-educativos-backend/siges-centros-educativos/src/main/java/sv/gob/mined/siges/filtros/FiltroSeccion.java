/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;

public class FiltroSeccion implements Serializable {
    
    private Long secSedeFk;
    private Long secNivelFk;
    private Long secCicloFk;
    private Long secModalidadEducativaPk;
    private Long secModalidadAtencionPk;
    private Long secSubModalidadAtencionFk;
    private Long secGradoFk;
    private Long secOpcionFk;
    private Long secProgramaEducativoFk;
    private Long secAnioLectivoFk;
    private Long secPk;
    private Long secPlanEstudioFk;
    private Integer secAnioLectivoAnio;
    private Long secGradoSiguienteFk;
    private Boolean secIntegrada;
    private EnumSeccionEstado estadoSeccion;
    private EnumEstadoProcesamientoCalificacionPromocion estadoPromocion;
    
    private Long secServicioEducativoFk;
    private List<Long> secServiciosEducativoFk;
    private String secNombre;
    private String secCodigo;
    private String codigoSede;
    private String nombreSede;
    private Long departamento;
    private Long municipio;
    private EnumSeccionEstado secEstado;
    private List<Long> seccionesPk;
    private Long secTipoCalendario;
    private List<Integer> secAnios;
    private List<Long> secSubModalidadesAtencionFk;
    private List<Long> secPkIds;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private EnumAmbito ambito;

    private String[] incluirCampos;
    private String[] secIncluirMatriculasCampos;
    private String securityOperation;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    

    public FiltroSeccion() {
        seNecesitaDistinct = Boolean.FALSE;
        securityOperation = ConstantesOperaciones.BUSCAR_SECCION;
    }

    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public Long getSecGradoFk() {
        return secGradoFk;
    }

    public void setSecGradoFk(Long secGradoFk) {
        this.secGradoFk = secGradoFk;
    }

    public Long getSecSedeFk() {
        return secSedeFk;
    }

    public void setSecSedeFk(Long secSedeFk) {
        this.secSedeFk = secSedeFk;
    }

    public Long getSecAnioLectivoFk() {
        return secAnioLectivoFk;
    }

    public void setSecAnioLectivoFk(Long secAnioLectivoFk) {
        this.secAnioLectivoFk = secAnioLectivoFk;
    }

    public EnumSeccionEstado getSecEstado() {
        return secEstado;
    }

    public void setSecEstado(EnumSeccionEstado secEstado) {
        this.secEstado = secEstado;
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

    public Long getSecServicioEducativoFk() {
        return secServicioEducativoFk;
    }

    public void setSecServicioEducativoFk(Long secServicioEducativoFk) {
        this.secServicioEducativoFk = secServicioEducativoFk;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
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

    public Long getSecNivelFk() {
        return secNivelFk;
    }

    public void setSecNivelFk(Long secNivelFk) {
        this.secNivelFk = secNivelFk;
    }

    public Long getSecCicloFk() {
        return secCicloFk;
    }

    public void setSecCicloFk(Long secCicloFk) {
        this.secCicloFk = secCicloFk;
    }

    public Long getSecModalidadEducativaPk() {
        return secModalidadEducativaPk;
    }

    public void setSecModalidadEducativaPk(Long secModalidadEducativaPk) {
        this.secModalidadEducativaPk = secModalidadEducativaPk;
    }

    public Long getSecModalidadAtencionPk() {
        return secModalidadAtencionPk;
    }

    public void setSecModalidadAtencionPk(Long secModalidadAtencionPk) {
        this.secModalidadAtencionPk = secModalidadAtencionPk;
    }

    public Long getSecSubModalidadAtencionFk() {
        return secSubModalidadAtencionFk;
    }

    public void setSecSubModalidadAtencionFk(Long secSubModalidadAtencionFk) {
        this.secSubModalidadAtencionFk = secSubModalidadAtencionFk;
    }

    public Long getSecOpcionFk() {
        return secOpcionFk;
    }

    public void setSecOpcionFk(Long secOpcionFk) {
        this.secOpcionFk = secOpcionFk;
    }

    public Long getSecProgramaEducativoFk() {
        return secProgramaEducativoFk;
    }

    public void setSecProgramaEducativoFk(Long secProgramaEducativoFk) {
        this.secProgramaEducativoFk = secProgramaEducativoFk;
    }

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String[] getSecIncluirMatriculasCampos() {
        return secIncluirMatriculasCampos;
    }

    public void setSecIncluirMatriculasCampos(String[] secIncluirMatriculasCampos) {
        this.secIncluirMatriculasCampos = secIncluirMatriculasCampos;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public List<Long> getSecServiciosEducativoFk() {
        return secServiciosEducativoFk;
    }

    public void setSecServiciosEducativoFk(List<Long> secServiciosEducativoFk) {
        this.secServiciosEducativoFk = secServiciosEducativoFk;
    }

    public List<Long> getSeccionesPk() {
        return seccionesPk;
    }

    public void setSeccionesPk(List<Long> seccionesPk) {
        this.seccionesPk = seccionesPk;
    }

    public Long getSecPlanEstudioFk() {
        return secPlanEstudioFk;
    }

    public void setSecPlanEstudioFk(Long secPlanEstudioFk) {
        this.secPlanEstudioFk = secPlanEstudioFk;
    }

    public Integer getSecAnioLectivoAnio() {
        return secAnioLectivoAnio;
    }

    public void setSecAnioLectivoAnio(Integer secAnioLectivoAnio) {
        this.secAnioLectivoAnio = secAnioLectivoAnio;
    }

    public Long getSecGradoSiguienteFk() {
        return secGradoSiguienteFk;
    }

    public void setSecGradoSiguienteFk(Long secGradoSiguienteFk) {
        this.secGradoSiguienteFk = secGradoSiguienteFk;
    }

    public Long getSecTipoCalendario() {
        return secTipoCalendario;
    }

    public void setSecTipoCalendario(Long secTipoCalendario) {
        this.secTipoCalendario = secTipoCalendario;
    }

    public List<Integer> getSecAnios() {
        return secAnios;
    }

    public void setSecAnios(List<Integer> secAnios) {
        this.secAnios = secAnios;
    }

    public Boolean getSecIntegrada() {
        return secIntegrada;
    }

    public void setSecIntegrada(Boolean secIntegrada) {
        this.secIntegrada = secIntegrada;
    }

    public EnumAmbito getAmbito() {
        return ambito;
    }

    public void setAmbito(EnumAmbito ambito) {
        this.ambito = ambito;
    }

    public List<Long> getSecSubModalidadesAtencionFk() {
        return secSubModalidadesAtencionFk;
    }

    public void setSecSubModalidadesAtencionFk(List<Long> secSubModalidadesAtencionFk) {
        this.secSubModalidadesAtencionFk = secSubModalidadesAtencionFk;
    }

    public EnumSeccionEstado getEstadoSeccion() {
        return estadoSeccion;
    }

    public void setEstadoSeccion(EnumSeccionEstado estadoSeccion) {
        this.estadoSeccion = estadoSeccion;
    }

    public EnumEstadoProcesamientoCalificacionPromocion getEstadoPromocion() {
        return estadoPromocion;
    }

    public void setEstadoPromocion(EnumEstadoProcesamientoCalificacionPromocion estadoPromocion) {
        this.estadoPromocion = estadoPromocion;
    }

    public List<Long> getSecPkIds() {
        return secPkIds;
    }

    public void setSecPkIds(List<Long> secPkIds) {
        this.secPkIds = secPkIds;
    }
     
}

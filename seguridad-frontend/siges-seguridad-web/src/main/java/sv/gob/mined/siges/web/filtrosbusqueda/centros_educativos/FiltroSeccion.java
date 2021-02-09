/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumSeccionEstado;

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
    
    private Long secServicioEducativoFk;
    private String codigoSede;
    private String nombreSede;
    private Long departamento;
    private Long municipio;
    private String secNombre;
    private String secCodigo;
    
    private EnumSeccionEstado secEstado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    private String[] secIncluirMatriculasCampos;
    
    public FiltroSeccion() {
        this.first = 0L;
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

    public String getSecNombre() {
        return secNombre;
    }

    public void setSecNombre(String secNombre) {
        this.secNombre = secNombre;
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

    public String[] getSecIncluirMatriculasCampos() {
        return secIncluirMatriculasCampos;
    }

    public void setSecIncluirMatriculasCampos(String[] secIncluirMatriculasCampos) {
        this.secIncluirMatriculasCampos = secIncluirMatriculasCampos;
    }


}

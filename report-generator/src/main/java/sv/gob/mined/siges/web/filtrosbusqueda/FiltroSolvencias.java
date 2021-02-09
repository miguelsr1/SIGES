/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;

public class FiltroSolvencias implements Serializable {
    private TipoUnidad tipoUnidad;
    private Long unidadActivoFijoId;
    private Long unidadAdministrativaId;
    private Long departamentoId;
    private Long municipioId;
    private Boolean actualizado;
    private Integer anio;
    private Boolean validarEstadoSolicitudVacio;
    private Boolean validarEstadoSolicitud;
    
    private Long descargoId;
    private Long bienId;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public Long getUnidadActivoFijoId() {
        return unidadActivoFijoId;
    }

    public void setUnidadActivoFijoId(Long unidadActivoFijoId) {
        this.unidadActivoFijoId = unidadActivoFijoId;
    }

    public Long getUnidadAdministrativaId() {
        return unidadAdministrativaId;
    }

    public void setUnidadAdministrativaId(Long unidadAdministrativaId) {
        this.unidadAdministrativaId = unidadAdministrativaId;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public Boolean getActualizado() {
        return actualizado;
    }

    public void setActualizado(Boolean actualizado) {
        this.actualizado = actualizado;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getDescargoId() {
        return descargoId;
    }

    public void setDescargoId(Long descargoId) {
        this.descargoId = descargoId;
    }

    public Long getBienId() {
        return bienId;
    }

    public void setBienId(Long bienId) {
        this.bienId = bienId;
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

    public Boolean getValidarEstadoSolicitudVacio() {
        return validarEstadoSolicitudVacio;
    }

    public void setValidarEstadoSolicitudVacio(Boolean validarEstadoSolicitudVacio) {
        this.validarEstadoSolicitudVacio = validarEstadoSolicitudVacio;
    }

    public Boolean getValidarEstadoSolicitud() {
        return validarEstadoSolicitud;
    }

    public void setValidarEstadoSolicitud(Boolean validarEstadoSolicitud) {
        this.validarEstadoSolicitud = validarEstadoSolicitud;
    }
    
    
}

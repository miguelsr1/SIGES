/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;

public class FiltroDescargosDetalle implements Serializable {
    private TipoUnidad tipoUnidad;
    private Long unidadActivoFijoId;
    private Long unidadAdministrativaId;
    private Long departamentoId;
    private Long municipioId;
    private Long estadoId;
    private Long tipoDescargo;
    private String codigoInventario;
    private String numeroActa;
    private Boolean activos;
    private LocalDate fechaDescargoDesde;
    private LocalDate fechaDescargoHasta;
    private Long clasificacionId;
    private Long categoriaId;
    private LocalDate fechaSolicitudDesde;
    private LocalDate fechaSolicitudHasta;
    private String codigoDescargo;
    private Long descargoId;
    private Long bienId;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public Long getDescargoId() {
        return descargoId;
    }

    public void setDescargoId(Long descargoId) {
        this.descargoId = descargoId;
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

    public Long getBienId() {
        return bienId;
    }

    public void setBienId(Long bienId) {
        this.bienId = bienId;
    }

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

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public String getNumeroActa() {
        return numeroActa;
    }

    public void setNumeroActa(String numeroActa) {
        this.numeroActa = numeroActa;
    }

    public Boolean getActivos() {
        return activos;
    }

    public void setActivos(Boolean activos) {
        this.activos = activos;
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

    public Long getTipoDescargo() {
        return tipoDescargo;
    }

    public void setTipoDescargo(Long tipoDescargo) {
        this.tipoDescargo = tipoDescargo;
    }

    public LocalDate getFechaDescargoDesde() {
        return fechaDescargoDesde;
    }

    public void setFechaDescargoDesde(LocalDate fechaDescargoDesde) {
        this.fechaDescargoDesde = fechaDescargoDesde;
    }

    public LocalDate getFechaDescargoHasta() {
        return fechaDescargoHasta;
    }

    public void setFechaDescargoHasta(LocalDate fechaDescargoHasta) {
        this.fechaDescargoHasta = fechaDescargoHasta;
    }

    public Long getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(Long clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LocalDate getFechaSolicitudDesde() {
        return fechaSolicitudDesde;
    }

    public void setFechaSolicitudDesde(LocalDate fechaSolicitudDesde) {
        this.fechaSolicitudDesde = fechaSolicitudDesde;
    }

    public LocalDate getFechaSolicitudHasta() {
        return fechaSolicitudHasta;
    }

    public void setFechaSolicitudHasta(LocalDate fechaSolicitudHasta) {
        this.fechaSolicitudHasta = fechaSolicitudHasta;
    }

    public String getCodigoDescargo() {
        return codigoDescargo;
    }

    public void setCodigoDescargo(String codigoDescargo) {
        this.codigoDescargo = codigoDescargo;
    }
    
    
}

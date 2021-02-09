/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;

public class FiltroLoteBienes implements Serializable {
    private Long bienDepreciableId;
    private EnumEstadosProceso estado;
    private TipoUnidad tipoUnidad;
    private Long unidadAdministrativaId;
    
    private LocalDate fechaProcesoDesde;
    private LocalDate fechaProcesoHasta;
    private Boolean diferenteEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private String securityOperation;
    
    public FiltroLoteBienes() {
        this.securityOperation = ConstantesOperaciones.BUSCAR_LOTE_BIENES;
    }
    
    public Long getBienDepreciableId() {
        return bienDepreciableId;
    }

    public void setBienDepreciableId(Long bienDepreciableId) {
        this.bienDepreciableId = bienDepreciableId;
    }

    public EnumEstadosProceso getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadosProceso estado) {
        this.estado = estado;
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

    public LocalDate getFechaProcesoDesde() {
        return fechaProcesoDesde;
    }

    public void setFechaProcesoDesde(LocalDate fechaProcesoDesde) {
        this.fechaProcesoDesde = fechaProcesoDesde;
    }

    public LocalDate getFechaProcesoHasta() {
        return fechaProcesoHasta;
    }

    public void setFechaProcesoHasta(LocalDate fechaProcesoHasta) {
        this.fechaProcesoHasta = fechaProcesoHasta;
    }

    public Boolean getDiferenteEstado() {
        return diferenteEstado;
    }

    public void setDiferenteEstado(Boolean diferenteEstado) {
        this.diferenteEstado = diferenteEstado;
    }

    public TipoUnidad getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(TipoUnidad tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public Long getUnidadAdministrativaId() {
        return unidadAdministrativaId;
    }

    public void setUnidadAdministrativaId(Long unidadAdministrativaId) {
        this.unidadAdministrativaId = unidadAdministrativaId;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }
    
    
}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;

public class FiltroLiquidacionProyectos implements Serializable {
    
    private Long fuenteId;
    private Long proyectoId;
    private LocalDate fechaLiquidacionDesde;
    private LocalDate fechaLiquidacionHasta;
    private EnumEstadosProceso estado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Boolean seNecesitaDistinct;
    private String securityOperation;
    
    public FiltroLiquidacionProyectos() {
        securityOperation = ConstantesOperaciones.BUSCAR_LIQUIDACIONES_PROYECTOS;
    }

    public Long getFuenteId() {
        return fuenteId;
    }

    public void setFuenteId(Long fuenteId) {
        this.fuenteId = fuenteId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public LocalDate getFechaLiquidacionDesde() {
        return fechaLiquidacionDesde;
    }

    public void setFechaLiquidacionDesde(LocalDate fechaLiquidacionDesde) {
        this.fechaLiquidacionDesde = fechaLiquidacionDesde;
    }

    public LocalDate getFechaLiquidacionHasta() {
        return fechaLiquidacionHasta;
    }

    public void setFechaLiquidacionHasta(LocalDate fechaLiquidacionHasta) {
        this.fechaLiquidacionHasta = fechaLiquidacionHasta;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }
}

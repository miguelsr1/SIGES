/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;

public class FiltroLiquidacionProyectos implements Serializable {
    
    private Long fuenteId;
    private Long proyectoId;
    private LocalDate fechaLiquidacionDesde;
    private LocalDate fechaLiquidacionHasta;
    private EnumEstadosProceso estado;
    private Boolean diferenteEstado;
    private List<EnumEstadosProceso> estados;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private String securityOperation;
    //Auxiliar
    private Boolean seNecesitaDistinct;

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

    public Boolean getDiferenteEstado() {
        return diferenteEstado;
    }

    public void setDiferenteEstado(Boolean diferenteEstado) {
        this.diferenteEstado = diferenteEstado;
    }

    public List<EnumEstadosProceso> getEstados() {
        return estados;
    }

    public void setEstados(List<EnumEstadosProceso> estados) {
        this.estados = estados;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }
}

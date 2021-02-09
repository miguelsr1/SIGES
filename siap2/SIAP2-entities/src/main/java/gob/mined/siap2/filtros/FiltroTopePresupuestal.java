package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import java.io.Serializable;
import java.util.List;

public class FiltroTopePresupuestal implements Serializable {

    private Integer departamentoId;
    private Integer anioFiscalId;
    private Integer compPresAnioFiscalId;
    private Integer fuenteFinId;
    private Integer fuenteRecursoId;
    private Integer sedeId;
    private Integer cuentaId;
    private Integer componentePresupuestoEscolarId;
    private Integer presupuestoEscolarMovimientoTopeId;
    private EstadoTopePresupuestal estado;
    private List<Integer> sedesId;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Integer getSedeId() {
        return sedeId;
    }

    public void setSedeId(Integer sedeId) {
        this.sedeId = sedeId;
    }

    public Integer getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Integer cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Integer getComponentePresupuestoEscolarId() {
        return componentePresupuestoEscolarId;
    }

    public void setComponentePresupuestoEscolarId(Integer componentePresupuestoEscolarId) {
        this.componentePresupuestoEscolarId = componentePresupuestoEscolarId;
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

    public List<Integer> getSedesId() {
        return sedesId;
    }

    public void setSedesId(List<Integer> sedesId) {
        this.sedesId = sedesId;
    }

    public EstadoTopePresupuestal getEstado() {
        return estado;
    }

    public void setEstado(EstadoTopePresupuestal estado) {
        this.estado = estado;
    }

    public Integer getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Integer departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Integer getPresupuestoEscolarMovimientoTopeId() {
        return presupuestoEscolarMovimientoTopeId;
    }

    public void setPresupuestoEscolarMovimientoTopeId(Integer presupuestoEscolarMovimientoTopeId) {
        this.presupuestoEscolarMovimientoTopeId = presupuestoEscolarMovimientoTopeId;
    }

    public Integer getCompPresAnioFiscalId() {
        return compPresAnioFiscalId;
    }

    public void setCompPresAnioFiscalId(Integer compPresAnioFiscalId) {
        this.compPresAnioFiscalId = compPresAnioFiscalId;
    }

    public Integer getFuenteFinId() {
        return fuenteFinId;
    }

    public void setFuenteFinId(Integer fuenteFinId) {
        this.fuenteFinId = fuenteFinId;
    }

    public Integer getFuenteRecursoId() {
        return fuenteRecursoId;
    }

    public void setFuenteRecursoId(Integer fuenteRecursoId) {
        this.fuenteRecursoId = fuenteRecursoId;
    }

   

    
    
}

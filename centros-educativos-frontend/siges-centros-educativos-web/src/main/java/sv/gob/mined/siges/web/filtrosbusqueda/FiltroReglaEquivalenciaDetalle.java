/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumOperacionReglaEquivalencia;
public class FiltroReglaEquivalenciaDetalle implements Serializable {
    private Long reglaNorativaDetalleId;
    private Long reglaNormativaId;
    private Long gradoId;
    private Long planEstudioId;
    private EnumOperacionReglaEquivalencia operacion;
    private Boolean reglaHabilitada;
    private List<Long> reglaPk;
    private Long opcion;
    private Long programaEducativo;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean habilitado;  
    
    public FiltroReglaEquivalenciaDetalle(){
        
    }

    public Long getReglaNorativaDetalleId() {
        return reglaNorativaDetalleId;
    }

    public void setReglaNorativaDetalleId(Long reglaNorativaDetalleId) {
        this.reglaNorativaDetalleId = reglaNorativaDetalleId;
    }

    public Long getReglaNormativaId() {
        return reglaNormativaId;
    }

    public void setReglaNormativaId(Long reglaNormativaId) {
        this.reglaNormativaId = reglaNormativaId;
    }

    public Long getGradoId() {
        return gradoId;
    }

    public void setGradoId(Long gradoId) {
        this.gradoId = gradoId;
    }

    public Long getPlanEstudioId() {
        return planEstudioId;
    }

    public void setPlanEstudioId(Long planEstudioId) {
        this.planEstudioId = planEstudioId;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public EnumOperacionReglaEquivalencia getOperacion() {
        return operacion;
    }

    public void setOperacion(EnumOperacionReglaEquivalencia operacion) {
        this.operacion = operacion;
    }

    public Boolean getReglaHabilitada() {
        return reglaHabilitada;
    }

    public void setReglaHabilitada(Boolean reglaHabilitada) {
        this.reglaHabilitada = reglaHabilitada;
    }
    
    public List<Long> getReglaPk() {
        return reglaPk;
    }

    public void setReglaPk(List<Long> reglaPk) {
        this.reglaPk = reglaPk;
    }

    public Long getOpcion() {
        return opcion;
    }

    public void setOpcion(Long opcion) {
        this.opcion = opcion;
    }

    public Long getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(Long programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    @Override
    public String toString() {
        return "FiltroReglaEquivalenciaDetalle{" + "reglaNorativaDetalleId=" + reglaNorativaDetalleId + ", reglaNormativaId=" + reglaNormativaId + ", gradoId=" + gradoId + ", planEstudioId=" + planEstudioId + ", operacion=" + operacion + ", reglaHabilitada=" + reglaHabilitada + ", reglaPk=" + reglaPk + ", opcion=" + opcion + ", programaEducativo=" + programaEducativo + ", incluirCampos=" + incluirCampos + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", habilitado=" + habilitado + '}';
    }


}

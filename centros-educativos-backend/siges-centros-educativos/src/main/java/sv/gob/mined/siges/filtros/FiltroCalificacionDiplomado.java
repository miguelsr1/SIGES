/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumTiposCalificacionDiplomado;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroCalificacionDiplomado implements Serializable {
    
    private Long calAnioLectivoFk;
    private Long calSedeFk;
    private Long calModuloDiplomadoFk;
    private Integer calNumeroPeriodo;
    private EnumTiposCalificacionDiplomado cadTipoCalificacion;
    private Boolean incluirCalificacionEstudiante;
    private Long calDepartamentoFk;
    private Long calDiplomadoFk;
    
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String securityOperation;
    
    private Boolean seNecesitaDistinct;
   
    public FiltroCalificacionDiplomado() {
    }

    public Long getCalAnioLectivoFk() {
        return calAnioLectivoFk;
    }

    public void setCalAnioLectivoFk(Long calAnioLectivoFk) {
        this.calAnioLectivoFk = calAnioLectivoFk;
    }

    public Long getCalSedeFk() {
        return calSedeFk;
    }

    public void setCalSedeFk(Long calSedeFk) {
        this.calSedeFk = calSedeFk;
    }

    public Long getCalModuloDiplomadoFk() {
        return calModuloDiplomadoFk;
    }

    public void setCalModuloDiplomadoFk(Long calModuloDiplomadoFk) {
        this.calModuloDiplomadoFk = calModuloDiplomadoFk;
    }

    public Integer getCalNumeroPeriodo() {
        return calNumeroPeriodo;
    }

    public void setCalNumeroPeriodo(Integer calNumeroPeriodo) {
        this.calNumeroPeriodo = calNumeroPeriodo;
    }   

    public EnumTiposCalificacionDiplomado getCadTipoCalificacion() {
        return cadTipoCalificacion;
    }

    public void setCadTipoCalificacion(EnumTiposCalificacionDiplomado cadTipoCalificacion) {
        this.cadTipoCalificacion = cadTipoCalificacion;
    }

    

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

   
    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
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

    public Boolean getIncluirCalificacionEstudiante() {
        return incluirCalificacionEstudiante;
    }

    public void setIncluirCalificacionEstudiante(Boolean incluirCalificacionEstudiante) {
        this.incluirCalificacionEstudiante = incluirCalificacionEstudiante;
    }

    public Long getCalDepartamentoFk() {
        return calDepartamentoFk;
    }

    public void setCalDepartamentoFk(Long calDepartamentoFk) {
        this.calDepartamentoFk = calDepartamentoFk;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getCalDiplomadoFk() {
        return calDiplomadoFk;
    }

    public void setCalDiplomadoFk(Long calDiplomadoFk) {
        this.calDiplomadoFk = calDiplomadoFk;
    }


}

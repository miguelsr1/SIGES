/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumTiposCalificacionDiplomado;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroCalificacionDiplomadoEstudiante implements Serializable {
    
    private Long cdeEstudianteFk;
    private Long cdeCalificacionDiplomadoFk;
    private Long sedeFk;
    private Long anioLectivoFk;
    private Long moduloDiplomadoFk;
    private Long diplomadoFk;
    private List<EnumTiposCalificacionDiplomado> tipoCalificacionDiplomadoList;
    
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String securityOperation;
   
    public FiltroCalificacionDiplomadoEstudiante() {
    }

    public Long getDiplomadoFk() {
        return diplomadoFk;
    }

    public void setDiplomadoFk(Long diplomadoFk) {
        this.diplomadoFk = diplomadoFk;
    }

    public Long getCdeEstudianteFk() {
        return cdeEstudianteFk;
    }

    public void setCdeEstudianteFk(Long cdeEstudianteFk) {
        this.cdeEstudianteFk = cdeEstudianteFk;
    }

    public Long getCdeCalificacionDiplomadoFk() {
        return cdeCalificacionDiplomadoFk;
    }

    public void setCdeCalificacionDiplomadoFk(Long cdeCalificacionDiplomadoFk) {
        this.cdeCalificacionDiplomadoFk = cdeCalificacionDiplomadoFk;
    }

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Long getAnioLectivoFk() {
        return anioLectivoFk;
    }

    public void setAnioLectivoFk(Long anioLectivoFk) {
        this.anioLectivoFk = anioLectivoFk;
    }

    public Long getModuloDiplomadoFk() {
        return moduloDiplomadoFk;
    }

    public void setModuloDiplomadoFk(Long moduloDiplomadoFk) {
        this.moduloDiplomadoFk = moduloDiplomadoFk;
    }

    public List<EnumTiposCalificacionDiplomado> getTipoCalificacionDiplomadoList() {
        return tipoCalificacionDiplomadoList;
    }

    public void setTipoCalificacionDiplomadoList(List<EnumTiposCalificacionDiplomado> tipoCalificacionDiplomadoList) {
        this.tipoCalificacionDiplomadoList = tipoCalificacionDiplomadoList;
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


}

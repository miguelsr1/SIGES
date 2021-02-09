package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;

public class FiltroRelSustitucionMiembroOAE implements Serializable {

    private Long rsmSustitucionMiembroFk;
    private Long rsmMiembroaSustituirFk;
    private Long rsmMiembroSustituyenteFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRelSustitucionMiembroOAE() {
    }

    public Long getRsmSustitucionMiembroFk() {
        return rsmSustitucionMiembroFk;
    }

    public void setRsmSustitucionMiembroFk(Long rsmSustitucionMiembroFk) {
        this.rsmSustitucionMiembroFk = rsmSustitucionMiembroFk;
    }

    public Long getRsmMiembroaSustituirFk() {
        return rsmMiembroaSustituirFk;
    }

    public void setRsmMiembroaSustituirFk(Long rsmMiembroaSustituirFk) {
        this.rsmMiembroaSustituirFk = rsmMiembroaSustituirFk;
    }

    public Long getRsmMiembroSustituyenteFk() {
        return rsmMiembroSustituyenteFk;
    }

    public void setRsmMiembroSustituyenteFk(Long rsmMiembroSustituyenteFk) {
        this.rsmMiembroSustituyenteFk = rsmMiembroSustituyenteFk;
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

  
    
}

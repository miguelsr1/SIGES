package sv.gob.mined.siges.filtros;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumEstadoSustitucionMiembroOAE;

public class FiltroRelSustitucionMiembroOAE implements Serializable {

    private Long rsmSustitucionMiembroFk;
    private Long rsmMiembroaSustituirFk;
    private Long rsmMiembroSustituyenteFk;
    private EnumEstadoSustitucionMiembroOAE rsmEstado;
    private List<Long> rsmMiembrosSustituirPkList;
    private Long rsmOAEPk;
    
    //pk de cabezales de sustitucion
    private List<Long> rsmSustituirMiembroOAEList;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRelSustitucionMiembroOAE() {
    }

    public Long getRsmOAEPk() {
        return rsmOAEPk;
    }

    public void setRsmOAEPk(Long rsmOAEPk) {
        this.rsmOAEPk = rsmOAEPk;
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

    public EnumEstadoSustitucionMiembroOAE getRsmEstado() {
        return rsmEstado;
    }

    public void setRsmEstado(EnumEstadoSustitucionMiembroOAE rsmEstado) {
        this.rsmEstado = rsmEstado;
    }

    public List<Long> getRsmMiembrosSustituirPkList() {
        return rsmMiembrosSustituirPkList;
    }

    public void setRsmMiembrosSustituirPkList(List<Long> rsmMiembrosSustituirPkList) {
        this.rsmMiembrosSustituirPkList = rsmMiembrosSustituirPkList;
    }

    public List<Long> getRsmSustituirMiembroOAEList() {
        return rsmSustituirMiembroOAEList;
    }

    public void setRsmSustituirMiembroOAEList(List<Long> rsmSustituirMiembroOAEList) {
        this.rsmSustituirMiembroOAEList = rsmSustituirMiembroOAEList;
    }

  
    
}

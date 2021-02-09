package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;

public class FiltroAsistenciaPersonal implements Serializable {

    private Long apePk;
    private Boolean apeInasistencia;
    private Long apeCabezal;
    private Long apePersonal;
    private Long apeMotivoInasistencia;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroAsistenciaPersonal() {
    }

    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public Boolean getApeInasistencia() {
        return apeInasistencia;
    }

    public void setApeInasistencia(Boolean apeInasistencia) {
        this.apeInasistencia = apeInasistencia;
    }

    public Long getApeCabezal() {
        return apeCabezal;
    }

    public void setApeCabezal(Long apeCabezal) {
        this.apeCabezal = apeCabezal;
    }

    public Long getApePersonal() {
        return apePersonal;
    }

    public void setApePersonal(Long apePersonal) {
        this.apePersonal = apePersonal;
    }

    public Long getApeMotivoInasistencia() {
        return apeMotivoInasistencia;
    }

    public void setApeMotivoInasistencia(Long apeMotivoInasistencia) {
        this.apeMotivoInasistencia = apeMotivoInasistencia;
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

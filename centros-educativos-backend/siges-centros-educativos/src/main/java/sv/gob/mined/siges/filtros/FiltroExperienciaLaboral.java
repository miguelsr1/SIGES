package sv.gob.mined.siges.filtros;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;

public class FiltroExperienciaLaboral implements Serializable {
    
    private Long elaPk;
    private Long elaPersonalPk;
    private Long elaDatoEmpleadoPk;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroExperienciaLaboral() {
    }

    public Long getElaPk() {
        return elaPk;
    }

    public void setElaPk(Long elaPk) {
        this.elaPk = elaPk;
    }

    public Long getElaDatoEmpleadoPk() {
        return elaDatoEmpleadoPk;
    }

    public void setElaDatoEmpleadoPk(Long elaDatoEmpleadoPk) {
        this.elaDatoEmpleadoPk = elaDatoEmpleadoPk;
    }
    
    public Long getElaPersonalPk() {
        return elaPersonalPk;
    }

    public void setElaPersonalPk(Long elaPersonalPk) {
        this.elaPersonalPk = elaPersonalPk;
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

    
    

    
}

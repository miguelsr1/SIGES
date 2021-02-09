/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;


public class FiltroGestionAnioLectivo implements Serializable {
    
    private Long galPk;
    private String galCodigo;
    private Boolean galAnioActual;
    private Boolean galHabilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroGestionAnioLectivo() {
    }

    public Long getGalPk() {
        return galPk;
    }

    public void setGalPk(Long galPk) {
        this.galPk = galPk;
    }

    public String getGalCodigo() {
        return galCodigo;
    }

    public void setGalCodigo(String galCodigo) {
        this.galCodigo = galCodigo;
    }

    public Boolean getGalAnioActual() {
        return galAnioActual;
    }

    public void setGalAnioActual(Boolean galAnioActual) {
        this.galAnioActual = galAnioActual;
    }

    public Boolean getGalHabilitado() {
        return galHabilitado;
    }

    public void setGalHabilitado(Boolean galHabilitado) {
        this.galHabilitado = galHabilitado;
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

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoImportado;


public class FiltroArchivoCalificaciones implements Serializable {
    
    private Long accPk;
    private EnumEstadoImportado accEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroArchivoCalificaciones() {
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public EnumEstadoImportado getAccEstado() {
        return accEstado;
    }

    public void setAccEstado(EnumEstadoImportado accEstado) {
        this.accEstado = accEstado;
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

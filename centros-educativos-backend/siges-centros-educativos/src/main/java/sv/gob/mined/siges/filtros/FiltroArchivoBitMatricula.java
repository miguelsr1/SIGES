/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;

public class FiltroArchivoBitMatricula implements Serializable {
    
    private Long ambPk;
    private EnumEstadoImportado abmEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroArchivoBitMatricula() {
    }

    public Long getAmbPk() {
        return ambPk;
    }

    public void setAmbPk(Long ambPk) {
        this.ambPk = ambPk;
    }

    public EnumEstadoImportado getAbmEstado() {
        return abmEstado;
    }

    public void setAbmEstado(EnumEstadoImportado abmEstado) {
        this.abmEstado = abmEstado;
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

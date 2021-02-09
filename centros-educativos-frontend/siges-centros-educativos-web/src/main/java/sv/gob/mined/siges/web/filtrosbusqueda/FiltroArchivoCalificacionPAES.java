/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoArchivoCalificacionPAES;

public class FiltroArchivoCalificacionPAES implements Serializable {
    
    private Long acpPk;
    private EnumEstadoArchivoCalificacionPAES acpEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroArchivoCalificacionPAES() {
    }

    public Long getAcpPk() {
        return acpPk;
    }

    public void setAcpPk(Long acpPk) {
        this.acpPk = acpPk;
    }

    public EnumEstadoArchivoCalificacionPAES getAcpEstado() {
        return acpEstado;
    }

    public void setAcpEstado(EnumEstadoArchivoCalificacionPAES acpEstado) {
        this.acpEstado = acpEstado;
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

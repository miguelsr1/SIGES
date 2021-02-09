/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroIdentificacion implements Serializable {

    private Long idePk;
    private String ideNumeroDocumento;
    private Long idePersona;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroIdentificacion() {
    }

    public Long getIdePk() {
        return idePk;
    }

    public void setIdePk(Long idePk) {
        this.idePk = idePk;
    }

    public String getIdeNumeroDocumento() {
        return ideNumeroDocumento;
    }

    public void setIdeNumeroDocumento(String ideNumeroDocumento) {
        this.ideNumeroDocumento = ideNumeroDocumento;
    }

    public Long getIdePersona() {
        return idePersona;
    }

    public void setIdePersona(Long idePersona) {
        this.idePersona = idePersona;
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

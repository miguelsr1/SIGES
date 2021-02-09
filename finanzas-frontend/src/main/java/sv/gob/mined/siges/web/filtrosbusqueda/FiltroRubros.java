/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroRubros implements Serializable {

    private Long ruId;
    private String ruNombre;
    private String ruCodigo;
    private Boolean ruHabilitado;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRubros() {
    }

    public void setRuId(Long ruId) {
        this.ruId = ruId;
    }

    public void setRuNombre(String ruNombre) {
        this.ruNombre = ruNombre;
    }

    public void setRuCodigo(String ruCodigo) {
        this.ruCodigo = ruCodigo;
    }

    public void setRuHabilitado(Boolean ruHabilitado) {
        this.ruHabilitado = ruHabilitado;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public String getRuNombre() {
        return ruNombre;
    }

    public String getRuCodigo() {
        return ruCodigo;
    }

    public Boolean getRuHabilitado() {
        return ruHabilitado;
    }

    public Long getFirst() {
        return first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public Long getRuId() {
        return ruId;
    }

}

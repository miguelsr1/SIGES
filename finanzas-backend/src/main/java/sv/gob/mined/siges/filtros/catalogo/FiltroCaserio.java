/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros.catalogo;

import java.io.Serializable;

/**
 * Filtro de caseríos
 *
 * @author jgiron
 */
public class FiltroCaserio implements Serializable {

    private String casCodigo;
    private String casCodigoExacto;

    private String casNombre;
    private String casNombreExacto;

    private String casCanton;
    private String casCantonExacto;

    private Boolean casHabilitado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroCaserio() {
    }

    public String getCasCodigo() {
        return casCodigo;
    }

    public void setCasCodigo(String casCodigo) {
        this.casCodigo = casCodigo;
    }

    public String getCasCodigoExacto() {
        return casCodigoExacto;
    }

    public void setCasCodigoExacto(String casCodigoExacto) {
        this.casCodigoExacto = casCodigoExacto;
    }

    public String getCasNombre() {
        return casNombre;
    }

    public void setCasNombre(String casNombre) {
        this.casNombre = casNombre;
    }

    public String getCasNombreExacto() {
        return casNombreExacto;
    }

    public void setCasNombreExacto(String casNombreExacto) {
        this.casNombreExacto = casNombreExacto;
    }

    public String getCasCanton() {
        return casCanton;
    }

    public void setCasCanton(String casCanton) {
        this.casCanton = casCanton;
    }

    public String getCasCantonExacto() {
        return casCantonExacto;
    }

    public void setCasCantonExacto(String casCantonExacto) {
        this.casCantonExacto = casCantonExacto;
    }

    public Boolean getCasHabilitado() {
        return casHabilitado;
    }

    public void setCasHabilitado(Boolean casHabilitado) {
        this.casHabilitado = casHabilitado;
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

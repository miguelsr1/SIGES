/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.catalogo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroCaserio implements Serializable {

    private String casCodigo;
    private String casCodigoExacto;
    private String casNombre;
    private String casNombreExacto;
    private String casCanton;
    private String casCantonExacto;
    private Boolean casHabilitado;
    private Long casCantonFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

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

    public Long getCasCantonFk() {
        return casCantonFk;
    }

    public void setCasCantonFk(Long casCantonFk) {
        this.casCantonFk = casCantonFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    // Equals y hashcode utilizados por jcache
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.casCodigo);
        hash = 53 * hash + Objects.hashCode(this.casCodigoExacto);
        hash = 53 * hash + Objects.hashCode(this.casNombre);
        hash = 53 * hash + Objects.hashCode(this.casNombreExacto);
        hash = 53 * hash + Objects.hashCode(this.casCanton);
        hash = 53 * hash + Objects.hashCode(this.casCantonExacto);
        hash = 53 * hash + Objects.hashCode(this.casHabilitado);
        hash = 53 * hash + Objects.hashCode(this.casCantonFk);
        hash = 53 * hash + Objects.hashCode(this.first);
        hash = 53 * hash + Objects.hashCode(this.maxResults);
        hash = 53 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 53 * hash + Arrays.hashCode(this.ascending);
        hash = 53 * hash + Arrays.deepHashCode(this.incluirCampos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroCaserio other = (FiltroCaserio) obj;
        if (!Objects.equals(this.casCodigo, other.casCodigo)) {
            return false;
        }
        if (!Objects.equals(this.casCodigoExacto, other.casCodigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.casNombre, other.casNombre)) {
            return false;
        }
        if (!Objects.equals(this.casNombreExacto, other.casNombreExacto)) {
            return false;
        }
        if (!Objects.equals(this.casCanton, other.casCanton)) {
            return false;
        }
        if (!Objects.equals(this.casCantonExacto, other.casCantonExacto)) {
            return false;
        }
        if (!Objects.equals(this.casHabilitado, other.casHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.casCantonFk, other.casCantonFk)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }

}

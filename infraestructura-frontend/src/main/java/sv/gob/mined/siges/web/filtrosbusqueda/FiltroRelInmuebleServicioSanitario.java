/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroRelInmuebleServicioSanitario implements Serializable {

    private Long inmuebleFk;
    private Long servicioSanitarioFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroRelInmuebleServicioSanitario() {
    }

    public Long getInmuebleFk() {
        return inmuebleFk;
    }

    public void setInmuebleFk(Long inmuebleFk) {
        this.inmuebleFk = inmuebleFk;
    }

    public Long getServicioSanitarioFk() {
        return servicioSanitarioFk;
    }

    public void setServicioSanitarioFk(Long servicioSanitarioFk) {
        this.servicioSanitarioFk = servicioSanitarioFk;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.first);
        hash = 47 * hash + Objects.hashCode(this.maxResults);
        hash = 47 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 47 * hash + Arrays.hashCode(this.ascending);
        hash = 47 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRelInmuebleServicioSanitario other = (FiltroRelInmuebleServicioSanitario) obj;
       
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

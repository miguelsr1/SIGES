/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.enumerados.EnumTipoUnidadResponsable;

public class FiltroRelInmuebleUnidadResp implements Serializable {

    private Long inmuebleFk;
    private EnumTipoUnidadResponsable tipoUnidadResp;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroRelInmuebleUnidadResp() {
    }

    public Long getInmuebleFk() {
        return inmuebleFk;
    }

    public void setInmuebleFk(Long inmuebleFk) {
        this.inmuebleFk = inmuebleFk;
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

    public EnumTipoUnidadResponsable getTipoUnidadResp() {
        return tipoUnidadResp;
    }

    public void setTipoUnidadResp(EnumTipoUnidadResponsable tipoUnidadResp) {
        this.tipoUnidadResp = tipoUnidadResp;
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
        final FiltroRelInmuebleUnidadResp other = (FiltroRelInmuebleUnidadResp) obj;
       
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

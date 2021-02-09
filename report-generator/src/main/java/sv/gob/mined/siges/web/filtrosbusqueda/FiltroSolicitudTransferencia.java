/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;

public class FiltroSolicitudTransferencia implements Serializable {

    private Long strPk;
    private String[] orderBy;
    private boolean[] ascending;
    private Long first;
    private Long maxResults;
    private String[] incluirCampos;

    public FiltroSolicitudTransferencia() {
    }

    public Long getStrPk() {
        return strPk;
    }

    public void setStrPk(Long desPk) {
        this.strPk = desPk;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.strPk);
        hash = 79 * hash + Objects.hashCode(this.ascending);
        hash = 79 * hash + Objects.hashCode(this.orderBy);
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
        final FiltroSolicitudTransferencia other = (FiltroSolicitudTransferencia) obj;
        if (!Objects.equals(this.strPk, other.strPk)) {
            return false;
        }
        return true;
    }

}

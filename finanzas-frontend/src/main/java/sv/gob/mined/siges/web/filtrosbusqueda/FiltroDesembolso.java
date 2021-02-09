/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;

public class FiltroDesembolso implements Serializable {

    private Long desPk;
    private EnumDesembolsoEstado estado;
    String[] orderBy;
    boolean[] ascending;
    private Long first;
    private Long maxResults;
    private String[] incluirCampos;

    public FiltroDesembolso() {
    }

    public Long getDesPk() {
        return desPk;
    }

    public void setDesPk(Long desPk) {
        this.desPk = desPk;
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

    public EnumDesembolsoEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumDesembolsoEstado estado) {
        this.estado = estado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.desPk);
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
        final FiltroDesembolso other = (FiltroDesembolso) obj;
        if (!Objects.equals(this.desPk, other.desPk)) {
            return false;
        }
        return true;
    }

}

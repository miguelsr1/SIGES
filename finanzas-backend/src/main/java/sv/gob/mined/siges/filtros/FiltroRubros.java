/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Filtro de rubros para otros ingresos
 *
 * @author jgiron
 */
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

    public Long getRuId() {
        return ruId;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.ruId);
        hash = 79 * hash + Objects.hashCode(this.ruNombre);
        hash = 79 * hash + Objects.hashCode(this.ruCodigo);
        hash = 79 * hash + Objects.hashCode(this.ruHabilitado);
        hash = 79 * hash + Objects.hashCode(this.first);
        hash = 79 * hash + Objects.hashCode(this.maxResults);
        hash = 79 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 79 * hash + Arrays.hashCode(this.ascending);
        hash = 79 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroRubros other = (FiltroRubros) obj;
        if (!Objects.equals(this.ruId, other.ruId)) {
            return false;
        }
        return true;
    }

}

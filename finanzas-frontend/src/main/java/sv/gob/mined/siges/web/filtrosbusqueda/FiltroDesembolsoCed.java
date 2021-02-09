/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;

/**
 * Filtro para el requerimiento de fondos (detalle de los centros educativos)
 *
 * @author jgiron
 */
public class FiltroDesembolsoCed implements Serializable {

    private long dcePk;
    private Long dceDetDesembolsoFk;
    private EnumDesembolsoEstado dceEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroDesembolsoCed() {
    }

    public long getDcePk() {
        return dcePk;
    }

    public void setDcePk(long dcePk) {
        this.dcePk = dcePk;
    }

    public Long getDceDetDesembolsoFk() {
        return dceDetDesembolsoFk;
    }

    public void setDceDetDesembolsoFk(Long dceDetDesembolsoFk) {
        this.dceDetDesembolsoFk = dceDetDesembolsoFk;
    }

    public EnumDesembolsoEstado getDceEstado() {
        return dceEstado;
    }

    public void setDceEstado(EnumDesembolsoEstado dceEstado) {
        this.dceEstado = dceEstado;
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
        hash = 71 * hash + (int) (this.dcePk ^ (this.dcePk >>> 32));
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
        final FiltroDesembolsoCed other = (FiltroDesembolsoCed) obj;
        if (this.dcePk != other.dcePk) {
            return false;
        }
        return true;
    }

   
    
    
    
   

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Objects;

/**
 * Filtro de las solicitudes de transferencia (requerimientos de fondos)
 *
 * @author jgiron
 */
public class FiltroTransferenciaComponente implements Serializable {

    private Long tcId;
    private Long tcTransferenciaFk;

    private String[] orderBy;
    private boolean[] ascending;
    private Long first;
    private Long maxResults;
    private String[] incluirCampos;

    public FiltroTransferenciaComponente() {
    }

    public Long getTcId() {
        return tcId;
    }

    public void setTcId(Long tcId) {
        this.tcId = tcId;
    }

    public Long getTcTransferenciaFk() {
        return tcTransferenciaFk;
    }

    public void setTcTransferenciaFk(Long tcTransferenciaFk) {
        this.tcTransferenciaFk = tcTransferenciaFk;
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
        hash = 29 * hash + Objects.hashCode(this.tcId);
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
        final FiltroTransferenciaComponente other = (FiltroTransferenciaComponente) obj;
        if (!Objects.equals(this.tcId, other.tcId)) {
            return false;
        }
        return true;
    }

    
   

}

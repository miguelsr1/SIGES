/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;

/**
 * Filtro de los desembolsos
 *
 * @author jgiron
 */
public class FiltroDetalleDesembolso implements Serializable {

    private Long dedPk;
    private Long dedDesembolsoFk;
    private Long requerimientoPk;
    private Long componenteId;
    private Long subComponenteId;
    private String sacUfi;
    private String sacGoes;
    
    String[] orderBy;
    boolean[] ascending;
    private Long first;
    private Long maxResults;
    private String[] incluirCampos;

    public FiltroDetalleDesembolso() {
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public Long getDedDesembolsoFk() {
        return dedDesembolsoFk;
    }

    public void setDedDesembolsoFk(Long dedDesembolsoFk) {
        this.dedDesembolsoFk = dedDesembolsoFk;
    }

    public Long getRequerimientoPk() {
        return requerimientoPk;
    }

    public void setRequerimientoPk(Long requerimientoPk) {
        this.requerimientoPk = requerimientoPk;
    }

    public Long getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(Long componenteId) {
        this.componenteId = componenteId;
    }

    public Long getSubComponenteId() {
        return subComponenteId;
    }

    public void setSubComponenteId(Long subComponenteId) {
        this.subComponenteId = subComponenteId;
    }
        
    public String getSacUfi() {
        return sacUfi;
    }

    public void setSacUfi(String sacUfi) {
        this.sacUfi = sacUfi;
    }

    public String getSacGoes() {
        return sacGoes;
    }

    public void setSacGoes(String sacGoes) {
        this.sacGoes = sacGoes;
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
        hash = 71 * hash + Objects.hashCode(this.dedPk);
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
        final FiltroDetalleDesembolso other = (FiltroDetalleDesembolso) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }

    

}

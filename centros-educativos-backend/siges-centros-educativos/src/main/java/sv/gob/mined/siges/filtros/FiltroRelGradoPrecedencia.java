/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.util.List;

public class FiltroRelGradoPrecedencia {

    private Long rgpGradoDestino;
    private Long rgpGradoOrigen;
    private List<Long> rpgGradosOrigen;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroRelGradoPrecedencia() {
    }

    public Long getRgpGradoDestino() {
        return rgpGradoDestino;
    }

    public void setRgpGradoDestino(Long rgpGradoDestino) {
        this.rgpGradoDestino = rgpGradoDestino;
    }

    public List<Long> getRpgGradosOrigen() {
        return rpgGradosOrigen;
    }

    public void setRpgGradosOrigen(List<Long> rpgGradosOrigen) {
        this.rpgGradosOrigen = rpgGradosOrigen;
    }

    public Long getRgpGradoOrigen() {
        return rgpGradoOrigen;
    }

    public void setRgpGradoOrigen(Long rgpGradoOrigen) {
        this.rgpGradoOrigen = rgpGradoOrigen;
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
    
    
}

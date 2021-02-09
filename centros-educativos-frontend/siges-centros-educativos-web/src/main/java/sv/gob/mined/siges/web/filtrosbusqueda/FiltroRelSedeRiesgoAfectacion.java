package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroRelSedeRiesgoAfectacion implements Serializable {

    private Long rarPk;
    private Long rarTipoRiesgo;
    private Long rarGradoAfectacion;
   

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroRelSedeRiesgoAfectacion() {

    }

    public Long getRarPk() {
        return rarPk;
    }

    public void setRarPk(Long rarPk) {
        this.rarPk = rarPk;
    }

    public Long getRarTipoRiesgo() {
        return rarTipoRiesgo;
    }

    public void setRarTipoRiesgo(Long rarTipoRiesgo) {
        this.rarTipoRiesgo = rarTipoRiesgo;
    }

    public Long getRarGradoAfectacion() {
        return rarGradoAfectacion;
    }

    public void setRarGradoAfectacion(Long rarGradoAfectacion) {
        this.rarGradoAfectacion = rarGradoAfectacion;
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

}

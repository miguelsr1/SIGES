/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoExtraccion;

public class FiltroExtraccion implements Serializable {
        
    private Long datasetPk;
    private Long nombrePk;
    private Integer anio;
    private EnumEstadoExtraccion estado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroExtraccion() {
    }

    public Long getDatasetPk() {
        return datasetPk;
    }

    public void setDatasetPk(Long datasetPk) {
        this.datasetPk = datasetPk;
    }

    public Long getNombrePk() {
        return nombrePk;
    }

    public void setNombrePk(Long nombrePk) {
        this.nombrePk = nombrePk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public EnumEstadoExtraccion getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoExtraccion estado) {
        this.estado = estado;
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

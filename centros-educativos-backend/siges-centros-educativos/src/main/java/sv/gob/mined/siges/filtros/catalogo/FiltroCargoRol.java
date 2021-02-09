/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros.catalogo;

import java.io.Serializable;

public class FiltroCargoRol implements Serializable {
    

    private Long carCargo;

    private Boolean carHabilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCargoRol() {
    }

    public Long getCarCargo() {
        return carCargo;
    }

    public void setCarCargo(Long carCargo) {
        this.carCargo = carCargo;
    }

    public Boolean getCarHabilitado() {
        return carHabilitado;
    }

    public void setCarHabilitado(Boolean carHabilitado) {
        this.carHabilitado = carHabilitado;
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

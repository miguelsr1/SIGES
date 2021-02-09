/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;

public class FiltroAccion implements Serializable {
    
    
    private Integer accNum;
    private EnumEstadoSolicitudTraslado accEstadoOrigen;
    private EnumEstadoSolicitudTraslado accEstadoDestino;
    private String accNombreAccion;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroAccion() {
    }

    public Integer getAccNum() {
        return accNum;
    }

    public void setAccNum(Integer accNum) {
        this.accNum = accNum;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoOrigen() {
        return accEstadoOrigen;
    }

    public void setAccEstadoOrigen(EnumEstadoSolicitudTraslado accEstadoOrigen) {
        this.accEstadoOrigen = accEstadoOrigen;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoDestino() {
        return accEstadoDestino;
    }

    public void setAccEstadoDestino(EnumEstadoSolicitudTraslado accEstadoDestino) {
        this.accEstadoDestino = accEstadoDestino;
    }

    public String getAccNombreAccion() {
        return accNombreAccion;
    }

    public void setAccNombreAccion(String accNombreAccion) {
        this.accNombreAccion = accNombreAccion;
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

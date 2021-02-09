/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.catalogo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.accNum);
        hash = 97 * hash + Objects.hashCode(this.accEstadoOrigen);
        hash = 97 * hash + Objects.hashCode(this.accEstadoDestino);
        hash = 97 * hash + Objects.hashCode(this.accNombreAccion);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroAccion other = (FiltroAccion) obj;
        if (!Objects.equals(this.accNombreAccion, other.accNombreAccion)) {
            return false;
        }
        if (!Objects.equals(this.accNum, other.accNum)) {
            return false;
        }
        if (this.accEstadoOrigen != other.accEstadoOrigen) {
            return false;
        }
        if (this.accEstadoDestino != other.accEstadoDestino) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }
    
    

}

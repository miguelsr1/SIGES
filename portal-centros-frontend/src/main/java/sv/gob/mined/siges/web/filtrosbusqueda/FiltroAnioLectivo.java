package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroAnioLectivo implements Serializable {

    private Long alePk;
    private EnumAnioLectivoEstado aleEstado;
    private Long aleGradoFk;
    private Long aleSedeFk;
    private Integer aleAnio;
    private EnumSeccionEstado aleSeccionEstado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroAnioLectivo() {

    }

    public Long getAlePk() {
        return alePk;
    }

    public void setAlePk(Long alePk) {
        this.alePk = alePk;
    }

    public EnumAnioLectivoEstado getAleEstado() {
        return aleEstado;
    }

    public void setAleEstado(EnumAnioLectivoEstado aleEstado) {
        this.aleEstado = aleEstado;
    }

    public Long getAleGradoFk() {
        return aleGradoFk;
    }

    public void setAleGradoFk(Long aleGradoFk) {
        this.aleGradoFk = aleGradoFk;
    }

    public Long getAleSedeFk() {
        return aleSedeFk;
    }

    public void setAleSedeFk(Long aleSedeFk) {
        this.aleSedeFk = aleSedeFk;
    }

    public Integer getAleAnio() {
        return aleAnio;
    }

    public void setAleAnio(Integer aleAnio) {
        this.aleAnio = aleAnio;
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

    public EnumSeccionEstado getAleSeccionEstado() {
        return aleSeccionEstado;
    }

    public void setAleSeccionEstado(EnumSeccionEstado aleSeccionEstado) {
        this.aleSeccionEstado = aleSeccionEstado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.alePk);
        hash = 89 * hash + Objects.hashCode(this.aleEstado);
        hash = 89 * hash + Objects.hashCode(this.aleGradoFk);
        hash = 89 * hash + Objects.hashCode(this.aleSedeFk);
        hash = 89 * hash + Objects.hashCode(this.aleAnio);
        hash = 89 * hash + Objects.hashCode(this.aleSeccionEstado);
        hash = 89 * hash + Objects.hashCode(this.first);
        hash = 89 * hash + Objects.hashCode(this.maxResults);
        hash = 89 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 89 * hash + Arrays.hashCode(this.ascending);
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
        final FiltroAnioLectivo other = (FiltroAnioLectivo) obj;
        if (!Objects.equals(this.alePk, other.alePk)) {
            return false;
        }
        if (this.aleEstado != other.aleEstado) {
            return false;
        }
        if (!Objects.equals(this.aleGradoFk, other.aleGradoFk)) {
            return false;
        }
        if (!Objects.equals(this.aleSedeFk, other.aleSedeFk)) {
            return false;
        }
        if (!Objects.equals(this.aleAnio, other.aleAnio)) {
            return false;
        }
        if (this.aleSeccionEstado != other.aleSeccionEstado) {
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
        return true;
    }
    
    

}

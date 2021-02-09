/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;

public class FiltroEscalaCalificacion implements Serializable {

    private String ecaCodigo;
    private String ecaCodigoExacto;

    private Boolean ecaHabilitado;

    private String ecaNombre;
    private String ecaNombreExacto;

    private TipoEscalaCalificacion ecaTipoEscala;

    private String ecaValorMinimo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroEscalaCalificacion() {
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public String getEcaCodigoExacto() {
        return ecaCodigoExacto;
    }

    public void setEcaCodigoExacto(String ecaCodigoExacto) {
        this.ecaCodigoExacto = ecaCodigoExacto;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreExacto() {
        return ecaNombreExacto;
    }

    public void setEcaNombreExacto(String ecaNombreExacto) {
        this.ecaNombreExacto = ecaNombreExacto;
    }

    public TipoEscalaCalificacion getEcaTipoEscala() {
        return ecaTipoEscala;
    }

    public void setEcaTipoEscala(TipoEscalaCalificacion ecaTipoEscala) {
        this.ecaTipoEscala = ecaTipoEscala;
    }

    public String getEcaValorMinimo() {
        return ecaValorMinimo;
    }

    public void setEcaValorMinimo(String ecaValorMinimo) {
        this.ecaValorMinimo = ecaValorMinimo;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.ecaCodigo);
        hash = 89 * hash + Objects.hashCode(this.ecaCodigoExacto);
        hash = 89 * hash + Objects.hashCode(this.ecaHabilitado);
        hash = 89 * hash + Objects.hashCode(this.ecaNombre);
        hash = 89 * hash + Objects.hashCode(this.ecaNombreExacto);
        hash = 89 * hash + Objects.hashCode(this.ecaTipoEscala);
        hash = 89 * hash + Objects.hashCode(this.ecaValorMinimo);
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
        final FiltroEscalaCalificacion other = (FiltroEscalaCalificacion) obj;
        if (!Objects.equals(this.ecaCodigo, other.ecaCodigo)) {
            return false;
        }
        if (!Objects.equals(this.ecaCodigoExacto, other.ecaCodigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.ecaNombre, other.ecaNombre)) {
            return false;
        }
        if (!Objects.equals(this.ecaNombreExacto, other.ecaNombreExacto)) {
            return false;
        }
        if (!Objects.equals(this.ecaValorMinimo, other.ecaValorMinimo)) {
            return false;
        }
        if (!Objects.equals(this.ecaHabilitado, other.ecaHabilitado)) {
            return false;
        }
        if (this.ecaTipoEscala != other.ecaTipoEscala) {
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

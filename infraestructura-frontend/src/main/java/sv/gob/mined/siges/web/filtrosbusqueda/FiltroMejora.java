/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;


public class FiltroMejora implements Serializable {
    
    private Long edificio;
    private Long tipoMejora;
    private LocalDate fecha;
    private Long fuenteFinanciamiento;
    private Long inmueble;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroMejora() {
    }

    public Long getEdificio() {
        return edificio;
    }

    public void setEdificio(Long edificio) {
        this.edificio = edificio;
    }

    public Long getTipoMejora() {
        return tipoMejora;
    }

    public void setTipoMejora(Long tipoMejora) {
        this.tipoMejora = tipoMejora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(Long fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public Long getInmueble() {
        return inmueble;
    }

    public void setInmueble(Long inmueble) {
        this.inmueble = inmueble;
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
        hash = 67 * hash + Objects.hashCode(this.edificio);
        hash = 67 * hash + Objects.hashCode(this.tipoMejora);
        hash = 67 * hash + Objects.hashCode(this.fecha);
        hash = 67 * hash + Objects.hashCode(this.fuenteFinanciamiento);
        hash = 67 * hash + Objects.hashCode(this.inmueble);
        hash = 67 * hash + Objects.hashCode(this.first);
        hash = 67 * hash + Objects.hashCode(this.maxResults);
        hash = 67 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 67 * hash + Arrays.hashCode(this.ascending);
        hash = 67 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroMejora other = (FiltroMejora) obj;
        if (!Objects.equals(this.edificio, other.edificio)) {
            return false;
        }
        if (!Objects.equals(this.tipoMejora, other.tipoMejora)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        if (!Objects.equals(this.fuenteFinanciamiento, other.fuenteFinanciamiento)) {
            return false;
        }
        if (!Objects.equals(this.inmueble, other.inmueble)) {
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

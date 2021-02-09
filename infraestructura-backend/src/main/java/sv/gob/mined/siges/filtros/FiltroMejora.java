/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;


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


}

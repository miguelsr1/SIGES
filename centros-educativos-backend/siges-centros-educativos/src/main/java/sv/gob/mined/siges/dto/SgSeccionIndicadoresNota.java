package sv.gob.mined.siges.dto;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author fabricio
 */
public class SgSeccionIndicadoresNota {
    
    private Long seccionId;
    private String codigoCentro;
    private Integer anio;
    private String codigoPeriodo;
    private Set<SgIndicadorNota> indicadores;


    public SgSeccionIndicadoresNota(Long seccionId, String codigoCentro, Integer anio, String codigoPeriodo) {
        this.seccionId = seccionId;
        this.codigoCentro = codigoCentro;
        this.anio = anio;
        this.codigoPeriodo = codigoPeriodo;
        this.indicadores = new HashSet<>();
    }
    
    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }

    public String getCodigoCentro() {
        return codigoCentro;
    }

    public void setCodigoCentro(String codigoCentro) {
        this.codigoCentro = codigoCentro;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getCodigoPeriodo() {
        return codigoPeriodo;
    }

    public void setCodigoPeriodo(String codigoPeriodo) {
        this.codigoPeriodo = codigoPeriodo;
    }

    public Set<SgIndicadorNota> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<SgIndicadorNota> indicadores) {
        this.indicadores = indicadores;
    }

    

    
      
}

package sv.gob.mined.siges.dto;

import java.io.Serializable;

public class SgPrediccionDesercionResponse implements Serializable {

    private Double porcentaje;
    private Integer resultado;
    private String tipo;

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultado(Integer resultado) {
        this.resultado = resultado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}

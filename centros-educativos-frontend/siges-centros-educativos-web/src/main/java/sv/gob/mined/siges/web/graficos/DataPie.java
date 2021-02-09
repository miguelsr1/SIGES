/*
 * Nombre del clinete
 * Sistema de Gestión
 * Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.graficos;

import java.io.Serializable;

/**
 *
 * @author Usuario
 */
public class DataPie implements Serializable {
    
    private Object rotulo;
    private Float valor;
    private Float porcentaje;
    private Long id;

    public Object getRotulo() {
        return rotulo;
    }

    public void setRotulo(Object rotulo) {
        this.rotulo = rotulo;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

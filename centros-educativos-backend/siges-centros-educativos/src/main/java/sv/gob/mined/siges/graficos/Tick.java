/*
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.graficos;

import java.io.Serializable;

public class Tick implements Serializable {
    
    private Double valor;
    private String label; 

    public Tick() {
    }
    

    public Tick(Double valor, String label) {
        this.valor = valor;
        this.label = label;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    


}

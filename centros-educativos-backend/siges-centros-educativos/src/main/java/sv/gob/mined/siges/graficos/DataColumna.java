/*
 *  Nombre del clinete
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.graficos;

import java.io.Serializable;

/**
 *
 * @author Usuario
 */
public class DataColumna implements Serializable {
    
    private Object rotulo;
    private Double[] valor; 

    public DataColumna(Object rotulo, Double[] valor) {
        this.rotulo = rotulo;
        this.valor = valor;
    }

    public DataColumna() {
    }
    
    

    public Object getRotulo() {
        return rotulo;
    }

    public void setRotulo(Object rotulo) {
        this.rotulo = rotulo;
    }

    public Double[] getValor() {
        return valor;
    }

    public void setValor(Double[] valor) {
        this.valor = valor;
    }


}

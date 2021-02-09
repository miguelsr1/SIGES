/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

public class EstGenerica implements Serializable {
    
    private Object dato;
    private String desagregacion;
    private Object cantidad;
    

    public EstGenerica() {
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public String getDesagregacion() {
        return desagregacion;
    }

    public void setDesagregacion(String desagregacion) {
        this.desagregacion = desagregacion;
    }

    public Object getCantidad() {
        return cantidad;
    }

    public void setCantidad(Object cantidad) {
        this.cantidad = cantidad;
    }

     
}

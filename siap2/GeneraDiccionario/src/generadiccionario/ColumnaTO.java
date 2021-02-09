/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generadiccionario;

/**
 *
 * @author sofis
 */
public class ColumnaTO {
    
    private String nombre;
    private String tipo;
    private String longitud;
    private String valorPorDefecto;
    private String permiteNulos;
    private String llavePrimaria;
    private String descripcion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getValorPorDefecto() {
        return valorPorDefecto;
    }

    public void setValorPorDefecto(String valorPorDefecto) {
        this.valorPorDefecto = valorPorDefecto;
    }

    public String getPermiteNulos() {
        return permiteNulos;
    }

    public void setPermiteNulos(String permiteNulos) {
        this.permiteNulos = permiteNulos;
    }

    public String getLlavePrimaria() {
        return llavePrimaria;
    }

    public void setLlavePrimaria(String llavePrimaria) {
        this.llavePrimaria = llavePrimaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
            
    
}

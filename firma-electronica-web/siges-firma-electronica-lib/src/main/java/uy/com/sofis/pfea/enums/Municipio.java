package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum Municipio {
    A("Municipio A"),
    B("Municipio B"),
    C("Municipio C"),
    CH("Municipio CH"),
    D("Municipio D"),
    E("Municipio E"),
    F("Municipio F"),
    G("Municipio G"),
    OTRO("Otro/Sin especificar");

    private final String nombre;
    
    Municipio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}

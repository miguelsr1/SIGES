package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum Sexo {
    F("Femenino"),
    M("Masculino"),
    O("Otro/No indicado");

    private final String nombre;

    Sexo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}

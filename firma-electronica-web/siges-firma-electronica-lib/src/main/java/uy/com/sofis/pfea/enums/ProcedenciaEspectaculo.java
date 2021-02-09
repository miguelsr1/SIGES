package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum ProcedenciaEspectaculo {
    UY("Uruguay"),
    LA("Latinoamérica"),
    NA("Norteamérica"),
    EU("Europa"),
    AS("Asia"),
    RM("Resto del mundo");

    private final String nombre;

    ProcedenciaEspectaculo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum PeriodoBeneficio {
    SEMANAL("Semanal"),
    MENSUAL("Mensual"),
    ANUAL("Anual"),
    VIDA("Vida"),
    ILIMITADO("Ilimitado");

    private final String nombre;

    PeriodoBeneficio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

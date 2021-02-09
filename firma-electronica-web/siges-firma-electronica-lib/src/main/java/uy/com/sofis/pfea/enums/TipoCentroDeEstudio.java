package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum TipoCentroDeEstudio {
    IFD("IFD");

    private final String nombre;

    TipoCentroDeEstudio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

}

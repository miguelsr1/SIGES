package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum TipoEspectaculo {
    CARNAVAL("Carnaval"),
    CINEARTE("Cine arte"),
    CINECOMERCIAL("Cine comercial"),
    DANZA("Danza"),
    EVENTO("Evento"),
    MUSEO("Museo"),
    MUSICA("Música"),
    OPERA("Ópera"),
    PARQUE("Parque de atracciones"),
    TEATRO("Teatro");

    private final String nombre;

    TipoEspectaculo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}

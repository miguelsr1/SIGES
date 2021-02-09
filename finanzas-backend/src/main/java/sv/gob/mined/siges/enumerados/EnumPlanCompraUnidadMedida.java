/*
 * TSIGES
* Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Unidades de medida
 *
 * @author sofis-iquezada
 */
public enum EnumPlanCompraUnidadMedida {
    METRO("Metro (m)"),
    KILOGRAMO("Kilogramo (kg)"),
    UNIDAD("Unidadad (u)"),
    PAQUETES("Paquetes");

    private final String text;

    private EnumPlanCompraUnidadMedida(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

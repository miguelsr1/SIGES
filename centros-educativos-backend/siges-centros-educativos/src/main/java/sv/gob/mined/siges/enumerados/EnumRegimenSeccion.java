/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumRegimenSeccion {
    LaV("Lunes a Viernes"),
    SyD("Sábado y Domingo");

    private final String text;

    private EnumRegimenSeccion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

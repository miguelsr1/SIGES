/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoPlaza {
    OCUPADA("Ocupada"),
    VACANTE("Vacante");

    private final String text;

    private EnumEstadoPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
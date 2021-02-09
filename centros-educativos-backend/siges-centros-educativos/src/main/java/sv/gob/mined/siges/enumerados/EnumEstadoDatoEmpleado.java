/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoDatoEmpleado {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String text;

    private EnumEstadoDatoEmpleado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
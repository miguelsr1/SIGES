/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoPEA {
    ENVIADO("Enviado"),
    REVISADO("Revisado"),
    VALIDADO("Validado");

    private final String text;

    private EnumEstadoPEA(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
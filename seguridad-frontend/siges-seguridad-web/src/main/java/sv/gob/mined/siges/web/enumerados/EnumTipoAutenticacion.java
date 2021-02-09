/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoAutenticacion {
    AUTHENTICATION_SUCCESS("Login satisfactorio"),
    AUTHENTICATION_FAILED("Login incorrecto"),
    TICKET_GRANTING_TICKET_DESTROYED("Cierre de sesión");

    private final String text;

    private EnumTipoAutenticacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

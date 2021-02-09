/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de los organismos de administración escolar.
 *
 * @author jgiron
 */
public enum EnumEstadoOrganismoAdministrativo {
    ELABORACION("Elaboración"),
    ENVIADO("Enviado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado"),
    CERRADO("Cerrado"),
    AMPLIAR_DATOS("Ampliar datos");

    private final String text;

    private EnumEstadoOrganismoAdministrativo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

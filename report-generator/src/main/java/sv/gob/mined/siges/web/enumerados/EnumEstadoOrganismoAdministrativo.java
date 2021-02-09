/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoOrganismoAdministrativo {
    ELABORACION("Elaboración"),
    ENVIADO("Enviado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");
    private final String text;

    private EnumEstadoOrganismoAdministrativo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

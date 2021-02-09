/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siap2.entities.enums;

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

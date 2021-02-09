/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado del presupuesto escolar
 *
 * @author jgiron
 */
public enum EnumPlanCompraEstado {
    EN_PROCESO("En proceso"),
    CERRADO("Cerrado"),
    EN_REVISION("En revisión"),
    OBSERVADO("Observado"),
    APROBADO("Aprobado"),
    CORREGIDO("Corregido");

    private final String text;

    private EnumPlanCompraEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

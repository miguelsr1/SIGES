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
public enum EnumPresupuestoEscolarEstado {
    EN_PROCESO("En proceso"),
    FORMULADO("Formulado"),
    EN_AJUSTE("En ajuste"),
    AJUSTADO("Ajustado"),
    OBSERVADO("Observado"),
    APROBADO("Aprobado"),
    EDITADO("Editado"),
    CERRADO("Cerrado");

    private final String text;

    private EnumPresupuestoEscolarEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

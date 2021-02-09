/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumFacturaEstado {
    EN_PROCESO("En proceso"),
    FORMULADO("Formulado"),
    EN_AJUSTE("En ajuste"),
    APROBADO("Aprobado"),
    ANULACION("Anulado"),
    PAGADO("Pagado");

    private final String text;

    private EnumFacturaEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTiposPeriodosCalificaciones {
    ORD("Ordinario"),
    EXORD("Extraordinario"),
    NOTIN("Nota Institucional"),
    APR("Aprobacion"),
    GRA("Promoción Grado"),
    PAES("PAES");

    private final String text;

    private EnumTiposPeriodosCalificaciones(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

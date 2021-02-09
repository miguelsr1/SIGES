/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTiposCalificacionDiplomado {
    ORD("Ordinario"),
    NOTIN("Nota Institucional");

    private final String text;

    private EnumTiposCalificacionDiplomado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

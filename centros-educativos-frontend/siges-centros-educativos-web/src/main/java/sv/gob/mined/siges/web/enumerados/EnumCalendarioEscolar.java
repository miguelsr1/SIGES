/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumCalendarioEscolar {
    MAT("Matrícula"),
    PREC("Primera Recuperación"),
    PRECPS("Primera Recuperación PS"),
    SREC("Segunda Recuperación"),
    SRECPS("Segunda Recuperación PS");

    private final String text;

    private EnumCalendarioEscolar(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

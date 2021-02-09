/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;


public enum EnumCalendarioEscolar {
    MAT("Matricula"),
    PREC("Primera Recuperación"),
    PRECPS("Primera Recuperación PS"),
    SREC("Segunda Recupearción"),
    SRECPS("Segunda Recuperación PS");

    private final String text;

    private EnumCalendarioEscolar(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

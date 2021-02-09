/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumCalculoNotaInstitucional {
    PROM("Promedio"),
    MED("Mediana"),
    ULT("Último"),
    MAY("Mayor");

    private final String text;

    private EnumCalculoNotaInstitucional(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

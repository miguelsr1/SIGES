/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumCategoriaAlertaTrabajo {
    TRABAJO_INFANTIL("Trabajo infantil");
    
    private final String text;

    private EnumCategoriaAlertaTrabajo(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}

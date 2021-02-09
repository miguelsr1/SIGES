/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumTipoAula {
    PERMANENTE("Permanente"),
    PROVISIONAL("Provisional"),
    SEMIPERMANENTE("Semipermanente");
    
    private final String text;

    private EnumTipoAula(final String text) {
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

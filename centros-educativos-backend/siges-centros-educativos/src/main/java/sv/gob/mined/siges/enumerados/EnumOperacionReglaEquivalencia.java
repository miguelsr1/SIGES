/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumOperacionReglaEquivalencia {
    ORIGEN("Origen"),
    DESTINO("Destino");
    
    private final String text;

    private EnumOperacionReglaEquivalencia(final String text) {
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



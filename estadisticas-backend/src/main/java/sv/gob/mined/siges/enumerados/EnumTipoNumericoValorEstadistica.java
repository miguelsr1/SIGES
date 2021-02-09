/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumTipoNumericoValorEstadistica {
    DECIMAL("Decimal"),
    ENTERO("Entero");
    
    private final String text;

    private EnumTipoNumericoValorEstadistica(final String text) {
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

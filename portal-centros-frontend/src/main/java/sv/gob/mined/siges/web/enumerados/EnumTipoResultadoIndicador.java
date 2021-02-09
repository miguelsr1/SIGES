/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumTipoResultadoIndicador {
    CANTIDAD("Cantidad"),
    PORCENTAJE("Porcentaje");
    
    private final String text;

    private EnumTipoResultadoIndicador(final String text) {
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

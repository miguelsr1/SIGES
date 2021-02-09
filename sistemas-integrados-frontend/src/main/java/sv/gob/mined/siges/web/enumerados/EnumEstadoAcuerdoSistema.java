/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumEstadoAcuerdoSistema {
    VIGENTE("Vigente"),
    FINALIZADO("Finalizado"),
    NO_VIGENTE("No vigente");
    
    private final String text;

    private EnumEstadoAcuerdoSistema(final String text) {
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

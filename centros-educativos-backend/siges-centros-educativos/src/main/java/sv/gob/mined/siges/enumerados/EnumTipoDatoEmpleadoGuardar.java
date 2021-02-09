/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoDatoEmpleadoGuardar {
    ESCALAFON("Escalafón"),
    DATOS_GENERALES("Datos generales");

    private final String text;

    private EnumTipoDatoEmpleadoGuardar(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
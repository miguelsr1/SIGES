
package sv.gob.mined.siges.web.enumerados;

import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;


public enum EnumTipoPruebaPaes {
    
    PAES("PAES", ConstantesOperaciones.HABILITADO_CALIFICACIONES_PAES),
    EXTERNA("Externa", ConstantesOperaciones.HABILITADO_CALIFICACIONES_EXTERNAS);

    private final String text;
    private final String operacion;

    private EnumTipoPruebaPaes(final String text, final String operacion) {
        this.text = text;
        this.operacion = operacion;
    }

    public String getText() {
        return text;
    }

    public String getOperacion() {
        return operacion;
    }
    
}

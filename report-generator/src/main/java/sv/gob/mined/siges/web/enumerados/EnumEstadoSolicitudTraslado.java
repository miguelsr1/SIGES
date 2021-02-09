/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

import java.util.HashMap;
import java.util.Map;

public enum EnumEstadoSolicitudTraslado {
    PENDIENTE("Pendiente"),
    PENDIENTE_RESOLUCION("Pendiente resolución"),
    AUTORIZADA("Autorizada"),
    RECHAZADA("Rechazada"),
    CONFIRMADO("Confirmado"),
    ANULADO("Anulado");

    private final String text;
    private static final Map<String, EnumEstadoSolicitudTraslado> MY_MAP = new HashMap<String, EnumEstadoSolicitudTraslado>();
    static {
      for (EnumEstadoSolicitudTraslado myEnum : values()) {
        MY_MAP.put(myEnum.getText(), myEnum);
      }
    }

    private EnumEstadoSolicitudTraslado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public static EnumEstadoSolicitudTraslado getByValue(String value){
        return MY_MAP.get(value);
    }
    
}

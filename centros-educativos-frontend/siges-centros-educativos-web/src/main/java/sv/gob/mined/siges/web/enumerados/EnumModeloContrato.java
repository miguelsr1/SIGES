/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

import java.util.HashMap;
import java.util.Map;


public enum EnumModeloContrato {
    ACUERDO("Acuerdo de Nombramiento"),
    CONTRATO("Contrato"),
    OTRO("Otros vínculos");
    
    private final String text;
    private static final Map<String, EnumModeloContrato> MY_MAP = new HashMap<String, EnumModeloContrato>();
    static {
      for (EnumModeloContrato myEnum : values()) {
        MY_MAP.put(myEnum.getText(), myEnum);
      }
    }

    private EnumModeloContrato(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public static EnumModeloContrato getByValue(String value){
        return MY_MAP.get(value);
    }
}


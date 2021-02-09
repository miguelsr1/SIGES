/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

import java.util.HashMap;
import java.util.Map;

public enum EnumAmbito {
    MINED("General", 1),
    DEPARTAMENTAL("Departamental", 2),
    SEDE("Sede", 3),
    SECCION("Sección", 4),
    PERSONA("Persona", 5);

    private final String text;
    private final int orden;

    private static final Map<String, EnumAmbito> MY_MAP = new HashMap<String, EnumAmbito>();

    static {
        for (EnumAmbito myEnum : values()) {
            MY_MAP.put(myEnum.getText(), myEnum);
        }
    }

    private EnumAmbito(final String text, final int orden) {
        this.text = text;
        this.orden = orden;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public int getOrden() {
        return orden;
    }

    public static EnumAmbito getByValue(String value) {
        return MY_MAP.get(value);
    }
}

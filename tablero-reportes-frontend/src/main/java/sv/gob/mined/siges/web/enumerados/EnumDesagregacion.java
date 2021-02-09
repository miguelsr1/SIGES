/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sofis Solutions
 */
public enum EnumDesagregacion {

    DEPARTAMENTO("Departamento"),
    ZONA("Zona"),
    SECTOR("Sector"),
    SEXO("Sexo"),
    NIVEL("Nivel"),
    GRADO("Grado");

    private final String text;

    private static final Map<String, EnumDesagregacion> MY_MAP = new HashMap<String, EnumDesagregacion>();

    static {
        for (EnumDesagregacion myEnum : values()) {
            MY_MAP.put(myEnum.getText(), myEnum);
        }
    }

    private EnumDesagregacion(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
    
    public static EnumDesagregacion getByValue(String value) {
        return MY_MAP.get(value);
    }

}

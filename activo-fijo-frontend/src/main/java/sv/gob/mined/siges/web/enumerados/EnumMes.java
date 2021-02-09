/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

import java.util.HashMap;
import java.util.Map;
import static sv.gob.mined.siges.web.enumerados.EnumMes.values;

public enum EnumMes {
    ENERO(1, "Enero"),
    FEBRERO(2,"Febrero"),
    MARZO(3,"Marzo"),
    ABRIL(4,"Abril"),
    MAYO(5,"Mayo"),
    JUNIO(6,"Junio"),
    JULIO(7,"Julio"),
    AGOSTO(8,"Agosto"),
    SEPTIEMBRE(9,"Septiembre"),
    OCTUBRE(10,"Octubre"),
    NOVIEMBRE(11,"Noviembre"),
    DICIEMBRE(12,"Diciembre"),;

    private final String text;
    private final Integer numero;

    private static final Map<Integer, EnumMes> MY_MAP = new HashMap<Integer, EnumMes>();

    static {
        for (EnumMes myEnum : values()) {
            MY_MAP.put(myEnum.getNumero(), myEnum);
        }
    }

    private EnumMes(final Integer numero, final String text) {
        this.text = text;
        this.numero = numero;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public Integer getNumero() {
        return numero;
    }

    public static EnumMes getByValue(Integer value) {
        return MY_MAP.get(value);
    }
}

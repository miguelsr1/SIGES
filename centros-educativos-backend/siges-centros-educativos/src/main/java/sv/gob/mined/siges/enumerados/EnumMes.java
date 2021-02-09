/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

import java.util.HashMap;
import java.util.Map;

public enum EnumMes {
    ENERO("Enero", "Ene", 1),
    FEBRERO("Febrero", "Feb", 2),
    MARZO("Marzo", "Mar", 3),
    ABRIL("Abril", "Abr", 4),
    MAYO("Mayo", "May", 5),
    JUNIO("Junio", "Jun", 6),
    JULIO("Julio", "Jul", 7),
    AGOSTO("Agosto", "Ago", 8),
    SEPTIEMBRE("Septiembre", "Sep", 9),
    OCTUBRE("Octubre", "Oct", 10),
    NOVIEMBRE("Noviembre", "Nov", 11),
    DICIEMBRE("Diciembre", "Dic", 12);

    private final String text;
    private final String shortText;
    private final int numero;

    private static final Map<String, EnumMes> MY_MAP = new HashMap<String, EnumMes>();
    private static final Map<Integer, EnumMes> MY_MAP_NUM = new HashMap<Integer, EnumMes>();

    static {
        for (EnumMes myEnum : values()) {
            MY_MAP.put(myEnum.getText(), myEnum);
            MY_MAP_NUM.put(myEnum.numero, myEnum);
        }
    }

    
    private EnumMes(final String text, final String shortText, final int numero) {
        this.text = text;
        this.shortText = shortText;
        this.numero = numero;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public int getNumero() {
        return numero;
    }

    public String getShortText() {
        return shortText;
    }
      
    public static EnumMes getByValue(String value) {
        return MY_MAP.get(value);
    }
    
    public static EnumMes getByNum(Integer value) {
        return MY_MAP_NUM.get(value);
    }
}

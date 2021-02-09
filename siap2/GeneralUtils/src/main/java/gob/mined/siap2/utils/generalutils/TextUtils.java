/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase provee métodos para trabajar sobre textos.
 *
 * @author Sofis Solutions
 */
public class TextUtils {

    /**
     * Determina si una secuencia de caracteres es vacía (nula o largo 0).
     *
     * @param str el string a examinar
     * @return true if str es nulo o de largo 0. (no considera el caso solo
     * espacios en blanco). en ese caso es false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * Este método reemplaza varios strings por otros
     *
     * @param text: texto a ser reemplazado
     * @param reemplazos, pares de reemplazo, reemplazante.
     * @return text con los valores reemplazados.
     */
    public static String reemplazarMultiplesStrings(String text, Map<String, String> reemplazos) {
        String respuesta = text;
        for (String s : reemplazos.keySet()) {
            respuesta = StringUtils.replace(respuesta, s, reemplazos.get(s));
        }

        return respuesta;
    }

    /**
     *
     * @param text
     * @param replacements
     * @return
     */
    public static String replaceTokens(String text, Map<String, String> replacements) {
        Pattern pattern = Pattern.compile("\\[(\\S+)\\]");
        Matcher matcher = pattern.matcher(text);

        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(text.substring(i, matcher.start()));

            if (replacement != null) {
                builder.append(replacement);
            }
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }

    /**
     * Este método determina si una cadena de caracteres es un número entero.
     *
     * @param s: cadena de caracteres.
     * @return true si s es un número entero.
     */
    public static boolean isInteger(String s) {
        try {
            Integer num = Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Agrega puntos suspensivos a una cadena de caracteres después de cierto
     * largo.
     *
     * @param input cadena de caracteres a acortar.
     * @param maxCharacters Cantidad de caracteres mínima. Debe ser mayor a 3
     *
     * @return input trucado seguido de ...
     */
    public static String ellipsize(String input, int maxCharacters) {

        if (input == null || input.length() <= maxCharacters) {
            return input;
        }
        return input.substring(0, maxCharacters - 3) + "...";
    }

    /**
     *
     * @param delimiter
     * @param tokens
     * @return
     */
    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static Map<String, String> ordenarMapDeStringsPorClave(Map<String, String> unsortMap) {
        List<Map.Entry<String, String>> list = new LinkedList(unsortMap.entrySet());
        Collections.sort(list, (Map.Entry<String, String> o1, Map.Entry<String, String> o2) -> (o1.getKey().toUpperCase()).compareTo(o2.getKey().toUpperCase()));

        // Loop the sorted list and put it into a new insertion order Map
        Map<String, String> sortedMap = new LinkedHashMap();
        list.forEach(entry -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });

        return sortedMap;
    }

    /**
     * Ordena un Map<String,String> por el value
     *
     * @param passedMap
     * @return
     */
    public static LinkedHashMap<String, String> ordenarMapDeStringByValue(HashMap<String, String> passedMap) {
        ArrayList<String> mapKeys = new ArrayList<>(passedMap.keySet());
        ArrayList<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, String> sortedMap
                = new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    /**
     * Si el largo del texto pasado por parámetro supera el entero pasado en el parámetro largoCorte, 
     * dicho texto se deja del tamaño del entero y se le agrega al final ...
     * @param texto
     * @param largoCorte
     * @return 
     */
    public static String limitarLargoTexto(String texto, int largoCorte) {
        String nuevotexto = "";
        if(texto!=null){
            if(texto.length() > largoCorte){
                nuevotexto = texto.substring(0, largoCorte) + "...";
            } else {
               nuevotexto = texto; 
            }
        }
        return nuevotexto;
    }

}

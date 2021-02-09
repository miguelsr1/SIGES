/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SofisStringUtils {

    private static String digitos = "0123456789";
    private static String letrasMayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Elimina los espacios en blanco al inicio, al final. Si hay más de un
     * espacio en blanco consecutivo en el texto, reemplaza esa subcadena por un
     * solo espacio en blanco. Por ejemplo: " hola que tal " devuelve "hola que
     * tal"
     *
     * @param s
     * @return
     */
    public static String normalizarString(String s) {
        if (s != null) {
            return s.replaceAll("[ ]+", " ").trim();
        }
        return null;
    }

    /**
     * Elimina los espacios en blanco al inicio, al final. Si hay más de un
     * espacio en blanco consecutivo en el texto, reemplaza esa subcadena por un
     * solo espacio en blanco. Finalmente, pasa todo el texto a mayúsculas. Por
     * ejemplo: " hola que tal " devuelve "HOLA QUE TAL"
     *
     * @param s
     * @return
     */
    public static String normalizarStringMayuscula(String s) {
        if (s != null) {
            return normalizarString(s).toUpperCase();
        }
        return null;
    }

    public static String convertNonAscii(String s) {
        if (s != null) {
            return Normalizer.normalize(s, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }
        return null;
    }

    public static String decode(String toDecodeString) {
        String decodedString = null;

        try {
            decodedString = URLDecoder.decode(toDecodeString, "ISO-8859-1");
        } catch (UnsupportedEncodingException uee) {
            Logger.getLogger(SofisStringUtils.class.getName()).log(Level.SEVERE, null, uee);
        }

        return decodedString;
    }

    public static String removeComas(String s) {
        if (s == null) {
            return "";
        }

        s = s.replaceAll(",", " ");
        return s;
    }

    public static String replaceTabEndLine(String s) {

        s = s.replaceAll("\n", " ");
        s = s.replaceAll("\t", " ");
        s = s.replaceAll("\b", " ");
        s = s.replaceAll("\r", " ");
        s = s.trim();
        return s;
    }


    public static String normalizarParaBusqueda(String s) {
        if (s != null){
            return SofisStringUtils.convertNonAscii(SofisStringUtils.normalizarString(s)).toLowerCase();
        }
        return s;
    }

    public static String encodeSHA256Hex(String text) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }
    
    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
}
    
    public static int cantidadDigitos(String c) {
        return contarEn(c,digitos);
    }
    
    public static int cantidadLetrasMayusculas(String c) {
        return contarEn(c,letrasMayusculas);
    }
    
    public static int contarEn(String c, String cadena) {
        int respuesta = 0;
        if (c!=null) {
            for(int i=0;i<c.length();i++) {
                if (cadena.contains(c.charAt(i)+"")) {
                    respuesta ++;
                }
            }
        }
        return respuesta;
    }
}

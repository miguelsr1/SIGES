/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

/**
 * Esta clase provee métodos sobre la clase String.
 * @author Sofis Solutions
 */
public class StringsUtils {
    
    /**
     * Determina si una cadena de caracteres es vacía.
     * Considera el caso nulo y solo espacios en blanco. En ese caso, devuelve true.
     * @param s: cadena a procesar.
     * @return true si la cadena es nula o solo tiene espacios en blanco.
     */
    public static boolean isEmpty(String s) {
        return (s==null) || (s.trim().length()==0);
    }
    
    
    /**
     * Determina si dos strings son iguales, considerando los casos nulos.
     * @param s1: primer string a comparar
     * @param s2: segundo string a comparar.
     * @return true si ambos son nulos o ambos son iguales.
     */
    public static boolean sonStringIguales(String s1, String s2) {
        if (s1!=null) {
            if (s2!=null) {
                return s1.equals(s2);
            } else {
                return false;
            }
        } else {
            return s2==null;
        }
    }
    
    /**
     * Normaliza una cadena de caracteres de la siguiente manera:
     * Elimina los espacios en blanco al inicio, al final y, si ocurren más de un espacio en blanco consecutivo, deja uno solo.
     * @param s
     * @return 
     */
    public static String normalizarString(String s) {
        if (s!=null) {
            return s.replaceAll("[ ]+", " ").trim();
        }
        return null;
    }
    
    /**
     * Este método normaliza un string y lo transforma a mayúsculas.
     * @param s: string a normalizar.
     * @return string normalizado y en mayúscula.
     */
     public static String normalizarStringMayuscula(String s) {
        if (s!=null) {
            return normalizarString(s).toUpperCase();
        }
        return null;
    }
    
     
     
}

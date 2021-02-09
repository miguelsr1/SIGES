/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

/**
 * Esta clase incluye métodos para procesar elementos de tipo Boolean.
 * Ejemplos son: determinar si dos boolean son iguales, considerando valores nulos.
 * @author Sofis Solutions
 */
public class BooleanUtils {
    
    /**
     * Determina si dos elementos de tipo boolean son iguales, considerando valores nulos.
     * @param s1: primer elemento a comparar.
     * @param s2: segundo elemento a comparar.
     * @return devuelve un valor que indica si son exactamente iguales.
     */
    public static boolean sonBooleanIguales(Boolean s1, Boolean s2) {
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
     * Determina si un elemento boolean es true, considerando casos nulos.
     * @param b: valor a determinar si es verdadero.
     * @return: true si el valor es true.
     */
    public static boolean isTrue(Boolean b){
        if (b == null ||b.booleanValue() == false){
            return false;
        }else{
            return true;
        }
    }
}

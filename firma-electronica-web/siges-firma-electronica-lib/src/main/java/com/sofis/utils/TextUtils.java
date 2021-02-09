/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofis.utils;

/**
 *
 * @author Sofis Solutions
 */
public class TextUtils {
    
    /**
     * Determina si una secuencia de caracteres es vacía (nula o largo 0).
     *
     * @param str el string a examinar
     * @return true if str es nulo o de largo 0.
     * (no considera el caso solo espacios en blanco). en ese caso es false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
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
    
}

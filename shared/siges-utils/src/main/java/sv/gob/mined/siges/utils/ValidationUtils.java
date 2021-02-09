/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author USUARIO
 */
public class ValidationUtils {

    public static Boolean validarDUI(String dui) {
        Boolean resultado = false;
        try {
            if (dui.length() <= 9) {
                dui = dui.replace("-", "");
                if (dui.length() < 9) {
                    int faltante = 9 - dui.length();
                    for (int i = 1; i <= faltante; i++) {
                        dui = "0" + dui;
                    }
                }

                int suma = 0;
                int multiplicador = 9;
                for (int i = 0; i < 8; i++) {
                    if (Character.isDigit(dui.charAt(i))) {
                        suma = (Integer.parseInt(String.valueOf(dui.charAt(i))) * multiplicador) + suma;
                        multiplicador--;
                    } else {
                        return false;
                    }
                }

                int mod = suma % 10;
                int resta = (10 - mod) % 10;

                if (resta == 0 || String.valueOf(resta).equals(String.valueOf(dui.charAt(8)))) {
                    resultado = true;
                } else {
                    resultado = false;
                }
            }
        } catch (Exception e) {
            //excepcion
        }

        return resultado;
    }
    
    public static Boolean validarEmail(String email, String expresionRegular){
                String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +"[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
                if (expresionRegular == null){
                    expresionRegular = emailPattern;
                }
                Pattern pattern = Pattern.compile(expresionRegular);
                Matcher matcher = pattern.matcher(email);              
                return matcher.matches();
                
    }

}

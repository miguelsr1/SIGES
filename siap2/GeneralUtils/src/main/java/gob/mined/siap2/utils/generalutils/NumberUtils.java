/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Esta clase incluye métodos para trabajar con números en diferentes formatos.
 * @author Sofis Solutions
 */
public class NumberUtils {

    /**
     * El valor 100 en tipo BigDecimal
     */
    public static final BigDecimal CIEN = new BigDecimal("100");

    /**
     * Este método permite calcular un porcentaje de un objeto BigDecimal redondeado.
     * @param porcentaje: porcentaje a calcular
     * @param numero: número al que se le calculará el porcentaje.
     * @param roundingMode: modo de redondeo.
     * @return porcentaje de número redondeado según el modo roundingMode.
     */
    public static BigDecimal porcentaje(BigDecimal porcentaje, BigDecimal numero, RoundingMode roundingMode) {
        if (porcentaje != null && numero != null) {
            BigDecimal iter = numero.multiply(porcentaje);
            return (iter.divide(CIEN, 2, roundingMode));
        }
        return null;
    }

    /**
     * Este método completa con ceros a la izquierda un string a partir de un número.
     * 
     * @param valor entero a transformar
     * @param largoStrSalida largo del string de salida
     * @return un string con el número a la derecha y con ceros  a la izquierda
     * Ejemplo: valor=12, largoStrSalida=5, la salida será 00012.
     * valor=34932, largoSalida=4, la salida será 34932
     */
    public static String ponerCerosALaIzquierda(Integer valor, Integer largoStrSalida) {
        String numberAsString = String.valueOf(valor);
        String shortString = numberAsString.substring(0, Math.min(numberAsString.length(), largoStrSalida));

        StringBuilder sb = new StringBuilder();
        while (sb.length() + shortString.length() < largoStrSalida) {
            sb.append('0');
        }
        sb.append(shortString);
        return sb.toString();
    }


    /**
     * Este método parsea un número como Integer. Si el string no es un valor numérico entero entonces devuelve nulo.
     * @param number
     * @return 
     */
    public static Integer getIntegerONull(String number){
        try{
            if (TextUtils.isEmpty(number)){
                return null;
            }
            return Integer.parseInt(number);
        }catch (Throwable t){
            return null;
        }
    }
    
    
    /**
     * locale para el dinero.
     */
    public final static String localeDineroTag = "es-SV";
    /**
     * pattern a utilizar para los valores monetarios.
     */
    public final static String patternDinero = "###,###,###,##0.00";
    
    /**
     * Este método formatea un número según el pattern de valores monetarios (patternDinero).
     * @param numero a formatear.
     * @return 
     */
    public static String nomberToString(BigDecimal numero) {
        if (numero == null){
            return "";
        }
        Locale loc = new Locale.Builder().setLanguageTag(localeDineroTag).build();
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(patternDinero);
        return df.format(numero);
    }

    /**
     * Este método determina la clase css a aplicar según el valor sea positivo o negativo.
     * @param numero
     * @return 
     */
    public String estiloNumero(BigDecimal numero) {
        if (numero!=null){
            if (numero.equals(numero.abs())) {
                return "normal";
            } else {
                return "rojo";
            }
        }
        return "normal";
    }

    
    
}

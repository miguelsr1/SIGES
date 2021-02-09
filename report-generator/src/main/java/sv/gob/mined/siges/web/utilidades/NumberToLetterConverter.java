/*
 * Sofis Solutions
 */

package sv.gob.mined.siges.web.utilidades;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class NumberToLetterConverter {
    /**
     * Evita la creación una nueva instancia de la clase usando la palabra 
     * reservada <code>new</code>.
     */
    private NumberToLetterConverter() {
    }
    
    /**
     * Convierte a letras un numero de la forma $123,456.32
     * @throws NumberFormatException 
     * Si valor del numero no es valido (fuera de rango o )
     * @return Numero en letras
     */
    public static String convertNumberToLetter(String number, Boolean procesarCentavos) throws NumberFormatException {
        return convertNumberToLetter(new BigDecimal(number), procesarCentavos);
    }

    /**
     * Convierte a letras un numero de la forma $123,456.32
     * @throws NumberFormatException 
     * Si valor del numero no es valido (fuera de rango o )
     * @return Numero en letras
     */
    public static String convertNumberToLetter(Integer number, Boolean procesarCentavos) throws NumberFormatException {
        return convertNumberToLetter(new BigDecimal(number), procesarCentavos);
    }
    
    /**
     * Convierte el número que recibe como argumento a su representación escrita con letra.
     *
     * @return  Una cadena de texto que contiene la representación con letra de
     *          la parte entera del número que se recibió como argumento.
     */
    public static String convertNumberToLetter(BigDecimal totalBigDecimal, Boolean procesarCentavos) {
        StringBuilder result = new StringBuilder();
        totalBigDecimal = totalBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        long parteEntera = totalBigDecimal.toBigInteger().longValue();
        int triUnidades      = (int)((parteEntera % 1000));
        int triMiles         = (int)((parteEntera / 1000) % 1000);
        int triMillones      = (int)((parteEntera / 1000000) % 1000);
        int triMilMillones   = (int)((parteEntera / 1000000000) % 1000);
 
        if (parteEntera == 0) {
            result.append("Cero ");
            return result.toString();
        }
        List<String> splitNumber = Arrays.asList(String.valueOf(totalBigDecimal).replace('.', '#').split("#"));
        
        if (triMilMillones > 0) result.append(triTexto(triMilMillones).toString() + "Mil ");
        if (triMillones > 0)    result.append(triTexto(triMillones).toString());
 
        if (triMilMillones == 0 && triMillones == 1) result.append("Millón ");
        else if (triMilMillones > 0 || triMillones > 0) result.append("Millones ");
 
        if (triMiles > 0)       result.append(triTexto(triMiles).toString() + "Mil ");
        if (triUnidades > 0)    result.append(triTexto(triUnidades).toString());
        
        if(procesarCentavos != null && procesarCentavos) {
            //Descompone los centavos
            String valor = splitNumber.get(1);
             if(valor.length()==1){
                 result.append(splitNumber.get(1)).append("0").append("/100 ");
             }else{
                 result.append(splitNumber.get(1)).append("/100 "); 
             }
        }
        return result.toString();
    }
 
    /**
     * Convierte una cantidad de tres cifras a su representación escrita con letra.
     *
     * @return  Una cadena de texto que contiene la representación con letra
     *          del número que se recibió como argumento.
     */
    private static StringBuilder triTexto(int n) {
        StringBuilder result = new StringBuilder();
        int centenas = n / 100;
        int decenas  = (n % 100) / 10;
        int unidades = (n % 10);
 
        switch (centenas) {
            case 0: break;
            case 1:
                if (decenas == 0 && unidades == 0) {
                    result.append("Cien ");
                    return result;
                }
                else result.append("Ciento ");
                break;
            case 2: result.append("Doscientos "); break;
            case 3: result.append("Trescientos "); break;
            case 4: result.append("Cuatrocientos "); break;
            case 5: result.append("Quinientos "); break;
            case 6: result.append("Seiscientos "); break;
            case 7: result.append("Setecientos "); break;
            case 8: result.append("Ochocientos "); break;
            case 9: result.append("Novecientos "); break;
        }
 
        switch (decenas) {
            case 0: break;
            case 1:
                if (unidades == 0) { 
                    result.append("Diez "); 
                    return result; 
                } else if (unidades == 1) { 
                    result.append("Once "); 
                    return result; 
                } else if (unidades == 2) { 
                    result.append("Doce "); 
                    return result; 
                } else if (unidades == 3) { 
                    result.append("Trece "); 
                    return result;
                } else if (unidades == 4) { 
                    result.append("Catorce "); 
                    return result; 
                } else if (unidades == 5) { 
                    result.append("Quince "); 
                    return result; 
                } else result.append("Dieci");
                break;
            case 2:
                if (unidades == 0) { 
                    result.append("Veinte "); 
                    return result; 
                } else result.append("Veinti");
                break;
            case 3: result.append("Treinta "); break;
            case 4: result.append("Cuarenta "); break;
            case 5: result.append("Cincuenta "); break;
            case 6: result.append("Sesenta "); break;
            case 7: result.append("Setenta "); break;
            case 8: result.append("Ochenta "); break;
            case 9: result.append("Noventa "); break;
        }
 
        if (decenas > 2 && unidades > 0)
            result.append("y ");
 
        switch (unidades) {
            case 0: break;
            case 1: result.append("Un "); break;
            case 2: result.append("Dos "); break;
            case 3: result.append("Tres "); break;
            case 4: result.append("Cuatro "); break;
            case 5: result.append("Cinco "); break;
            case 6: result.append("Seis "); break;
            case 7: result.append("Siete "); break;
            case 8: result.append("Ocho "); break;
            case 9: result.append("Nueve "); break;
        }
 
        return result;
    }
    
}
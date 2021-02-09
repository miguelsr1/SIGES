/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class MathFunctionsUtils {

    public static Double redondear(Double d, int decimales, RoundingMode redondeo) {
        return BigDecimal.valueOf(d).setScale(decimales, redondeo).doubleValue();
    }

    public static Double promedio(List<BigDecimal> numeros,Integer precision,RoundingMode roundMode) {        
        BigDecimal[] resultado = numeros.stream()
                .filter(bd -> bd != null)
                .map(bd -> new BigDecimal[]{bd, BigDecimal.ONE})
                .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)})
                .get();
        return  resultado[0].divide(resultado[1], precision, roundMode).doubleValue();
    }

    public static Double mayor(List<Double> numeros) {
        Double mayor = -1d;
        for (Double num : numeros) {
            if (num > mayor) {
                mayor = num;
            }
        }
        return mayor == -1d ? null : mayor;
    }

    public static Double mediana(List<Double> numeros) {
        Collections.sort(numeros);
        Integer medio = numeros.size() / 2;
        return numeros.get(medio);
    }
}

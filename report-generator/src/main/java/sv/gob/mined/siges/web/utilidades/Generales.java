/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class Generales {

    static DateTimeFormatter dateTimeformat = null;
    static DecimalFormat formatter = new DecimalFormat("#,###.00");
    static String monedaDefaultCode = "$";
    static String formatoDefault = "dd/MM/yyyy";

    public static String getMesDescripcion(Integer mes) {
        String mesDEscripcion = "";
        switch (mes) {
            case 1:
                mesDEscripcion = "Enero";
                break;
            case 2:
                mesDEscripcion = "Febrero";
                break;
            case 3:
                mesDEscripcion = "Marzo";
                break;
            case 4:
                mesDEscripcion = "Abril";
                break;
            case 5:
                mesDEscripcion = "Mayo";
                break;
            case 6:
                mesDEscripcion = "Junio";
                break;
            case 7:
                mesDEscripcion = "Julio";
                break;
            case 8:
                mesDEscripcion = "Agosto";
                break;
            case 9:
                mesDEscripcion = "Septiembre";
                break;
            case 10:
                mesDEscripcion = "Octubre";
                break;
            case 11:
                mesDEscripcion = "Noviembre";
                break;
            case 12:
                mesDEscripcion = "Diciembre";
                break;
        }

        return mesDEscripcion;
    }
    
    public static String getDateFormat(LocalDate date) {
        if (date != null ) {
            dateTimeformat = DateTimeFormatter.ofPattern(formatoDefault);
            return date.format(dateTimeformat);
        }
        return "";
    }

    public static String getDateFormat(LocalDate date, String format) {
        dateTimeformat = DateTimeFormatter.ofPattern(StringUtils.isNotBlank(format) ? format : formatoDefault);
        if (date != null && format != null) {
            return date.format(dateTimeformat);
        }
        return "";
    }
    
    public static String getDateTimeFormat(LocalDateTime date, String format) {
        dateTimeformat = DateTimeFormatter.ofPattern(StringUtils.isNotBlank(format) ? format : formatoDefault);
        if (date != null && format != null) {
            return date.format(dateTimeformat);
        }
        return "";
    }

    public static String getValorMonetario(String moneda, BigDecimal valor) {
        if (valor != null) {
            return (StringUtils.isNotBlank(moneda) ? moneda : monedaDefaultCode) + " " + formatter.format(valor);
        }
        return "";
    }

    public static String formatMonto(BigDecimal valor) {
        if (valor != null) {
            return formatter.format(valor);
        }
        return "";
    }
}

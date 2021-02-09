/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Sofis Solutions
 */
public class DatesUtils {

    /**
     * Este método permite determinar si dos fechas son iguales, considerando valores nulos.
     * @param s1: primera fecha
     * @param s2: segunda fecha
     * @return si las dos fechas son iguales
     */
    public static boolean sonStringIguales(Date s1, Date s2) {
        if (s1 != null) {
            if (s2 != null) {
                return s1.equals(s2);
            } else {
                return false;
            }
        } else {
            return s2 == null;
        }
    }

    /**
     * Suma o resta dias a una fecha. Resta si dias < 0
     *
     * @param fecha
     * @param dias
     * @return
     */
    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    /**
     * Este método permite obtener el año de una fecha.
     * @param d es la fecha
     * @return el año de la fecha.
     */
    public static int getYearOfDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return (calendar.get(Calendar.YEAR));
    }

    /**
     * Este método devuelve el mes de una fecha.
     * @param d es la fecha
     * @return mes de la fecha
     */
    public static int getMonthOfDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return (calendar.get(Calendar.MONTH));
    }

    /**
     * Este método permite calcular la cantidad de meses entre dos fechas.
     * @param s1: fecha inicial.
     * @param s2: fecha final
     * @return cantidad de meses entre las dos fechas.
     */
    public static int monthsBetween(Date s1, Date s2) {
        final Calendar d1 = Calendar.getInstance();
        d1.setTime(s1);
        final Calendar d2 = Calendar.getInstance();
        d2.setTime(s2);
        int diff = (d2.get(Calendar.YEAR) - d1.get(Calendar.YEAR)) * 12 + d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
        return diff;
    }

    /**
     * Este método adiciona meses a una fecha.
     * @param d: fecha a la que se le deben sumar meses.
     * @param meses: cantidad de meses a sumar.
     * @return: fecha inicial con meses adicionados.
     */
    public static Date sumarMeses(Date d, int meses) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        //Le resto 1 al mes para que no sume el mes actual
        cal.add(Calendar.MONTH, (meses - 1));
        return cal.getTime();
    }

    /**
     * Este método devuelve una fecha como una cadena de caracteres en formato dd/MM/yyyy
     * @param fecha a formatear
     * @return fecha en el formato indicado.
     */
    public static String dateToString(Date fecha) {
        if (fecha == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(fecha);
    }

    /**
     * Parsea una fecha en formato String, considerando que el string sea nulo.
     * @param date, fecha a procesar.
     * @return: fecha en formato Date
     * @throws ParseException 
     */
    public static Date stringToDate(String date) throws ParseException {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.parse(date);
    }

    /**
     * Este método calcula la diferencia entre dos objetos de tipo Date, en milisegundos.
     *
     * @param date1 la fecha más vieja.
     * @param date2 la fecha más nueva.
     * @param timeUnit ignorado
     * @return la diferencia entre las dos fechas en milisegundos
     */
    public static Long getDateDiff(Date fechaInicial, Date fechaFinal, TimeUnit timeUnit) {
        Long diffInMillies = fechaFinal.getTime() - fechaInicial.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    
    
    
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfYear(Integer anio) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return getStartOfDay(cal.getTime());
    }

    public static Date getEndOfYear(Integer anio) {
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, anio);
        cal2.set(Calendar.MONTH, 11);
        cal2.set(Calendar.DAY_OF_MONTH, 31);
        return getEndOfDay(cal2.getTime());
    }
    
    public static Date getStartOfMonth(Integer anio, Integer mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getStartOfDay(cal.getTime());
    }
        
    public static Date getEndOfMonth(Integer anio, Integer mes) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.MONTH, mes);
        int ultimoDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, ultimoDia);
        return getEndOfDay(cal.getTime());
    }

    public static Date setYearToDate(Date d, int anio) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.YEAR, anio);
        return cal.getTime();
    }


    
    
}

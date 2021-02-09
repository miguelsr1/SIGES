/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Sofis Solutions
 */
public class DateUtils {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    public static Date sumMinutes(Date date, int minutes) {
        long t = date.getTime();
        return new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
    }

    //compara tiempo en minutos
    public static int compareTimes(Date d1, Date d2) {

        Date d = new Date();
        String hora1 = timeToString(d1);
        String hora2 = timeToString(d2);

        String[] parts = hora1.split(":");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(d.getTime());
        cal1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal1.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

        parts = hora2.split(":");
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(d.getTime());
        cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal2.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

        return cal1.compareTo(cal2);

        /*
         long t1 = (long) (d1.getTime() % (24 * 60 * 60 * 1000L));
         long t2 = (long) (d2.getTime() % (24 * 60 * 60 * 1000L));
         return (t1 - t2);*/
    }

    public static String toString(Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(d);
    }

    public static String timeToString(Date d) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(d);
    }

    public static Date timeFromStringTime(String d) throws Exception {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.parse(d);
    }

    public static int getHoursOfTime(Date d) {
        String hora = timeToString(d);
        String[] parts = hora.split(":");
        return Integer.parseInt(parts[0]);
    }

    public static int getMinutesOfTime(Date d) {
        String hora = timeToString(d);
        String[] parts = hora.split(":");
        return Integer.parseInt(parts[1]);
    }

    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return (sdf.format(calendar.getTime()));
    }

    
    /**
     * Get a diff between two dates
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static Long getDateDiff(Date fechaInicial, Date fechaFinal, TimeUnit timeUnit) {
        Long diffInMillies = fechaFinal.getTime() - fechaInicial.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String dateToWords(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        NumeroALetra numeroALetraDia = new NumeroALetra();
        String fechaString = numeroALetraDia.convertirEntero(String.valueOf(day), false);

        DateFormat format2 = new SimpleDateFormat("MMMMM ", new Locale("es"));
        fechaString += " de " + format2.format(cal.getTime());

        int year = cal.get(Calendar.YEAR);
        NumeroALetra numeroALetraAnio = new NumeroALetra();
        fechaString += " del " + numeroALetraAnio.convertirEntero(String.valueOf(year), false);

        return fechaString;
    }

    public static String dateToWords2(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        NumeroALetra numeroALetraDia = new NumeroALetra();
        String fechaString = numeroALetraDia.convertirEntero(String.valueOf(day), false);

        DateFormat format2 = new SimpleDateFormat("MMMMM ", new Locale("es"));
        fechaString += " días del mes de " + format2.format(cal.getTime());

        int year = cal.get(Calendar.YEAR);
        NumeroALetra numeroALetraAnio = new NumeroALetra();
        fechaString += " del " + numeroALetraAnio.convertirEntero(String.valueOf(year), false);

        return fechaString;
    }
}

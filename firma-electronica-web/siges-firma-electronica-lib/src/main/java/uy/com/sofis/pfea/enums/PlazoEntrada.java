package uy.com.sofis.pfea.enums;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Sofis Solutions
 */
public enum PlazoEntrada {
    MINUTOS0("Hasta el comienzo de la función"),
    MINUTOS15("15 minutos antes de la función"),
    MINUTOS30("30 minutos antes de la función"),
    HORAS01("1 hora antes de la función"),
    HORAS02("2 horas antes de la función"),
    HORAS04("4 horas antes de la función"),
    DIAS01("1 día antes de la función"),
    DIAS02("2 días antes de la función"),
    DIAS05("5 días antes de la función"),
    SEMANAS01("1 semana antes de la función"),
    SEMANAS02("2 semanas antes de la función"),
    MESES01("1 mes antes de la función"),
    HORA10("Hasta las 10:00 del día de la función"),
    HORA12("Hasta las 12:00 del día de la función"),
    HORA14("Hasta las 14:00 del día de la función"),
    HORA16("Hasta las 16:00 del día de la función"),
    HORA18("Hasta las 18:00 del día de la función"),
    HORA20("Hasta las 20:00 del día de la función");

    private final String nombre;

    PlazoEntrada(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Date calcularLimite(Date fecha, boolean conHora) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        if (!conHora) {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            switch (this) {
                case MINUTOS15:
                case MINUTOS30:
                case HORAS01:
                case HORAS02:
                case HORAS04:
                    //Nada para hacer, el mismo día está bien
                    break;
                case DIAS01:
                    cal.add(Calendar.DATE, -1);
                    break;
                case DIAS02:
                    cal.add(Calendar.DATE, -2);
                    break;
                case DIAS05:
                    cal.add(Calendar.DATE, -5);
                    break;
                case SEMANAS01:
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    break;
                case SEMANAS02:
                    cal.add(Calendar.DAY_OF_MONTH, -14);
                    break;
                case MESES01:
                    cal.add(Calendar.DAY_OF_MONTH, -30);
                    break;
                case HORA10:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(10, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA12:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(12, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA14:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(14, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA16:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(16, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA18:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(18, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA20:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(20, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
            }
        } else {
            switch (this) {
                case MINUTOS15:
                    cal.add(Calendar.MINUTE, -15);
                    break;
                case MINUTOS30:
                    cal.add(Calendar.MINUTE, -30);
                    break;
                case HORAS01:
                    cal.add(Calendar.HOUR_OF_DAY, -1);
                    break;
                case HORAS02:
                    cal.add(Calendar.HOUR_OF_DAY, -2);
                    break;
                case HORAS04:
                    cal.add(Calendar.HOUR_OF_DAY, -4);
                    break;
                case DIAS01:
                    cal.add(Calendar.DATE, -1);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case DIAS02:
                    cal.add(Calendar.DATE, -2);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case DIAS05:
                    cal.add(Calendar.DATE, -5);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case SEMANAS01:
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case SEMANAS02:
                    cal.add(Calendar.DAY_OF_MONTH, -14);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case MESES01:
                    cal.add(Calendar.DAY_OF_MONTH, -30);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    break;
                case HORA10:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(10, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA12:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(12, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA14:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(14, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA16:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(16, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA18:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(18, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
                case HORA20:
                    cal.set(Calendar.HOUR_OF_DAY, Math.min(20, cal.get(Calendar.HOUR_OF_DAY)));
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    break;
            }
        }
        return cal.getTime();
    }
}

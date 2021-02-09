/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.caux;

import java.time.LocalDate;

/**
 *
 * @author Sofis Solutions
 */
public class KeyControlAsistencia {
    
    public final Long seccion;
    public final LocalDate fecha;

    public KeyControlAsistencia(Long seccion, LocalDate fecha) {
        this.seccion = seccion;
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyControlAsistencia)) return false;
        KeyControlAsistencia key = (KeyControlAsistencia) o;
        return seccion.equals(key.seccion) && fecha.isEqual(key.fecha);
    }

    @Override
    public int hashCode() {
        int result = seccion.intValue();
        result = 31 * result + (fecha.getYear()+fecha.getMonthValue()+fecha.getDayOfMonth());
        return result;
    }
}

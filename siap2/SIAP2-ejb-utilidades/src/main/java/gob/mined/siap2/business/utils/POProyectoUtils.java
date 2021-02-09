package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POGInsumoAnio;
import gob.mined.siap2.entities.data.impl.POGProyecto;
import gob.mined.siap2.entities.data.impl.POIndicadorLinea;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class POProyectoUtils {

    public static POLinea findLinea(List<POLinea> lienas, ProyectoEstProducto producto) {
        for (POLinea linea : lienas) {
            if (linea.getProducto().equals(producto)) {
                return linea;
            }
        }
        return null;
    }

    public static POActividadProyecto findActividad(List<POActividadProyecto> lienas, ActividadPOProyecto codiguera) {
        for (POActividadProyecto iter : lienas) {
            if (iter.getActividadCodiguera().equals(codiguera)) {
                return iter;
            }
        }
        return null;
    }

    public static void initPOG(POGProyecto pog) {
        for (POLinea linea : pog.getLineas()) {
            POProyectoUtils.initPOLinea(linea);
        }
    }

    public static void initPOAProyecto(POAProyecto poa) {
        for (POLinea linea : poa.getLineas()) {
            POProyectoUtils.initPOLinea(linea);
        }
        poa.getRiesgos().isEmpty();
    }

    public static void initPOLinea(POLinea linea) {
        linea.getColaboradoras().isEmpty();
        for (ComboValorSeguimiento c : linea.getValoresProducto()) {
            c.getValores().isEmpty();
        }
        for (POIndicadorLinea li : linea.getIndicadores()) {
            for (ComboValorSeguimiento c : li.getComboValoresSeguimiento()) {
                c.getValores().isEmpty();
            }
        }
        for (POActividadBase a : linea.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                for (FlujoCajaAnio fca : i.getFlujosDeCajaAnio()) {
                    fca.getFlujoCajaMenusal().isEmpty();
                }
                i.getMontosFuentes().isEmpty();
                if (i instanceof POGInsumo) {
                    POGInsumo pogInsumo = (POGInsumo) i;
                    for (POGInsumoAnio anio : pogInsumo.getDistribucionAnios()) {
                        anio.getMontosFuentes().isEmpty();
                    }

                    if (pogInsumo.getTramo() != null) {
                        pogInsumo.getTramo().getParipassus().isEmpty();
                    }
                }
            }
        }
    }

    /**
     * recalcula los montos totales *
     */
    public static void recalcularMontosLineas(List<POLinea> lineas) {
        for (POLinea linea : lineas) {
            for (POActividadBase a : linea.getActividades()) {
                for (POInsumos i : a.getInsumos()) {
                    BigDecimal montoTotalInsumo = BigDecimal.ZERO;
                    for (POMontoFuenteInsumo mi : i.getMontosFuentes()) {
                        montoTotalInsumo = montoTotalInsumo.add(mi.getMonto());
                    }
                    i.setMontoTotal(montoTotalInsumo);
                }
            }
        }
    }

}

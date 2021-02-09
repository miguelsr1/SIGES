/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POGInsumoAnio;
import gob.mined.siap2.entities.data.impl.POGProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.enums.EstadoPOGProyecto;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 *
 * @author Sofis Solutions
 */
public class POGUtils {

    public static BigDecimal getUsadoEnTramo(POGProyecto pog, ProyectoAporteTramoTramo tramo) {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea linea : pog.getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (Iterator<POInsumos> it = actividad.getInsumos().iterator(); it.hasNext();) {
                    POGInsumo insumo = (POGInsumo) it.next();
                    if (insumo.getTramo() != null && insumo.getTramo().equals(tramo)) {
                        usado = usado.add(insumo.getMontoTotal());
                    }
                }
            }
        }
        return usado;

    }

    public static POGInsumoAnio getAnioInsumo(POGInsumo insumo, int anio) {
        for (POGInsumoAnio iter : insumo.getDistribucionAnios()) {
            if (iter.getAnio() != null && iter.getAnio().intValue() == anio) {
                return iter;
            }
        }
        return null;
    }

    public static boolean habilitadoActividad(POGProyecto pog, POActividadBase actividad, AnioFiscal anioEjecucion) {

        if (pog.getEstado() == EstadoPOGProyecto.EDICION_UT) {
            return true;
        }
        if (actividad == null) {
            return true;
        }
        if (anioEjecucion == null) {
            return false;
        }

        POGActividadProyecto actividadPOG = (POGActividadProyecto) actividad;
        if (actividadPOG.getAnioFin() == null) {
            return true;
        }
        if (actividadPOG.getAnioFin().compareTo(anioEjecucion.getAnio()) >= 0) {
            return true;
        }
        return false;
    }

    public static boolean habilitadoInsumo(POGProyecto pog, POGInsumo insumo, AnioFiscal anioEjecucion) {

        if (pog.getEstado() == EstadoPOGProyecto.EDICION_UT) {
            return true;
        }
        if (insumo == null) {
            return true;
        }
        if (anioEjecucion == null) {
            return false;
        }

        Integer anio = DatesUtils.getYearOfDate(insumo.getFechaContratacion());
        if (anio.compareTo(anioEjecucion.getAnio()) >= 0) {
            return true;
        }

        return false;
    }
    
    
    
    public static boolean existeLinea (POGProyecto pog, ProyectoEstProducto producto){
        for (POLinea linea : pog.getLineas()){
            if (linea.getProducto().equals(producto)){
                return true;
            }
        }
        return false;
    }
}

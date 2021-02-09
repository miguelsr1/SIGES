/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.OrigenDistribuccionMontoInsumo;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.TechoActividadAccionCentral;
import gob.mined.siap2.entities.data.impl.TechoAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class POAUtils {

    public static BigDecimal getMonto(ConMontoPorAnio objeto, UnidadTecnica ut, AnioFiscal anioFiscal) {

        if (objeto.getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
            AccionCentral accionCentral = (AccionCentral) objeto;
            for (TechoActividadAccionCentral techo : accionCentral.getMontosUT()) {
                if (techo.getUnidadTecnica().equals(ut) && techo.getAnioFiscal().equals(anioFiscal)) {
                    return techo.getMonto();
                }
            }
        } else if (objeto.getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
            AsignacionNoProgramable asignacionNoProgramable = (AsignacionNoProgramable) objeto;
            if (asignacionNoProgramable.getUnidadTecnica().equals(ut)) {
                for (TechoAsignacionNoProgramable techos : asignacionNoProgramable.getTechosPlanificacion()) {
                    if (techos.getAnioFiscal().equals(anioFiscal)) {
                        return techos.getMonto();
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getMontoTotal(ConMontoPorAnio objeto, AnioFiscal anioFiscal) {
        BigDecimal sumaTodasUT = BigDecimal.ZERO;
        if (objeto.getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
            AccionCentral accionCentral = (AccionCentral) objeto;
            for (TechoActividadAccionCentral techo : accionCentral.getMontosUT()) {
                if (techo.getAnioFiscal().equals(anioFiscal)) {
                    sumaTodasUT = sumaTodasUT.add(techo.getMonto());
                }
            }
        } else if (objeto.getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
            AsignacionNoProgramable asignacionNoProgramable = (AsignacionNoProgramable) objeto;
            for (TechoAsignacionNoProgramable techos : asignacionNoProgramable.getTechosPlanificacion()) {
                if (techos.getAnioFiscal().equals(anioFiscal)) {
                    sumaTodasUT = sumaTodasUT.add(techos.getMonto());
                }
            }
        }
        return sumaTodasUT;
    }

    public static String getNombreActividad(POActividadBase actividad) {
        if (actividad == null) {
            return null;
        }
        if (actividad instanceof POActividadProyecto) {
            POActividadProyecto actividadProyecto = (POActividadProyecto) actividad;
            if (actividadProyecto.getActividadCodiguera() != null) {
                return actividadProyecto.getActividadCodiguera().getNombre();
            }
        } else if (actividad instanceof POActividadAsignacionNoProgramable) {
            POActividadAsignacionNoProgramable actividadNoProgramable = (POActividadAsignacionNoProgramable) actividad;
            if (actividadNoProgramable.getActividadNP() != null) {
                return actividadNoProgramable.getActividadNP().getNombre();
            }
        } else if (actividad instanceof POActividad) {
            POActividad poaActividad = (POActividad) actividad;
            return poaActividad.getNombre();

        } else if (actividad instanceof POGActividadProyecto) {
            POGActividadProyecto actividadProyecto = (POGActividadProyecto) actividad;
            if (actividadProyecto.getActividadCodiguera() != null) {
                return actividadProyecto.getActividadCodiguera().getNombre();
            }
        }
        return null;
    }

    public static POMontoFuenteInsumo getFuenteEnInsumo(POInsumos insumo, OrigenDistribuccionMontoInsumo f) {
        for (POMontoFuenteInsumo iter : insumo.getMontosFuentes()) {
            if (iter.getFuente().getFuenteRecursos().getId().equals(f.getFuenteRecursos().getId())) {
                return iter;
            }
        }
        return null;
    }

}

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.TechoPlanificacionAccionCentral;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Esta es una clase utilitaria de Planificación Estratégica
 * @author Sofis Solutions
 */
public class PlanificacionEstrategicaUtils {

    /**
     * Esta clase devuelve el techo de acción central en un determinado año para una determina unidad técnica.
     * @param p
     * @param idUT
     * @param anio
     * @return 
     */
    public static BigDecimal getAccionCentralTechoUT(PlanificacionEstrategica p, Integer idUT, Integer anio) {
        BigDecimal techoUT = null;
        Iterator<TechoPlanificacionAccionCentral> iterT = p.getTechosAccionCentral().iterator();
        while (iterT.hasNext() && techoUT == null) {
            TechoPlanificacionAccionCentral techoP = iterT.next();
            if (techoP.getAnioFiscal().getAnio().equals(anio) && techoP.getUnidadTecnica().getId().equals(idUT)) {
                return techoP.getMonto();
            }
        }

        return BigDecimal.ZERO;

    }

}

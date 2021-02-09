/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.AnioFiscal;

/**
 *
 * @author rgonzalez
 */
public class AnioFiscalUtils {
    
    public static AnioFiscal crearAnioFiscal (Integer anio){
        AnioFiscal anioF = new AnioFiscal();
        anioF.setAnio(anio);
        anioF.setNombre(anio.toString());
        anioF.setHabilitadoEjecucion(Boolean.FALSE);
        anioF.setHabilitadoPlanificacion(Boolean.FALSE);
        return anioF;
    }
}

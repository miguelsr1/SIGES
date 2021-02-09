/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;

/**
 *
 * @author Sofis Solutions
 */
public class InsumoUtils {
    
    public static String OEGToString(ObjetoEspecificoGasto oeg){
        return String.valueOf(oeg.getClasificador());
    }
    
    
    public static boolean esMontoDeGOES(POMontoFuenteInsumo fuenteMonto){
        if (fuenteMonto == null || fuenteMonto.getFuente() == null || fuenteMonto.getFuente().getFuenteRecursos() == null ){
            return false;
        }
        String codigoFuente = fuenteMonto.getFuente().getFuenteRecursos().getCodigo();
        return ConstantesEstandares.CODIGO_FUENTE_RECURSO_GOES.equals(codigoFuente);
    }
}

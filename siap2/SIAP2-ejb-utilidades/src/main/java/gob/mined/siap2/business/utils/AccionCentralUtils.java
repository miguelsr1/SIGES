/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.TechoAccionCentral;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class AccionCentralUtils {

 

    public static TechoAccionCentral getMontoUnidadTecnica(List<TechoAccionCentral> unidadesTecnicas, Integer idUnidadTecnica) {
        for (TechoAccionCentral u : unidadesTecnicas) {
            if (u.getUnidadTecnica().getId().equals(idUnidadTecnica)) {
                return u;
            }
        }
        return null;
    }



}

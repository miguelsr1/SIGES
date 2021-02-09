/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class OEGUtils {

    public static List<String> oegToList(ObjetoEspecificoGasto oeg) {
        List<String> oegs = new ArrayList(ConstantesEstandares.CANTIDAD_NIVELES_OEG);
        while (oeg != null) {
            String actualOEG = String.valueOf(oeg.getClasificador());
            if (oeg.getPadre() != null) {
                actualOEG = actualOEG.replaceFirst(oeg.getPadre().getClasificador().toString(), "");
            }
            oegs.add(0, actualOEG);
            oeg = oeg.getPadre();

        }

        for (int iter = oegs.size(); iter < ConstantesEstandares.CANTIDAD_NIVELES_OEG; iter++) {
            oegs.add("");
        }
        return oegs;
    }

}

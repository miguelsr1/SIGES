/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.data.impl.MetodoAdquisicionTarea;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.entities.enums.TipoTarea;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta es una clase utilitaria vinculada a Metodos de Adquisicion
 *
 * @author Sofis Solutions
 */
public class MetodoAdquisicionUtils {

    /**
     * Este método devuelve el método de adquisición según una tarea.
     *
     * @param lista
     * @param tipo
     * @return
     */
    public static MetodoAdquisicionTarea findTarea(List<MetodoAdquisicionTarea> lista, TipoTarea tipo) {
        if (lista == null) {
            return null;
        }
        for (MetodoAdquisicionTarea t : lista) {
            if (t.getTipoTarea() == tipo) {
                return t;
            }
        }
        return null;
    }

    public static Map<PasosProcesoAdquisicion, TipoTarea[]> MapeoPasosProcesoAdqConTiposTareaMetodoAdq() {
        Map<PasosProcesoAdquisicion, TipoTarea[]> mapMapeoPasosTareas = new HashMap<PasosProcesoAdquisicion, TipoTarea[]>();

        TipoTarea[] tareasInicializacion = {TipoTarea.INICIALIZACION};
        mapMapeoPasosTareas.put(PasosProcesoAdquisicion.INICIALIZACION, tareasInicializacion);

        return mapMapeoPasosTareas;
    }

    public static void ordenarTareasPorInicio(List<MetodoAdquisicionTarea> tareas) {
        Collections.sort(tareas, new Comparator<MetodoAdquisicionTarea>() {
            @Override
            public int compare(MetodoAdquisicionTarea o1, MetodoAdquisicionTarea o2) {
                if (o1.getInicioEnDias() == null || o2.getInicioEnDias() == null) {
                    return 0;
                }
                return o1.getInicioEnDias().compareTo(o2.getInicioEnDias());
            }
        });
    }

}

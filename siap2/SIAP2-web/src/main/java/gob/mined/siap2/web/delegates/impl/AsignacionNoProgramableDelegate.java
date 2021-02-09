/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.AsignacionNoProgramableBean;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de Asignación No programable
 *
 * @author Sofis Solutions
 */
@Named
public class AsignacionNoProgramableDelegate implements Serializable {

    @Inject
    private AsignacionNoProgramableBean bean;

    /**
     * Este método crea o actualiza los datos de una asignación no programable.
     *
     * @param idPlanificacion
     * @param asignacion
     */
    public void crearOActualizarAsignacionNoProgramable(Integer idPlanificacion, AsignacionNoProgramable asignacion) {
        bean.crearOActualizarAsignacionNoProgramable(idPlanificacion, asignacion);
    }

    /**
     * Este método permite eliminar una asignación no programable según su id.
     *
     * @param idAsignacion
     */
    public void eleiminarAsignacionNoProgramable(Integer idAsignacion) {
        bean.eleiminarAsignacionNoProgramable(idAsignacion);
    }

    /**
     * Este método permite obtener las asignaciones no programables de una
     * planificación estratégica.
     *
     * @param idPlanificacion
     * @return
     */
    public List<AsignacionNoProgramable> getAsignacionesEnPlanificacion(Integer idPlanificacion) {
        return bean.getAsignacionesEnPlanificacion(idPlanificacion);
    }

    /**
     * Este método permite guardar los techos de asignaciones no programables.
     *
     * @param l
     */
    public void guardarTechosAsignaiconesNoProgramables(List<AsignacionNoProgramable> l) {
        bean.guardarTechosAsignaiconesNoProgramables(l);
    }

}

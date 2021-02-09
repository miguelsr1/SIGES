/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase incluye los métodos de acceso a datos de ASignaciones No programables.
 * @author Sofis Solutions
 */
@JPADAO
public class AsignacionNoProgramableDAO extends EclipselinkJpaDAOImp<AsignacionNoProgramable, Integer> {

    /**
     * Este método permite ubicar una Asignación No Programable por un id dado.
     * @param primaryKey
     * @return 
     */
    public AsignacionNoProgramable find(Integer primaryKey) {
        return entityManager.find(AsignacionNoProgramable.class, primaryKey);
    }
 
    /**
     * Este método devuelve todas las asignaciones no programables de una planificación.
     * @param idPlanificacion
     * @return 
     */
     public List<AsignacionNoProgramable> getAsignaiconesEnplanificaion(Integer idPlanificacion) {
        return entityManager.createQuery("SELECT a FROM AsignacionNoProgramable a"
                + " WHERE a.planificacionEstrategica.id =:idPlanificacion "
                + " ORDER BY a.nombre")
                .setParameter("idPlanificacion", idPlanificacion)
                .getResultList();
    }
    
 
    
}

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase incluye los métodos para el acceso a datos de planificacion estrategica.
 * @author Sofis Solutions
 */
@JPADAO
public class PlanificacionEstrategicaDAO extends EclipselinkJpaDAOImp<Proyecto, Integer> {
    
   
    /**
     * Este método devuelve la lista de planificaciones estratégicas en un determinado estado
     * @param estado de la planificación estratégica.
     * @return lista de planificaciones estratégicas en el estado dado.
     */    
    public List<PlanificacionEstrategica> getPlanificacionesByEstado(EstadoComun estado){
        return entityManager.createQuery("SELECT pla "
                + " FROM PlanificacionEstrategica pla"
                + " WHERE pla.estado=:estado "
                + " ORDER BY pla.nombre")
                .setParameter("estado", estado)
                .getResultList();
    }

    
    /**
     * Este método devuelve las planificaciones estratégicas con lineas estratégicas.
     * @param idLineaEstrategica id de la línea estratégica.
     * @return lista de planificaciones estratégicas asociadas a la línea indicada.
     */
   public List<PlanificacionEstrategica> getPlanificacionesConLinea(Integer idLineaEstrategica){
        return entityManager.createQuery("SELECT pla "
                + " FROM PlanificacionEstrategica pla, pla.lineasEstrategicas lineas"
                + " WHERE lineas.id=:idLineaEstrategica ")
                .setParameter("idLineaEstrategica", idLineaEstrategica)
                .getResultList();
    }

}

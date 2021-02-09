/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase incluye los métodos para el acceso a datos de Línea Estratégica.
 * @author Sofis Solutions
 */
@JPADAO
public class LineaEstrategicaDAO extends EclipselinkJpaDAOImp<Proyecto, Integer> {
    
     
    /**
     * Este método permite obtener todas las líneas estratégicas de una planificación, en un determinado estado
     * @param idPlanificacion
     * @param estado
     * @return 
     */
    
    public List<LineaEstrategica> getLineas(Integer idPlanificacion, EstadoComun estado ){
        return entityManager.createQuery("SELECT linea "
                + " FROM PlanificacionEstrategica pla, pla.lineasEstrategicas linea"
                + " WHERE pla.id=:idPlanificacion "
                + " AND linea.estado=:estado "
                + " ORDER BY linea.nombre")
                .setParameter("idPlanificacion", idPlanificacion)
                .setParameter("estado", estado)
                .getResultList();
    }
    
    /**
     * Este método permite obtener todas las líneas en un determinado estado
     * @param estado
     * @return 
     */
    public List<LineaEstrategica> getLineas(EstadoComun estado ){
        return entityManager.createQuery("SELECT linea "
                + " FROM LineaEstrategica linea"
                + " WHERE linea.estado=:estado "
                + " ORDER BY linea.nombre")
                .setParameter("estado", estado)
                .getResultList();
    }
   
    /**
     * Este método permite obtener todas las líneas estratégicas de una planificación.
     * @param idPlanificacion
     * @return 
     */
    public List<LineaEstrategica> getLineas(Integer idPlanificacion){
        return entityManager.createQuery("SELECT linea "
                + " FROM PlanificacionEstrategica pla, pla.lineasEstrategicas linea"
                + " WHERE pla.id=:idPlanificacion "
                + " ORDER BY linea.nombre")
                .setParameter("idPlanificacion", idPlanificacion)
                .getResultList();
    }
    
    

}

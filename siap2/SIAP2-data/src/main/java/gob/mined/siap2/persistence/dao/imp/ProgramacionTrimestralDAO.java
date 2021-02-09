/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

/**
 * Esta clase incluye los métodos de acceso a datos de POA
 * @author Sofis Solutions
 */
@JPADAO
public class ProgramacionTrimestralDAO extends EclipselinkJpaDAOImp implements Serializable {

    /**
     * Este método permite lockear una entidad. Aplicar en los casos de mutua
     * exclusión.
     *
     * @param entity
     * @param id
     */
    public void lock(Class entity, int id) {
        Object lock = entityManager.find(entity, id);
        entityManager.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Devuelve el entityManager
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Este método devuelve el objeto de una determina clase que tiene una clave
     * primaria indicada.
     *
     * @param c clase
     * @param primaryKey clave primaria
     * @return unidad técnica con la clave primaria indicada
     */
    public ProgramacionTrimestral find(Class c, Integer primaryKey) {
        return (ProgramacionTrimestral) entityManager.find(c, primaryKey);
    }
 
}
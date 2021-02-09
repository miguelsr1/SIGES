/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

/**
 * Clase que gestiona elementos en forma genérica.
 * Para guardar siempre llamar al DAO así verifica su se guarda en la linea de trabajo o base
 * @author Sofis Solutions
 */

 
@JPADAO
public class GeneralDAO extends EclipselinkJpaDAOImp implements Serializable {
 
    private static final Logger logger = Logger.getLogger(GeneralDAO.class.getName());

    /**
     * Este método permite lockear una entidad.
     * Aplicar en los casos de mutua exclusión.
     * @param entity
     * @param id 
     */
    public void lock(Class entity, int id) {
        Object lock = entityManager.find(entity, id);
        entityManager.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }
    public void lockPessimisticForceIncremenet(Class entity, int id) {
        Object lock = entityManager.find(entity, id);
        entityManager.lock(lock, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    }
    
    
    /**
     * Este método permite seleccionar un objeto (entidad) en forma pesimistic_write
     * (select for update)
     * @param entity
     * @param id
     * @return 
     */
    public Object selectForUpdate(Class entity, int id) {
        return entityManager.find(entity, id, LockModeType.PESSIMISTIC_WRITE);
    }
    
    
    
    /**
     * Devuelve el entityManager
     * @return 
     */
    public EntityManager getEntityManager(){
        return this.entityManager;
    }
    

    /**
     * Este método devuelve el objeto de una determina clase que tiene una clave primaria indicada.    
    */
    public Object find(Class c, Object primaryKey) {
        return entityManager.find(c, primaryKey);
    }

    /**
     * Esta operación realiza el merge de un objeto.
     * @param o
     * @return 
     */
    public Object merge(Object o) {
        if (o instanceof ManejoLineaBaseTrabajo) {
            ManejoLineaBaseTrabajo converiones = (ManejoLineaBaseTrabajo) o;
            if (converiones.getLineaTrabajo() != null) {
                logger.log(Level.SEVERE, "Error: se esta tratando de guardar un elemento de la linea base");
                throw (new DAOObjetoNoEditableException());
            }
        }
        return entityManager.merge(o);
    }
}

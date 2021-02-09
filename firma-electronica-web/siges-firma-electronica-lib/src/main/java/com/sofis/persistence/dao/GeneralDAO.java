/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofis.persistence.dao;

import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import uy.com.sofis.pfea.anotaciones.JPADAO;

/**
 *
 * @author Sofis Solutions
 */
@JPADAO
public class GeneralDAO extends HibernateJpaDAOImp implements Serializable{

    /**
     * Este método permite lockear una entidad.
     * Aplicar en los casos de mutua exclusion.
     * @param entity
     * @param id
     */
    public void lock(Class entity, int id) {
        Object lock = entityManager.find(entity, id);
        entityManager.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Este método permite lockear globalmente por id
     * Aplicar en los casos de mutua exclusion.
     *
     * @param id
     */
    public boolean lockGlobalById(long id) {
        // Intenta liberar el lock por si lo tiene esta instancia
        boolean lockOk = (boolean) entityManager.createNativeQuery("SELECT pg_advisory_unlock(" + id + ")").getSingleResult();
        // intentar obtener el lock
        lockOk = (boolean) entityManager.createNativeQuery("SELECT pg_try_advisory_lock(" + id + ")").getSingleResult();
        return lockOk;
    }

    /**
     * Este método deslockea globalmente por id
     * Aplicar en los casos de mutua exclusion.
     *
     * @param id
     */
    public void unlockGlobalById(long  id) {
        // intentar liberar el lock (si lo tiene esta instancia)
        entityManager.createNativeQuery("SELECT pg_advisory_unlock(" + id + ")").getSingleResult();
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




}

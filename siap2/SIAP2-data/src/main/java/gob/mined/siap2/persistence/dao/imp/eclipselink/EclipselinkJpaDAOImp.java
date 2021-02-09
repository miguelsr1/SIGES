/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp.eclipselink;

import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.JpaDAOImp;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
 

/**
 * Clase desarrollada por Sofis Solutions implementación DAO de Hibernate con versionado.
 * @author Sofis Solutions
 */
public class EclipselinkJpaDAOImp<T, ID extends Serializable> extends JpaDAOImp<T, ID> {

    private static EntityManagerFactory emFactoryCustom = null;
    
    public EclipselinkJpaDAOImp(){
        super();
    }

    public EclipselinkJpaDAOImp(EntityManager em) {
        super(em);
        if (emFactoryCustom != null) {
            this.entityManager = emFactoryCustom.createEntityManager();
        }
    }

    public T create(T entity, String userName, String description) throws DAOGeneralException {
        setAudited(entity, userName, description);
        return super.create(entity);
    }

    public T update(T entity, String userName, String description) throws DAOGeneralException {
        setAudited(entity, userName, description);
        return super.update(entity);
    }

    public void delete(T entity, String userName, String description) throws DAOGeneralException {
        setAudited(entity, userName, description);
        try {
            super.delete(entity);
        } catch (DAOGeneralException e) {
 
            throw e; 
        }
    }
 
    private void setAudited(T entity, String userName, String description) {
 
    }

    public Object persist(Object o, String userName, String desc) throws DAOGeneralException {
        return super.create((T) o);
    }

}

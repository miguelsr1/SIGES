/*
 *  Clase desarrollada por Sofis Solutions 
 * 
 */
package com.sofis.persistence.dao.imp.hibernate;

import com.sofis.persistence.dao.exceptions.DAOConstraintViolationException;
import com.sofis.persistence.dao.imp.*;
import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Clase desarrollada por Sofis Solutions implementacion DAO de Hibernate con Versionado.
 * @author Sofis Solutions
 */
public class HibernateJpaDAOImp<T, ID extends Serializable> extends JpaDAOImp<T, ID> {

    private static EntityManagerFactory emFactoryCustom = null;
    
    
    
    public HibernateJpaDAOImp(){
        super();
    }

    public HibernateJpaDAOImp(EntityManager em) {
        super(em);
        if (emFactoryCustom != null) {
            this.setEm(emFactoryCustom.createEntityManager());
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

            if (e.getCause() != null && e.getCause().getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cons = (ConstraintViolationException) e.getCause().getCause();
                String causaEnBase = cons.getCause().getMessage();

                Pattern patronFK = Pattern.compile("FK[a-zA-Z_0-9]*");
                Matcher m = patronFK.matcher(causaEnBase);
                m.find();
                String fk = m.group();

                DAOConstraintViolationException ex = new DAOConstraintViolationException();
                ex.setForeignKey(fk);
                throw ex;
            }
        }
    }

    public T findEntityByDate(Class<T> cl, String idPropertyName, ID id, Date date) throws DAOGeneralException {
        AuditReader a = AuditReaderFactory.get(super.getEm());
        //Dada la fecha obtenemos la revision de esa fecha de la base de datos
        Number number = a.getRevisionNumberForDate(date);
        //Obtenemos las revisiones de la entidad
        List<Number> revisions = a.getRevisions(cl, id);
        T toReturn = a.find(cl, id, number);
        if (toReturn == null) {
            //buscamos la revision mas cercana a la number
            Number entityRev = 0;

            for (Number rev : revisions) {

                if (rev.longValue() == number.longValue()) {
                    entityRev = rev;
                    break;
                } else if (rev.longValue() < number.longValue()) {
                    entityRev = rev;
                    continue;
                } else if (rev.longValue() > number.longValue()) {
                    break;
                }
            }
            if (entityRev.longValue() > 0L) {
                toReturn = a.find(cl, id, entityRev);
                return toReturn;
            } else {
                return null;
            }
        } else {
            return toReturn;
        }
    }

    public List<Number> findEntityRevisions(Class<T> cl, ID id) throws DAOGeneralException {
        AuditReader a = AuditReaderFactory.get(super.getEm());
        List<Number> revision = a.getRevisions(cl, id);
        return revision;
    }

    public T findEntityByRevision(Class<T> cl, ID id, Number revision) throws DAOGeneralException {
        AuditReader a = AuditReaderFactory.get(super.getEm());
        return a.find(cl, id, revision);
    }

    private void setAudited(T entity, String userName, String description) {
        Object a = entity.getClass().getAnnotation(Audited.class);
        if (a != null) {
            RevisionEntityListener.description.set(description);
            RevisionEntityListener.userName.set(userName);
        }
    }

    public Object persist(Object o, String userName, String desc) throws DAOGeneralException {
        return super.create((T) o);
    }

}

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import sv.gob.mined.siges.utils.ReflectionUtils;

public class CodigueraDAO<T> extends HibernateJpaDAOImp<T, Long> implements Serializable {

    private EntityManager em;
    private final Class<T> clazz;

    public CodigueraDAO(EntityManager em, Class clase) {
        super(em);
        this.em = em;
        clazz = clase;
    }

    public void lock(int id) {
        Object lock = em.find(clazz, id);
        em.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }


    public T guardar(T elemento) throws DAOGeneralException {
        try {
            Field campoId = ReflectionUtils.obtenerCampoAnotado(clazz, Id.class);
            Object value = campoId.get(elemento);
            if (value == null) {
                elemento = super.create(elemento, null);
            } else {
                elemento = super.update(elemento, null);
            }
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
        return elemento;
    }

    public void eliminar(T elemento) throws DAOGeneralException {
        try {
            Field campoId = ReflectionUtils.obtenerCampoAnotado(clazz, Id.class);
            Object value = campoId.get(elemento);
            em.remove(em.find(clazz, value));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Boolean objetoExistePorId(Long id) throws DAOGeneralException {
        try {
            String campoId = ReflectionUtils.obtenerCampoAnotado(clazz, Id.class).getName();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoId, id);
            Long count = this.countByCriteria(clazz, criterio, false, null);
            if (count > 0L) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public T obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(clazz, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public void eliminarPorId(Long id) throws DAOGeneralException {
        try {
            em.remove(em.find(clazz, id));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public List<T> obtenerPorCampo(Object dato, String campo) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = new ArrayList();

            if (dato != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, campo, dato);
                criterios.add(criterio);
            }

            String[] orderBy = null;
            boolean[] asc = null;

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(clazz, criterio, orderBy, asc, null, null, true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}

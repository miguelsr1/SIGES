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
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

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

    private List<CriteriaTO> generarCriteria(FiltroCodiguera filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoNombre.class);
        String campoDescripcion = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoDescripcion.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, SofisStringUtils.normalizarParaBusqueda(filtro.getCodigo()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getNombre()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDescripcion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoDescripcion, SofisStringUtils.normalizarParaBusqueda(filtro.getDescripcion()));
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<T> obtenerPorFiltro(FiltroCodiguera filtro) throws DAOGeneralException {
        try {
            
            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);    
            }
            return this.findEntityByCriteria(clazz, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(clazz, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
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
            em.createQuery("SELECT b." + campoId + " FROM " + clazz.getSimpleName() + " b WHERE b." + campoId + " =:id").setParameter("id", id).getSingleResult();
            return Boolean.TRUE;
        } catch (NoResultException nr) {
            return Boolean.FALSE;
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

}

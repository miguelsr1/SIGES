/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.DataSecurityException;
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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 * DAO genérico para los catálogos
 *
 * @author jgiron
 * @param <T>
 */
public class CodigueraDAO<T> extends HibernateJpaDAOImp<T, Long> implements Serializable {

    private EntityManager em;
    private final Class<T> clazz;
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @param clase
     * @throws Exception
     */
    public CodigueraDAO(EntityManager em, Class clase) throws Exception {
        super(em);
        this.em = em;
        this.clazz = clase;
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    /**
     * Lockear una entidad
     *
     * @param id
     */
    public void lock(int id) {
        Object lock = em.find(clazz, id);
        em.lock(lock, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Genera un Criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroCodiguera filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoNombre.class);
        String campoDescripcion = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoDescripcion.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(clazz, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getCodigo());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getNombre()));
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
        
        if (!StringUtils.isBlank(filtro.getCodigoONombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getCodigoONombre());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoNombre, filtro.getCodigoONombre());
            
            criterios.add(CriteriaTOUtils.createORTO(criterio,criterio1));
        }

        return criterios;
    }

    /**
     * Obtiene todos los elementos que satisfacen un determinado filtro según un
     * contexto.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<T> obtenerPorFiltro(FiltroCodiguera filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(clazz, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve el total de elementos según un determinado filtro en un contexto
     * de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return super.countByCriteria(clazz, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Guarda un elemento segùn un determinado contexto de seguridad
     *
     * @param elemento
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     * @throws BusinessException
     */
    public T guardar(T elemento, String securityOperation) throws DAOGeneralException, BusinessException {
        try {
            securityOperation = null;
            Field campoId = ReflectionUtils.obtenerCampoAnotado(clazz, Id.class);
            Object value = campoId.get(elemento);
            if (value == null) {
                elemento = super.create(elemento, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
            } else {
                elemento = super.update(elemento, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
            }
        } catch (DataSecurityException se) {
            BusinessException ge = new BusinessException();
            ge.addError(Errores.ERROR_SEGURIDAD);
            throw ge;
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
        return elemento;
    }

    /**
     * Elimina un elemento según un contexto de seguridad
     *
     * @param elemento
     * @param securityOperation
     * @throws DAOGeneralException
     * @throws BusinessException
     */
    public void eliminar(T elemento, String securityOperation) throws DAOGeneralException, BusinessException {
        try {
            super.delete(elemento, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (DataSecurityException se) {
            BusinessException ge = new BusinessException();
            ge.addError(Errores.ERROR_SEGURIDAD);
            throw ge;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Determina si existe un objeto según su id y un contexto de seguridad
     *
     * @param id
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Boolean objetoExistePorId(Long id, String securityOperation) throws DAOGeneralException {
        try {
            String campoId = ReflectionUtils.obtenerCampoAnotado(clazz, Id.class).getName();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoId, id);
            Long count = this.countByCriteria(clazz, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
            if (count > 0L) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve el elemento que tiene un ID dado en un contexto de seguridad y
     * que satisfacen determinada propiedades
     *
     * @param id
     * @param securityOperation
     * @param properties
     * @return
     * @throws DAOGeneralException
     * @throws BusinessException
     */
    public T obtenerPorId(Long id, String securityOperation, String... properties) throws DAOGeneralException, BusinessException {
        try {
            return super.findById(clazz, id, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve un elemento según su id en un contexto de seguridad.
     *
     * @param id
     * @param securityOperation
     * @throws DAOGeneralException
     */
    public void eliminarPorId(Long id, String securityOperation) throws DAOGeneralException {
        try {
            this.eliminar(this.obtenerPorId(id, securityOperation), securityOperation);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}

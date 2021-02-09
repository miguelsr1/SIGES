/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.EntityManagementBeanRemote;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.web.utils.EJBUtils;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;

/**
 * Esta clase implementa el acceso a datos en forma genérica
 *
 * @author Sofis Solutions
 */
@Named
public class EntityManagementDelegate implements Serializable {

    private EntityManagementBeanRemote c;

    public EntityManagementDelegate() throws TechnicalException {
        c = EJBUtils.getEntityManagement();
    }

    /**
     * Este método elimina un objeto según id.
     *
     * @param entity
     * @param id
     * @param userName
     * @param desc
     * @return
     * @throws GeneralException
     */
    public Object delete(Object entity, int id, String userName, String desc) throws GeneralException {

        return c.delete(entity, id, userName, desc);
    }

    /**
     * Este método devuelve un objeto de un clase con un determinado id de tipo
     * Integer
     *
     * @param className
     * @param id
     * @return
     * @throws GeneralException
     */
    public Object getEntityById(String className, Integer id) throws GeneralException {
        return c.getEntityById(className, id);
    }

    /**
     * Este método devuelve un objeto de una clase con un determinado id de tipo
     * Long
     *
     * @param className
     * @param id
     * @return
     * @throws GeneralException
     */
    public Object getEntityById(String className, Long id) throws GeneralException {
        return c.getEntityById(className, id);
    }

    /**
     * Este método devuelve todas las entidades de una clase.
     *
     * @param className
     * @return
     * @throws GeneralException
     */
    public List getEntities(String className) throws GeneralException {
        return c.getEntities(className);
    }

    /**
     * Este método devuelve todas las entidades de una clase, ordenadas por uno
     * de los campos.
     *
     * @param className
     * @param orderBy
     * @return
     * @throws GeneralException
     */
    public List getEntities(String className, String orderBy) throws GeneralException {
        return c.getEntities(className, orderBy);
    }

    
    /**
     * Este método devuelve todas las entidades de una clase, ordenadas por uno
     * de los campos.
     *
     * @param className
     * @param orderBy
     * @return
     * @throws GeneralException
     */
    public List getEntitiesUpper(String className, String orderBy) throws GeneralException {
        return c.getEntitiesUpper(className, orderBy);
    }
    
    
    /**
     * Este método devuelve referencias a entidades de un tipo, según un
     * criterio o filtro.
     *
     * @param className clase a realizar la búsqueda
     * @param criteria criterio o filtro
     * @param desde desde qué número de registro devolver en la lista resultante
     * @param maximo cantidad máxima de elementos a devolver
     * @param propiedadesNombre nombre de las propiedades a devolver
     * @param orderBy campo de ordenación
     * @param ascending si el criterio de ordenación es ascendente o descendente
     * @return
     * @throws GeneralException
     */
    public List<EntityReference<Integer>> getEntitiesReferenceByCriteria(String className, CriteriaTO criteria, Integer desde, Integer maximo, String[] propiedadesNombre, String[] orderBy, boolean[] ascending) throws GeneralException {
        return c.getEntitiesReferenceByCriteria(className, criteria, desde, maximo, propiedadesNombre, orderBy, ascending);
    }

    //termina llamando al entity reference
    public List getEntitiesByCriteria(String className, CriteriaTO criteria, Integer desde, Integer maximo, String[] propiedadesNombre, String[] orderBy, boolean[] ascending) throws GeneralException {
        return c.getEntitiesByCriteria(className, criteria, desde, maximo, propiedadesNombre, orderBy, ascending);
    }

    /**
     * Este método devuelve un conjunto de entidades por criterio
     *
     * @param className nombre de la clase
     * @param criteria criterio o filtro
     * @param startPosition elemento inicial desde el que se debe volver
     * resultado
     * @param maxResult cantidad máxima de resultados
     * @param orderBy campo de ordenación
     * @param ascending criterio de ordenación
     * @return
     * @throws GeneralException
     */
    public List<Object> getEntitiesByCriteria(String className, CriteriaTO criteria, Integer startPosition, Integer maxResult, String[] orderBy, boolean[] ascending) throws GeneralException {
        return c.getEntitiesByCriteria(className, criteria, startPosition, maxResult, orderBy, ascending);

    }

    /**
     * Este método permite guardar un objeto al que se le asignarán los campos
     * userName y origen
     *
     * @param o
     * @param userName
     * @param desc
     * @return
     * @throws GeneralException
     */
    public Object saveOrUpdate(Object o, String userName, String desc) throws GeneralException {
        return c.saveOrUpdate(o, userName, desc);
    }

    /**
     * Este método permite guardar un objeto
     *
     * @param o
     * @return
     * @throws GeneralException
     */
    public Object saveOrUpdate(Object o) throws GeneralException {
        return c.saveOrUpdate(o);
    }

    /**
     * Este método devuelve la cantidad de elementos que satisfacen un criteria.
     *
     * @param className
     * @param desde
     * @param maximo
     * @param criteria
     * @return
     * @throws GeneralException
     */
    public Long getCountsByCriteria(String className, Integer desde,
            Integer maximo, CriteriaTO criteria) throws GeneralException {
        Long cant = -1L;
        Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
        try {
            cant = c.getCountsByCriteria(className, desde, maximo, criteria);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getMessage(), t);
            throw t;
        }
        return cant;
    }

    /**
     * Este método permite buscar un elemento según una propiedad-valor
     *
     * @param className
     * @param propiedad
     * @param valor
     * @return
     */
    public List findByOneProperty(String className, String propiedad, Object valor) {
        return c.findByOneProperty(className, propiedad, valor);
    }

    /**
     * Este método devuelve una lista de elementos que cumplen con una
     * propiedad-valor ordenados por un campo
     *
     * @param className
     * @param propiedad
     * @param valor
     * @param orderBy
     * @return
     */
    public List findByOneProperty(String className, String propiedad, Object valor, String orderBy) {
        return c.findByOneProperty(className, propiedad, valor, orderBy);
    }

    /**
     * Este método devuelve una lista de elementos según propiedad-valor
     * ordenados por un conjunto de campos.
     *
     * @param className
     * @param propiedad
     * @param valor
     * @param orderBy
     * @param ascending
     * @return
     */
    public List findByOneProperty(String className, String propiedad, Object valor, String[] orderBy, boolean[] ascending) {
        return c.findByOneProperty(className, propiedad, valor, orderBy, ascending);
    }

    /**
     * Este método permite eliminar un objeto según su id.
     *
     * @param classe
     * @param id
     * @throws GeneralException
     */
    public void delete(Class classe, int id) throws GeneralException {
        c.delete(classe, id);
    }

    public void delete(Class classe, Long id) throws GeneralException {
        c.delete(classe, id);
    }

    public List findEntityByCriteria(Class entityClass, CriteriaTO criteria, String[] orderBy, boolean[] ascending, Long startPosition, Long maxResult) throws GeneralException {
        return c.findEntityByCriteria(entityClass, criteria, orderBy, ascending, startPosition, maxResult);

    }

    /**
     * Este método permite obtener un objeto en una determinada versión del
     * histórico.
     *
     * @param <T>
     * @param entityClass
     * @param id
     * @return
     */
    public <T> List<T> obtenerHistorico(Class<T> entityClass, int id) {
        return c.obtenerHistorico(entityClass, id);
    }


}

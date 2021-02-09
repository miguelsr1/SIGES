/*
 * 
 * 
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Sofis Solutions
 */
@Remote
public interface EntityManagementBeanRemote {

    public Object delete(Object entity, int id, String userName, String desc) throws GeneralException;

    public Object getEntityById(String className, Integer id) throws GeneralException;

    public Object getEntityById(String className, Long id) throws GeneralException;

    public List getEntities(String className) throws GeneralException;
    
    public List getEntities(String className, String orderBy) throws GeneralException;
    
    public List getEntitiesUpper(String className, String orderBy) throws GeneralException;
    
     public List<EntityReference<Integer>> getEntitiesReferenceByCriteria(String className, CriteriaTO criteria, Integer desde, Integer maximo, String[] propiedadesNombre, String[] orderBy, boolean[] ascending) throws GeneralException;

    public List getEntitiesByCriteria(String className, CriteriaTO criteria, Integer desde, Integer maximo, String[] propiedadesNombre, String[] orderBy, boolean[] ascending) throws GeneralException;

    public List<Object> getEntitiesByCriteria(String className, CriteriaTO criteria, Integer startPosition, Integer maxResult, String[] orderBy, boolean[] ascending) throws GeneralException;

    public Long getCountsByCriteria(String className, Integer desde,
            Integer maximo, CriteriaTO criteria) throws GeneralException;

    public Object saveOrUpdate(Object o, String userName, String desc) throws GeneralException;

    public Object saveOrUpdate(Object o) throws GeneralException;

    public List findByOneProperty(String className, String propiedad, Object valor) throws GeneralException;

    public List findByOneProperty(String className, String propiedad, Object valor,  String orderBy) throws GeneralException ;
    
    public List findByOneProperty(String className, String propiedad, Object valor,  String[] orderBy, boolean[] ascending) throws GeneralException;
    
    public void delete(Class classe, int id) throws GeneralException;
    
    public void delete(Class classe, Long id) throws GeneralException;
    
    public List findEntityByCriteria(Class entityClass, CriteriaTO criteria, String[] orderBy, boolean[] ascending, Long startPosition, Long maxResult) throws GeneralException ;
    
    public <T> List<T> obtenerHistorico(Class<T> entityClass, int id); 
         
     
    
}

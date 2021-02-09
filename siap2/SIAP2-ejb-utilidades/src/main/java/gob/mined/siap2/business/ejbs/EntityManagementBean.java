/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import com.mined.siap2.audit.AuditReader;
import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.business.utils.Utils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Clase genérica para la gestión de entidades.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "EntityManagementBean")
@Interceptors({LoggedInterceptor.class})
public class EntityManagementBean implements EntityManagementBeanRemote {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private DatosUsuario usrBean;

    @Inject
    @JPADAO
    private GeneralDAO dao;

    /**
     * Almacena por primera vez o actualiza los datos de un objeto.
     *
     * @param o es el objeto a actualizar o grabar
     * @return el objeto o actualizado.
     * @throws GeneralException
     */
    @Override
    public Object saveOrUpdate(Object o) throws GeneralException {
        try {
            Object toReturn = dao.update(o, usrBean.getCodigoUsuario(), usrBean.getCodigoUsuario());
            return toReturn;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    @Override
    public void delete(Class classe, int id)
            throws GeneralException {
        try {
            Object objBorrar = getEntityById(classe.getName(), id);
            dao.delete(objBorrar, usrBean.getCodigoUsuario(), usrBean.getCodigoUsuario());

        } catch (DAOGeneralException ex) {
            // TODO: Agregar mensaje de excepcion
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Elimina el objeto de la clase classe con el id indicado
     *
     * @param classe es la clase del objeto a eliminar
     * @param id del objeto a eliminar
     * @throws GeneralException
     */
    @Override
    public void delete(Class classe, Long id)
            throws GeneralException {
        try {
            Object objBorrar = getEntityById(classe.getName(), id);
            dao.delete(objBorrar, usrBean.getCodigoUsuario(), usrBean.getCodigoUsuario());

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método permite guardar o actualizar un objeto con los datos de
     * auditoría userName y desc como origen
     *
     * @param o es el objeto a guardar
     * @param userName código del usuario que actualiza
     * @param desc origen de la aplicación
     * @return el objeto actualizado
     * @throws GeneralException
     */
    @Override
    public Object saveOrUpdate(Object o, String userName, String desc)
            throws GeneralException {
        try {
            Object toReturn = dao.update(o, userName, desc);
            return toReturn;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método elimina el objeto con el id de la clase del objeto entity
     * registra la eliminación con el userName y desc como origen
     *
     * @param entity a eliminar
     * @param id de la entidad a eliminar
     * @param userName que realiza la eliminación
     * @param desc origen
     * @return
     * @throws GeneralException
     */
    @Override
    public Object delete(Object entity, int id, String userName, String desc)
            throws GeneralException {
        try {
            Object objBorrar = getEntityById(entity.getClass().getName(), id);
            dao.delete(objBorrar, userName, desc);

            return entity;

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método devuelve el objeto de la clase className con el id indicado.
     *
     * @param className clase del objeto a obtener
     * @param id del objeto.
     * @return
     * @throws GeneralException
     */
    @Override
    public Object getEntityById(String className, Integer id)
            throws GeneralException {
        try {
            Class class_;
            try {
                class_ = Class.forName(className);

            } catch (ClassNotFoundException ex) {
                throw new DAOGeneralException(ex);
            }
            return dao.find(class_, id);

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método permite obtener la entidad con id de tipo long.
     *
     * @param className
     * @param id
     * @return
     * @throws GeneralException
     */
    @Override
    public Object getEntityById(String className, Long id)
            throws GeneralException {
        try {
            Class class_;
            try {
                class_ = Class.forName(className);

            } catch (ClassNotFoundException ex) {
                throw new DAOGeneralException(ex);
            }
            return dao.find(class_, id);

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método permite obtener todas las entidades de la clase className
     *
     * @param className nombre de la clase
     * @return
     * @throws GeneralException
     */
    @Override
    public List getEntities(String className) throws GeneralException {
        try {
            Class class_;
            try {
                class_ = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new DAOGeneralException(ex);
            }
            return dao.findAll(class_);

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método devuelve todas las entidades de la clase className ordenadas
     * por orderBy
     *
     * @param className, nombre de la clase
     * @param orderBy, field por el que se ordenará el resultado
     * @return
     * @throws GeneralException
     */
    @Override
    public List getEntities(String className, String orderBy) throws GeneralException {
        try {
            Class class_;
            try {
                class_ = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new DAOGeneralException(ex);
            }
            List l = dao.findAll(class_, orderBy);
            l.isEmpty();
            return l;

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    
    
    /**
     * Este método devuelve todas las entidades de la clase className ordenadas
     * por orderBy
     *
     * @param className, nombre de la clase
     * @param orderBy, field por el que se ordenará el resultado
     * @return
     * @throws GeneralException
     */
    @Override
    public List getEntitiesUpper(String className, String orderBy) throws GeneralException {
        try {
            Class class_;
            try {
                class_ = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new DAOGeneralException(ex);
            }
            List l = dao.findAllUpper(class_, orderBy);
            l.isEmpty();
            return l;

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }
    
    
    
    /**
     * Este método devuelve una lista de referencias a entidades según criterios
     *
     * @param className nombre de la clase
     * @param criteria criterios para el filtro
     * @param desde a partir de que registro del resultado de la búsqueda
     * @param maximo hasta qué registro del resultado de la búsqueda
     * @param propiedadesNombre nombres de los campos a devolver
     * @param orderBy campo por el que deberá ordenarse el resultado
     * @param ascending si el criterio de ordenación es ascendente o descendente
     * @return
     * @throws GeneralException
     */
    @Override
    public List<EntityReference<Integer>> getEntitiesReferenceByCriteria(
            String className, CriteriaTO criteria, Integer desde,
            Integer maximo, String[] propiedadesNombre, String[] orderBy,
            boolean[] ascending) throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            Long desdeL = Utils.toLong(desde);
            Long hastaL = Utils.toLong(maximo);

            return dao.findEntityReferenceByCriteria(class_, criteria, orderBy,
                    ascending, desdeL, hastaL, propiedadesNombre);

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método devuelve una lista de entidades según búsqueda.
     *
     * @param className nombre de la clase
     * @param criteria criterio de búsqueda
     * @param desde a partir de qué registro se devuelve el resultado
     * @param maximo registro hasta el que se devuelve el resultado
     * @param propiedadesNombre campos a incluir en el resultado
     * @param orderBy campo de ordenación
     * @param ascending si true es ascendente.
     * @return
     * @throws GeneralException
     */
    @Override
    public List getEntitiesByCriteria(String className, CriteriaTO criteria,
            Integer desde, Integer maximo, String[] propiedadesNombre,
            String[] orderBy, boolean[] ascending)
            throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            Long desdeL = Utils.toLong(desde);
            Long hastaL = Utils.toLong(maximo);
            List<EntityReference<Integer>> lista = dao.findEntityByCriteria(class_, criteria, orderBy,
                    ascending, desdeL, hastaL, propiedadesNombre);

            return lista;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());

            throw ge;
        }
    }

    /**
     * Este método realiza la búsqueda de entidades de una clase y devuelve una
     * cantidad fija.
     *
     * @param className nombre de la clase
     * @param criteria criterio de búsqueda
     * @param startPosition posición inicial a partir de la cual se devuelven
     * resultados
     * @param maxResult cantidad máxima de datos a devolver
     * @param orderBy criterio de ordenación (campo)
     * @param ascending si true es ascendente
     * @return
     * @throws GeneralException
     */
    @Override
    public List<Object> getEntitiesByCriteria(String className, CriteriaTO criteria,
            Integer startPosition, Integer maxResult, String[] orderBy,
            boolean[] ascending) throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            Long desdeL = Utils.toLong(startPosition);
            Long hastaL = Utils.toLong(maxResult);
            return dao.findEntityByCriteria(class_, criteria, orderBy, ascending, desdeL, hastaL);
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve la cantidad de elementos en el resultado de una
     * búsqueda
     *
     * @param className - nombre de la clase
     * @param desde - a partir de cuál debe empezar a contarse
     * @param maximo - cantidad máxima
     * @param criteria - criterio de filtro
     * @return
     * @throws GeneralException
     */
    @Override
    public Long getCountsByCriteria(String className, Integer desde,
            Integer maximo, CriteriaTO criteria) throws GeneralException {
        try {

            Class class_ = Utils.toClass(className);
            Long desdeL = Utils.toLong(desde);
            Long hastaL = Utils.toLong(maximo);
            Long l = dao.countByCriteria(class_, criteria, desdeL, hastaL);

            return l;

        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite buscar objetos de una clase según una propiedad de él
     *
     * @param className, nombre de la clase
     * @param propiedad, propiedad por la que se desea buscar
     * @param valor, valor que se requiere en la propiedad indicada.
     * @return
     * @throws GeneralException
     */
    public List findByOneProperty(String className, String propiedad, Object valor) throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            List l = dao.findByOneProperty(class_, propiedad, valor);
            l.isEmpty();
            return l;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los datos de una clase que tienen un valor en
     * una propiedad, ordenados por un campo
     *
     * @param className, nombre de la clase
     * @param propiedad, nombre de la propiedad
     * @param valor, valor en la propiedad
     * @param orderBy, campo de ordenación
     * @return
     * @throws GeneralException
     */
    public List findByOneProperty(String className, String propiedad, Object valor, String orderBy) throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            List l = dao.findByOneProperty(class_, propiedad, valor, orderBy);
            l.isEmpty();
            return l;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los objetos de una clase con un valor en una
     * propiedad ordenados por múltiples campos.
     *
     * @param className, nombre de la clase.
     * @param propiedad, nombre de la propiedad.
     * @param valor, valor en la propiedad.
     * @param orderBy, campos de ordenación.
     * @param ascending, criterios ascendente o descendente para cada campo.
     * Debe coincidir el largo de ascending con el de orderBy.
     * @return
     * @throws GeneralException
     */
    public List findByOneProperty(String className, String propiedad, Object valor, String[] orderBy, boolean[] ascending) throws GeneralException {
        try {
            Class class_ = Utils.toClass(className);
            List l = dao.findByOneProperty(class_, propiedad, valor, orderBy, ascending);
            l.isEmpty();
            return l;

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los objetos de una clase según un cierto criterio, ordenados y desde una cierta posicion
     * Paginado por base.
     * 
     * @param entityClass, clase de la entidad.
     * @param criteria, criterio de búsqueda.
     * @param orderBy, ordenación
     * @param ascending, ascendente o descendente
     * @param startPosition, a partir de qué posición.
     * @param maxResult, cantidad máxima de resultados.
     * @return
     * @throws GeneralException 
     */
    public List findEntityByCriteria(Class entityClass, CriteriaTO criteria, String[] orderBy, boolean[] ascending, Long startPosition, Long maxResult) throws GeneralException {
        try {
            return dao.findEntityByCriteria(entityClass, criteria, orderBy, ascending, startPosition, maxResult);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Datos históricos de un objeto.
     * @param <T>
     * @param entityClass
     * @param id
     * @return 
     */
    @Override
    public <T> List<T> obtenerHistorico(Class<T> entityClass, int id) {
        return AuditReader.read(dao.getEntityManager(), entityClass, id);
    }

   
}

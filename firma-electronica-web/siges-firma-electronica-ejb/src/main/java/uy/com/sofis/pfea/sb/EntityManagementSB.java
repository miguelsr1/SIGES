/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb;

import com.sofis.persistence.dao.GeneralDAO;
import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.reference.EntityReference;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.utils.UtilVarios;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import uy.com.sofis.pfea.anotaciones.JPADAO;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.exceptions.GeneralException;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@LocalBean
public class EntityManagementSB {

    private static final Logger logger = Logger.getLogger(EntityManagementSB.class.getName());

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
    public Object saveOrUpdate(Object o) throws GeneralException {
        try {
            if (o == null) {
                return null;
            }
            return dao.update(o, "", "");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.setEx(ex);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public void delete(Class classe, int id)
            throws GeneralException {
        try {
            Object objBorrar = getEntityById(classe.getName(), id);
            dao.delete(objBorrar, "", "");

        } catch (Exception ex) {
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
    public void delete(Class classe, Long id)
            throws GeneralException {
        try {
            Object objBorrar = getEntityById(classe.getName(), id);
            dao.delete(objBorrar, "", "");

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
     * auditoria userName y desc como origen
     *
     * @param o es el objeto a guardar
     * @param userName código del usuario que actualiza
     * @param desc origen de la aplicacion
     * @return el objeto actualizado
     * @throws GeneralException
     */
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
     * @param id de la entidad a elimianr
     * @param userName que realiza la eliminacion
     * @param desc origen
     * @return
     * @throws GeneralException
     */
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
     * Este método devuelve una lista de referencias a entidades según criteria
     *
     * @param className nombre de la clase
     * @param criteria criteria para el filtro
     * @param desde a partir de que registro del resultado de la busqueda
     * @param maximo hasta qué registro del resultado de la busqueda
     * @param propiedadesNombre nombres de los campos a devolver
     * @param orderBy campo por el que deberá ordenarse el resultado
     * @param ascending si el criterio de ordenación es ascendente o descendente
     * @return
     * @throws GeneralException
     */
    public List<EntityReference<Integer>> getEntitiesReferenceByCriteria(
            String className, CriteriaTO criteria, Integer desde,
            Integer maximo, String[] propiedadesNombre, String[] orderBy,
            boolean[] ascending) throws GeneralException {
        try {
            Class class_ = UtilVarios.toClass(className);
            Long desdeL = UtilVarios.toLong(desde);
            Long hastaL = UtilVarios.toLong(maximo);

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
     * @param criteria criteria de búsqueda
     * @param desde a partir de qué registro se devuelve el resultado
     * @param maximo registro hasta el que se devuelve el resultado
     * @param propiedadesNombre campos a incluir en el resultado
     * @param orderBy campo de ordenacion
     * @param ascending si true es ascendente.
     * @return
     * @throws GeneralException
     */
    public List getEntitiesByCriteria(String className, CriteriaTO criteria,
            Integer desde, Integer maximo, String[] propiedadesNombre,
            String[] orderBy, boolean[] ascending)
            throws GeneralException {
        try {
            Class class_ = UtilVarios.toClass(className);
            Long desdeL = UtilVarios.toLong(desde);
            Long hastaL = UtilVarios.toLong(maximo);
            List<EntityReference<Integer>> lista = dao.findEntityByCriteria(class_, criteria, orderBy,
                    ascending, desdeL, hastaL, propiedadesNombre);

            return lista;
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
    public List<Object> getEntitiesByCriteria(String className, CriteriaTO criteria,
            Integer startPosition, Integer maxResult, String[] orderBy,
            boolean[] ascending) throws GeneralException {
        try {
            //HibernateJpaDAOImp dao = new HibernateJpaDAOImp(em);
            Class class_ = UtilVarios.toClass(className);
            Long desdeL = UtilVarios.toLong(startPosition);
            Long hastaL = UtilVarios.toLong(maxResult);
            return dao.findEntityByCriteria(class_, criteria, orderBy, ascending, desdeL, hastaL);
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
     * Este método devuelve la cantidad de elementos en el resutlado de una
     * búsqueda
     *
     * @param className - nombre de la clase
     * @param desde - a partir de cual debe empezar a contarse
     * @param maximo - cantidad máxima
     * @param criteria - criterio de filtro
     * @return
     * @throws GeneralException
     */
    public Long getCountsByCriteria(String className, Integer desde,
            Integer maximo, CriteriaTO criteria) throws GeneralException {
        try {

            //HibernateJpaDAOImp dao = new HibernateJpaDAOImp(em);
            Class class_ = UtilVarios.toClass(className);
            Long desdeL = UtilVarios.toLong(desde);
            Long hastaL = UtilVarios.toLong(maximo);
            Long l = dao.countByCriteria(class_, criteria, desdeL, hastaL);

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
            Class class_ = Class.forName(className);
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
     * @param orderBy, campo de ordenacion
     * @return
     * @throws GeneralException
     */
    public List findByOneProperty(String className, String propiedad, Object valor, String orderBy) throws GeneralException {
        try {
            //HibernateJpaDAOImp dao = new HibernateJpaDAOImp(em);
            Class class_ = Class.forName(className);
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
     * Este método permite obtener los objetos de una clase según un cierto
     * criterio, ordenados y desde una cierta posicion Paginado por base.
     *
     * @param entityClass, clase de la entidad.
     * @param criteria, criterio de búsqueda.
     * @param orderBy, ordenación
     * @param ascending, ascendente o descendente
     * @param startPosition, a partir de qué posicion.
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
}

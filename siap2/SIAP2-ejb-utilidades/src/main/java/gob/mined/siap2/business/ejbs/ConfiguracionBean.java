/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.business.validations.ConfiguracionValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Session Bean que incluye los métodos para la gestión de los datos de configuración.
 * @author Sofis Solutions
 */
@Stateless(name = "ConfiguracionBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class ConfiguracionBean {
 
    @Inject
    private DatosUsuario du;
    @Inject @JPADAO
    private GeneralDAO generalDAO;
    
    private static final Logger logger =  Logger.getLogger(ConfiguracionBean.class.getName());

    /**
     * Este método guarda un elemento de tipo Configuración. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public Configuracion guardar(Configuracion cnf) throws GeneralException {
        logger.log(Level.SEVERE, "guardar");
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (ConfiguracionValidacion.validar(cnf)) {
                if (cnf.getId() == null) {
                    cnf = (Configuracion) generalDAO.create(cnf, du.getCodigoUsuario(), du.getOrigen());
                } else {
                    cnf = (Configuracion) generalDAO.update(cnf, du.getCodigoUsuario(), du.getOrigen());
                }
            }
            return cnf;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve el elemento configuración por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public Configuracion obtenerCnfPorId(Integer id) throws GeneralException {
        logger.log(Level.INFO, "obtenerCnfPorId");
        try {
            return (Configuracion) generalDAO.findById(Configuracion.class, id);
        } catch (DAOGeneralException ex) {
           logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve el elemento de configuración según el código Si no hay ningún
     * elemento con ese código devuelve null
     *
     * @param codigo
     * @return
     * @throws GeneralException
     */
    public Configuracion obtenerCnfPorCodigo(String codigo) throws GeneralException {
        logger.log(Level.INFO, "obtenerCnfPorCodigo");
        try {
            List<Configuracion> resultado = generalDAO.findByOneProperty(Configuracion.class, "cnfCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve todos los elementos de tipo configuración
     *
     * @return
     * @throws GeneralException
     */
    public List<Configuracion> obtenerTodos() throws GeneralException {
        logger.log(Level.INFO, "obtenerTodos");
        try {
            return generalDAO.findAll(Configuracion.class);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio ingresado
     *
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException
     */
    public List<Configuracion> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorCriteria");
        try {
            return generalDAO.findEntityByCriteria(Configuracion.class, cto, orderBy, ascending, startPosition, cantidad);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            
        }
        return null;
    }
}

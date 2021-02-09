/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.business.validations.IdiomaValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.CodigueraDAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Idioma;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase corresponde a la gestión de datos de idioma.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "IdiomaBean")
@LocalBean
public class IdiomaBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private CodigueraDAO codigueraDAO;

    public void init() {

    }

    /**
     * Este método guarda un elemento de tipo Idioma. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public Idioma guardar(Idioma tho) throws GeneralException {
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (IdiomaValidacion.validar(tho)) {
                if (tho.getIdiId() == null) {
                    tho = (Idioma) generalDAO.create(tho);
                } else {
                    tho = (Idioma) generalDAO.update(tho);
                }
            }
            return tho;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (DAOGeneralException be) {
            //Las excepciones generales de DAO se procesan aquí­ JM 14-05-2014
            BusinessException ge = new BusinessException();
            throw ge;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage(), ex);
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
    public Idioma obtenerIdiomaPorId(Integer id) throws GeneralException {
        try {
            return (Idioma) generalDAO.findById(Idioma.class, id);
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
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
    public Idioma obtenerIdiomaPorCodigo(String codigo) throws GeneralException {
        try {

            List<Idioma> resultado = generalDAO.findByOneProperty(Idioma.class, "idiCodigo", codigo);

            if (resultado.isEmpty()) {
                init();
                resultado = generalDAO.findByOneProperty(Idioma.class, "idiCodigo", codigo);
            }

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
              logger.log(Level.SEVERE, ex.getMessage(), ex);
             
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        } catch (Exception ex) {
             logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Devuelve todos los elementos de tipo configuración
     *
     * @return
     * @throws GeneralException
     */
    public List<Idioma> obtenerTodos() throws GeneralException {
        try {
            return generalDAO.findAll(Idioma.class);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Este método retorna la lista de idiomas habilitados
     * 
     * @return 
     */
    public List<Idioma> obtenerHabilitados() {
        try {
            List<Idioma> list = codigueraDAO.obtenerHabilitados(Idioma.class, "idiHabilitado", "idiDescripcion");
            return list;
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Este método elimina un idioma
     * 
     * @param id
     * @throws GeneralException 
     */
    public void eliminarIdioma(Integer id) throws GeneralException {
        try {
            Idioma aux = (Idioma) generalDAO.findById(Idioma.class, id);
            if (aux != null) {
                generalDAO.delete(aux);
            }
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Este método retorna el idioma Español
     * 
     * @return
     * @throws GeneralException 
     */
    public Idioma obtenerIdiomaEspaniol() throws GeneralException {
        try {
            return obtenerIdiomaPorCodigo(ConstantesEstandares.CODIGO_IDIOMA_ESPANIOL);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            throw e;
        }
    }

}

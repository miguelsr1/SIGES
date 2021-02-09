/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.audit.AuditReader;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.business.validations.ProgramacionTrimestralValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.ProgramacionTrimestralDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Esta clase incluye los métodos de servicio correspondientes a POAs.
 * @author Sofis Solutions
 */
@Stateless(name = "ProgramacionTrimestralBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class ProgramacionTrimestralBean {
    @Inject
    private DatosUsuario du;
    @Inject
    @JPADAO
    private ProgramacionTrimestralDAO programacionTrimestralDAO;
    @Inject
    private ProgramacionTrimestralValidacion progValidacion;

    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    private static final Logger logger = Logger.getLogger(ProgramacionTrimestralBean.class.getName());

    /**
     * Este método guarda un elemento de tipo ProgramacionTrimestral. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public ProgramacionTrimestral guardar(ProgramacionTrimestral poaProgramacionTrimestral) throws GeneralException {
       // logger.log(Level.INFO, "guardar");
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (progValidacion.validar(poaProgramacionTrimestral)) {
                if (poaProgramacionTrimestral.getId() == null) {
                    poaProgramacionTrimestral = (ProgramacionTrimestral) programacionTrimestralDAO.create(poaProgramacionTrimestral, du.getCodigoUsuario(), du.getOrigen());
                } else {
                    
                    poaProgramacionTrimestral = (ProgramacionTrimestral) programacionTrimestralDAO.update(poaProgramacionTrimestral, du.getCodigoUsuario(), du.getOrigen());
                }
            }
            return poaProgramacionTrimestral;
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
     * Devuelve el elemento ProgramacionTrimestral por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public ProgramacionTrimestral obtenerPorId(Integer id) throws GeneralException {
        logger.log(Level.INFO, "obtenerPOAPorId");
        try {
            return (ProgramacionTrimestral) programacionTrimestralDAO.findById(ProgramacionTrimestral.class, id);
        } catch (DAOGeneralException ex) {
           logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    /**
     * Devuelve el elemento ProgramacionTrimestral por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public ProgramacionTrimestral obtenerPorAnioFiscal(Integer id) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorAnioFiscal");
        try {
            List<ProgramacionTrimestral> listado = programacionTrimestralDAO.findByOneProperty(ProgramacionTrimestral.class, "anio.id", id);
            
            if(listado != null && !listado.isEmpty() && listado.size() > 0) {
                return listado.get(0);
            }
        } catch (DAOGeneralException ex) {
           logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
        return null;
    }
    
    /**
     * Devuelve todos los elementos de tipo ProgramacionTrimestral
     *
     * @return
     * @throws GeneralException
     */
    public List<ProgramacionTrimestral> obtenerTodos() throws GeneralException {
        logger.log(Level.INFO, "obtenerTodos");
        try {
            return programacionTrimestralDAO.findAll(ProgramacionTrimestral.class);
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Elimina el objeto ProgramacionTrimestral
     *
     * @return
     * @throws GeneralException
     */
    public void eliminar(Integer id) throws GeneralException {
        logger.log(Level.INFO, "eliminar");
        try {
            ProgramacionTrimestral progTri = (ProgramacionTrimestral) generalDAO.findById(ProgramacionTrimestral.class, id);
            generalDAO.delete(progTri);
        } catch (Exception ex) {
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
    public List<ProgramacionTrimestral> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorCriteria");
        try {
            return programacionTrimestralDAO.findEntityByCriteria(ProgramacionTrimestral.class, cto, orderBy, ascending, startPosition, cantidad);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

        }
        return null;
    }
    
    public ProgramacionTrimestral obtenerHistoricoPorVersion(int id, int version) {
        List<ProgramacionTrimestral> lista = AuditReader.readAllByVersion(generalDAO.getEntityManager(), ProgramacionTrimestral.class, id, version);
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        }
        return null;
    }
}
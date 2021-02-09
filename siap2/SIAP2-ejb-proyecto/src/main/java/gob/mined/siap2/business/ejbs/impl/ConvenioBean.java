/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Convenio;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.ConvenioDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos de servicio vinculados a convenios.
 * @author Sofis Solutions
 */
@Stateless(name = "ConvenioBean")
@LocalBean
public class ConvenioBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ConvenioDAO convenioDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza un convenio.
     * @param convenio 
     */
    public void crearActualizarConvenio(Convenio convenio) {
        try {
 
             
            generalDAO.update(convenio);
 
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
 
    /**
     * Este método devuelve un convenio a partir de su id.
     * @param idConvenio
     * @return 
     */
    public Convenio getConvenio(Integer idConvenio) {
        Convenio convenio = generalDAO.getEntityManager().find(Convenio.class, idConvenio);
        convenio.getCategorias().isEmpty();
        return convenio;
    }

    /**
     * retorna las categorías pertenecientes al convenio
     * @param idConvenio
     * @return 
     */
    public List<CategoriaConvenio> getCategorias(Integer idConvenio) {
        Convenio convenio = generalDAO.getEntityManager().find(Convenio.class, idConvenio);
        if (convenio != null) {
            return convenio.getCategorias();
        } else {
            return new ArrayList<CategoriaConvenio>();
        }
    }

    /**
     * Este método devuelve todos los convenios que tienen query como sub cadena del código.
     * @param query
     * @return 
     */
    public List<Convenio> getConveniosHabilitadosPorCodigo(String query) {
        try {
            List<Convenio> convenios = convenioDAO.getConveniosHabilitadosPorCodigo(query);
            convenios.isEmpty();
            return convenios;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}

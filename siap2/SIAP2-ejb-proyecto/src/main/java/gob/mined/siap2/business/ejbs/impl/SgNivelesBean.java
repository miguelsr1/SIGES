
package gob.mined.siap2.business.ejbs.impl;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgNivel;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.SgNivelesDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "sgNivelesBean")
@LocalBean
public class SgNivelesBean {
    
    private static final Logger logger = Logger.getLogger(SgNivelesBean.class.getName());
    
    @Inject
    @JPADAO        
    private SgNivelesDAO sgNivelesDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO; 
    
    /**
     * Este metodo devuelve un registro Niveles, filtrado por ID
     * @param id
     * @return 
     */
    public SgNivel getSgNivelesById(Integer id) {
        return generalDAO.getEntityManager().find(SgNivel.class, id);
    }

    /**
     * Este metodo devuelve el listado completo de Niveles
     * @return 
     */
    public List<SgNivel> getSgNiveles() {
        try {
            List<SgNivel> listaRegistros = generalDAO.findAll(SgNivel.class);
            listaRegistros.isEmpty();
            return listaRegistros;
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
    /**
     * Este metodo devuelve el listado completo de Niveles
     * @return 
     */
    public List<SgNivel> getSgNivelesByOrganizacionCurricular(Integer idOc) {
        try {
            List<SgNivel> listaRegistros = sgNivelesDAO.findSgNivelesByOc(idOc);
            listaRegistros.isEmpty();
            return listaRegistros;
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
        
    /**
     * Este metodo devuelve todos los registros de Niveles, que se encuentren habilitados
     * @return 
     */
    public List<SgNivel> getSgNivelesHabilitados() {
        try {
            List<SgNivel> listaRegistros = sgNivelesDAO.findSgNivelesHabilitados();
            listaRegistros.isEmpty();
            return listaRegistros;
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

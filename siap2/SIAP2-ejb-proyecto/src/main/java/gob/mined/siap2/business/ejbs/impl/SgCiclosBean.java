
package gob.mined.siap2.business.ejbs.impl;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgCiclo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.SgCiclosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "sgCiclosBean")
@LocalBean
public class SgCiclosBean {
    
    private static final Logger logger = Logger.getLogger(SgCiclosBean.class.getName());
    
    @Inject
    @JPADAO        
    private SgCiclosDAO sgCiclosDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO; 
    
    /**
     * Este metodo devuelve un registro Ciclos, filtrado por ID
     * @param id
     * @return 
     */
    public SgCiclo getSgCiclosById(Integer id) {
        return generalDAO.getEntityManager().find(SgCiclo.class, id);
    }

    /**
     * Este metodo devuelve el listado completo de Ciclos
     * @return 
     */
    public List<SgCiclo> getSgCiclos() {
        try {
            List<SgCiclo> listaRegistros = generalDAO.findAll(SgCiclo.class);
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
     * Este metodo devuelve todos los registros de Ciclos, que se encuentren habilitados
     * @return 
     */
    public List<SgCiclo> getSgCiclosHabilitados() {
        try {
            List<SgCiclo> listaRegistros = sgCiclosDAO.findSgCiclosHabilitados();
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
    
    
    public List<SgCiclo> getSgCiclosByNivel(Integer idNiv) {
        try {
            List<SgCiclo> listaRegistros = sgCiclosDAO.findSgCiclosByNivel(idNiv);
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

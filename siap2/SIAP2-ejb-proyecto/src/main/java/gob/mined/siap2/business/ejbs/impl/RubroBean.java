package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Rubro;
import gob.mined.siap2.entities.data.impl.SubRubro;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.RubroDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "RubroBean")
public class RubroBean {
    
    private static final Logger logger = Logger.getLogger(RubroBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private RubroDAO rubroDAO;
    
    
    
    /**
     * Este método crea o actualiza un Rubro
     * @param rubro 
     */
    public void crearActualizar(Rubro rubro) {
        try {
            generalDAO.update(rubro);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    /**
     * Método que se encarga de eliminar un Rubro
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            Rubro ai = (Rubro) generalDAO.find(Rubro.class, id);
            if(ai != null){
                generalDAO.delete(ai);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un Rubro filtrado por ID
     * @param gesId
     * @return 
     */
    public Rubro getRubroById(Integer gesId) {
        return generalDAO.getEntityManager().find(Rubro.class, gesId);
    }

    /**
     * Este método devuelve todos los registros de Rubros
     * @return 
     */
    public List<Rubro> getRubros() {
        try {
            List<Rubro> listaRegistros = rubroDAO.getRubros();
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
     * Este método devuelve todos los registros de Rubros que se encuentren habilitados
     * @return 
     */
    public List<Rubro> getRubrosHabilitados() {
        try {
            List<Rubro> listaRegistros = rubroDAO.getRubrosHabilitados();
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
     * Este método crea o actualiza un SubRubro
     * @param subRubro 
     */
    public void crearActualizarSubRubro(SubRubro subRubro) {
        try {
            generalDAO.update(subRubro);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    /**
     * Método que se encarga de eliminar un SubRubro
     * @param id Identificador de registro a eliminar
     */
    public void eliminarSubRubro(Integer id) {
        try {
            SubRubro ai = (SubRubro) generalDAO.find(SubRubro.class, id);
            if(ai != null){
                generalDAO.delete(ai);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un SubRubro filtrado por ID
     * @param gesId
     * @return 
     */
    public SubRubro getSubRubroById(Integer gesId) {
        return generalDAO.getEntityManager().find(SubRubro.class, gesId);
    }

    /**
     * Este método devuelve todos los registros de SubRubros
     * @return 
     */
    public List<SubRubro> getSubRubros() {
        try {
            List<SubRubro> listaRegistros = rubroDAO.getSubRubros();
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
     * Este método devuelve todos los registros de SubRubros que se encuentren habilitados
     * @return 
     */
    public List<SubRubro> getSubRubrosHabilitados() {
        try {
            List<SubRubro> listaRegistros = rubroDAO.getSubRubrosHabilitados();
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

package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.RelacionAreasInversionInsumo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.RelacionAreasInversionInsumoDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;


@LocalBean
@Stateless(name = "RelacionAreasInversionInsumoBean")
public class RelacionAreasInversionInsumoBean {
    
    
    private static final Logger logger = Logger.getLogger(AreasInversionBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private RelacionAreasInversionInsumoDAO areasInversionInsumoDAO;
    
    
       
    /**
     * Este método crea o actualiza un registro de relacion de RelacionAreasInversionInsumo
     * @param raii
     * @throws PersistenceException
     * @throws BusinessException
     * @throws DAOConstraintViolationException
     */
    public void crearActualizar(RelacionAreasInversionInsumo raii)throws DAOConstraintViolationException{
        try {
            areasInversionInsumoDAO.update(raii);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
            
        } catch(DAOConstraintViolationException e){
            throw e;
            
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        } 

    }
    
    /**
     * Método que se encarga de eliminar una registro de relacion de RelacionAreasInversionInsumo
     * actividades
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            RelacionAreasInversionInsumo ai = (RelacionAreasInversionInsumo) generalDAO.find(RelacionAreasInversionInsumo.class, id);
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
     * Este método devuelve un registro de Insumo, filtrado por ID
     * @param id
     * @return 
     */
    public Insumo getInsumoById(Integer id) {
        return generalDAO.getEntityManager().find(Insumo.class, id);
    }
    
    
    /**
     * Este método devuelve una relacion de areas de inversion e insumo, filtrados por un ID
     * @param gesId
     * @return 
     */
    public RelacionAreasInversionInsumo getRelacionAreaInversionInsumoById(Integer gesId) {
        return generalDAO.getEntityManager().find(RelacionAreasInversionInsumo.class, gesId);
    }

    
    /**
     * Este método devuelve todos los registros de relación de areas de inversion 
     * filtrado por ID de AreasInversion
     * 
     * @param idAreaInversion
     * @return 
     */
    public List<RelacionAreasInversionInsumo> getRelacionByAreaInversionId(Integer idAreaInversion) {
        try {
            List<RelacionAreasInversionInsumo> listaRegistros = areasInversionInsumoDAO.getRelAreasInversionInsumoByAreasInversionId(idAreaInversion);
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
     * Este método devuelve todos los registros de relación de areas de inversion 
     * filtrado por ID de Insumo
     * 
     * @param idInsumo
     * @return 
     */
    public List<RelacionAreasInversionInsumo> getRelacionByInsumoId(String idInsumo) {
        try {
            List<RelacionAreasInversionInsumo> listaRegistros = areasInversionInsumoDAO.getRelAreasInversionInsumoByInsumoId(idInsumo);
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
     * Este método devuelve un registro de relación de areas de inversion 
     * filtrado por ID de Insumo y ID AreasInversion
     * 
     * @param idInsumo
     * @param idAreaInversion
     * @return 
     */
    public RelacionAreasInversionInsumo getRelacionByIds(String idAreaInversion, String idInsumo) {
        try {
            return areasInversionInsumoDAO.getRelAreasInversionInsumoByIds(idAreaInversion, idInsumo);
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

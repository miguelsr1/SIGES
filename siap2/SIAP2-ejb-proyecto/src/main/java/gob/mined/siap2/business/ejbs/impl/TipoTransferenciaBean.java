package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.TipoTransferencia;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.TipoTransferenciaDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "tipoTransferenciaBean")
public class TipoTransferenciaBean {
    private static final Logger logger = Logger.getLogger(TipoTransferenciaBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private TipoTransferenciaDAO tipoTransferenciaDAO;
    
    
    
    /**
     * Este método crea o actualiza un TipoTransferencia
     * @param rubro 
     */
    public void crearActualizar(TipoTransferencia rubro) {
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
     * Método que se encarga de eliminar un TipoTransferencia
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            TipoTransferencia ai = (TipoTransferencia) generalDAO.find(TipoTransferencia.class, id);
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
     * Este método devuelve un TipoTransferencia filtrado por ID
     * @param gesId
     * @return 
     */
    public TipoTransferencia getTransferenciaById(Integer gesId) {
        return generalDAO.getEntityManager().find(TipoTransferencia.class, gesId);
    }

    /**
     * Este método devuelve todos los registros de Rubros
     * @return 
     */
    public List<TipoTransferencia> getTiposTransferencias() {
        try {
            List<TipoTransferencia> listaRegistros = tipoTransferenciaDAO.getTiposTransferencia();
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
     * Este método devuelve todos los registros de TipoTransferencia que se encuentren habilitados
     * @return 
     */
    public List<TipoTransferencia> getTiposTransferenciasHabilitados() {
        try {
            List<TipoTransferencia> listaRegistros = tipoTransferenciaDAO.getTransferenciasHabilitados();
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
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.TipoCredito;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.TipoCreditoDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;


@Stateless(name = "tipoCreditoBean")
@LocalBean
public class TipoCreditoBean {
    
    
    private static final Logger logger = Logger.getLogger(TipoCreditoBean.class.getName());
    
    @Inject
    @JPADAO
    private TipoCreditoDAO tipoCreditoDAO;
    
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    /**
     * Este método devuelve un registro de TipoCredito, filtrado por ID
     * @param id
     * @return 
     */
    public TipoCredito getTipoCreditoById(Integer id) {
        return generalDAO.getEntityManager().find(TipoCredito.class, id);
    }

    /**
     * Este método devuelve todos los registros de TipoCredito
     * @return 
     */
    public List<TipoCredito> getTipoCredito() {
        try {
            List<TipoCredito> listaRegistros = tipoCreditoDAO.getTipoCredito();
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
     * Este método devuelve todos los registros de TipoCredito que se encuentren habilitados
     * @return 
     */
    public List<TipoCredito> getTipoCreditoHabilitados() {
        try {
            List<TipoCredito> listaRegistros = tipoCreditoDAO.getTipoCreditoHabilitados();
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

package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgDesembolso;
import gob.mined.siap2.entities.data.impl.DesembolsoTransferenciaComponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.DesembolsosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;


@LocalBean
@Stateless(name = "DesembolsosBean")
public class DesembolsosBean {
    
    
    private static final Logger logger = Logger.getLogger(DesembolsosBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private DesembolsosDAO desembolsosDAO;
    
    
    
   
    
    /**
     * Este método busca un registro de Desembolso filtrado por su ID
     * @param idDesembolso
     * @return 
     */
    public DesembolsoTransferenciaComponente getDesembolsoById(Integer idDesembolso) {
        return generalDAO.getEntityManager().find(DesembolsoTransferenciaComponente.class, idDesembolso);
    }
    
    /**
     * Este método obtiene un listado de registros de Desembolso, filtrados por ID de transferenciaComponente
     * @param idTC
     * @return 
     */
    public List<DesembolsoTransferenciaComponente> getAllDesembolsByTransferenciaComponente(Integer idTC){
        try {
            return desembolsosDAO.getAllDesembolsByTransferenciaComponente(idTC);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
    
    /**
     * Este método crea o actualiza un registro de Desembolso
     *
     * @param desem
     */
    public void crearActualizarMovimiento(DesembolsoTransferenciaComponente desem) {
        try {
            BusinessException be = new BusinessException();
            if (desem != null) {
                if(desem.getTransferenciasComponente() == null){
                    be.addError(ConstantesErrores.ERR_TRANSFERENCIA_COMPONENTE_VACIO);
                }
                
                if(!be.getErrores().isEmpty()){
                    throw be;
                }
                if (desem.getId() != null) {
                    generalDAO.update(desem);
                } else {
                    generalDAO.create(desem);
                }
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Método que se encarga de eliminar un Desembolso
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            DesembolsoTransferenciaComponente ai = (DesembolsoTransferenciaComponente) generalDAO.find(DesembolsoTransferenciaComponente.class, id);
            if (ai != null) {
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
     * Este método crea un registro de desembolso por cada centro educativo asignados a una transferenciaComponente
     *
     * @param lista
     */
    public void generarDesembolsoPorCentro(List<SgDesembolso> lista) {
        try {
            for(SgDesembolso des : lista){
                des = (SgDesembolso) generalDAO.create(des);
                desembolsosDAO.guardarAuditoriaDesembolso(des);
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}

package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroTransferenciaDireccionDepartamental;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.TransferenciaDireccionDepartamentalDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;


@LocalBean
@Stateless(name = "TransferenciaDireccionDepartamentalBean")
public class TransferenciaDireccionDepartamentalBean {
 
    private static final Logger LOGGER = Logger.getLogger(TransferenciaDireccionDepartamentalBean.class.getName());
    
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private TransferenciaDireccionDepartamentalDAO transferenciaDireccionDepartamentalDAO;
    
    
    
    /**
     * Este método busca un registro de TransferenciaDireccionDepartamental filtrada por su ID
     * @param idTDD
     * @return 
     */
    public TransferenciaDireccionDepartamental getTransferenciasDirDepaById(Integer idTDD) {
        try {
            return (TransferenciaDireccionDepartamental) generalDAO.find(TransferenciaDireccionDepartamental.class, idTDD);
        } catch(NoResultException re){
            return null;
        }
    }
    
    
    /**
     * Este método busca un registro de TransferenciaDireccionDepartamental que se relacione con los filtro parametrizados
     * 
     * @param idTransfComp
     * @param idDirDepa
     * @return 
     */
    public TransferenciaDireccionDepartamental getTransferenciasDirDepaByTransfCompYDirDepa(Integer idTransfComp, Integer idDirDepa) {
        try {
            return transferenciaDireccionDepartamentalDAO.getTransferenciasDirDepaByTransfCompYDirDepa(idTransfComp, idDirDepa);
        } catch(NoResultException re){
            return null;
        }catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Metodo utilizado para el filtrado de registros TransferenciaDireccionDepartamental
     * @param filtro
     * @return 
     */
    public List<TransferenciaDireccionDepartamental> getTDDByFiltro(FiltroTransferenciaDireccionDepartamental filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getIdDireccionDepartamental() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "direccionDep.pk", filtro.getIdDireccionDepartamental());
                criterios.add(criterio);
            }

            if (filtro.getIdTransferenciaComponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "transferenciasComponente.id", filtro.getIdTransferenciaComponente());
                criterios.add(criterio);
            }
            
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }
            return generalDAO.findEntityByCriteria(TransferenciaDireccionDepartamental.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (DAOGeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
}

package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.TransferenciasACedDAO;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

@LocalBean
@Stateless(name = "TransferenciasACedBean")
public class TransferenciasACedBean {
    
    
    private static final Logger logger = Logger.getLogger(TransferenciasACedBean.class.getName());
    private static final String TDD_CREADO = "tddCreado";
    private static final String TDD_ACTUALIZADO = "tddActualizado";
    
    private static final String ACED_CREADO = "acedCreado";
    private static final String ACED_ACTUALIZADO = "acedActualizado";
    
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private TransferenciasACedDAO transferenciasACedDAO;

    
    
    /**
     * Este método crea y actualiza un registro de TransferenciasACed
     * @param taced
     */
    public void crearActualizar(TransferenciasACed taced) {
        try {
            generalDAO.update(taced);
        } catch (BusinessException be) {
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
     * Este método crea y actualiza una lista de registros de TransferenciasACed
     * 
     * @param mapaRegistros
     * @return 
     */
    public HashMap<String, Integer> crearActualizarTransferenciaDireccionDepartamental(HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> mapaRegistros) {
        try {
            HashMap<String, Integer> resultados = new HashMap<String, Integer>();
            int tddCreado = 0;
            int tddActualizado = 0;
            int acedCreado = 0;
            int acedActualizado = 0;
            if(mapaRegistros != null && !mapaRegistros.isEmpty()){
                
                for(Entry<TransferenciaDireccionDepartamental, List<TransferenciasACed>> entry : mapaRegistros.entrySet()){

                    HashMap<String, TransferenciaDireccionDepartamental> mapaTDD = crearActualizarTransferenciaDireccionDepartamental(entry.getKey());
                    TransferenciaDireccionDepartamental tdd = null;
                    if(mapaTDD.get(TDD_CREADO) != null){
                        tdd = mapaTDD.get(TDD_CREADO);
                        tddCreado++;
                    }else if(mapaTDD.get(TDD_ACTUALIZADO) != null){
                        tdd = mapaTDD.get(TDD_ACTUALIZADO);
                        tddActualizado++;
                    }
                    
                    
                    for(TransferenciasACed aced : entry.getValue()){
                        aced.setTransferenciaDireccionDep(tdd);
                        if(aced.getId() != null){
                            generalDAO.getEntityManager().persist(aced);
                            acedActualizado++;
                        }else{
                            generalDAO.create(aced);
                            acedCreado++;
                        }
                    }
                }
                resultados.put(TDD_CREADO, tddCreado);
                resultados.put(TDD_ACTUALIZADO, tddActualizado);
                resultados.put(ACED_CREADO, acedCreado);
                resultados.put(ACED_ACTUALIZADO, acedActualizado);
            }
            return resultados;
        } catch (BusinessException be) {
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
     * Este método crea y actualiza una lista de registros de TransferenciasACed
     * @param tdd
     * @return 
     */
    public HashMap<String, TransferenciaDireccionDepartamental> crearActualizarTransferenciaDireccionDepartamental(TransferenciaDireccionDepartamental tdd) {
        try {
            HashMap<String, TransferenciaDireccionDepartamental> resultados = new HashMap<String, TransferenciaDireccionDepartamental>();
            TransferenciaDireccionDepartamental reg;
            if(tdd.getPk() == null){
                reg = (TransferenciaDireccionDepartamental) generalDAO.create(tdd);
                resultados.put(TDD_CREADO, reg);
                return resultados;
            }else{
                reg = (TransferenciaDireccionDepartamental) generalDAO.update(tdd);
                resultados.put(TDD_ACTUALIZADO, reg);
                return resultados;
            }
        } catch (BusinessException be) {
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
     * Este método crea y actualiza una TransferenciaDireccionDepartamental y todas las transferenciasACed relacionadas
     * 
     * @param tdd
     * @param listaAced
     * @return 
     */
    public HashMap<String, Integer> crearActualizarTransferenciasDireccionesDepartamental(TransferenciaDireccionDepartamental tdd, List<TransferenciasACed> listaAced) {
        try {
            int acedCreado = 0;
            int acedActualizado = 0;
            HashMap<String, Integer> resultados = new HashMap<String, Integer>();

            TransferenciaDireccionDepartamental reg;
            if(tdd.getPk() == null){
                reg = (TransferenciaDireccionDepartamental) generalDAO.create(tdd);
                resultados.put(TDD_CREADO, 1);
            }else{
                reg = (TransferenciaDireccionDepartamental) generalDAO.update(tdd);
                resultados.put(TDD_ACTUALIZADO, 1);
            }

            for(TransferenciasACed aced : listaAced){
                aced.setTransferenciaDireccionDep(tdd);
                if(aced.getId() != null){
                    generalDAO.getEntityManager().merge(aced);
                    acedActualizado++;
                }else{
                    generalDAO.getEntityManager().persist(aced);
                    acedCreado++;
                }
                resultados.put(ACED_CREADO, acedCreado);
                resultados.put(ACED_ACTUALIZADO, acedActualizado);
            }
            return resultados;
        } catch (BusinessException be) {
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
     * Este método crea y actualiza muchas TransferenciaDireccionDepartamental y todas las transferenciasACed relacionadas
     * 
     * @param sedesPorTDD
     * @return 
     */
    public HashMap<String, Integer> crearActualizarTransferenciasDireccionesDepartamental(HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD) {
        try {
            int acedCreado = 0;
            int acedActualizado = 0;
            int tddCreado = 0;
            int tddActualizado = 0;
            
            HashMap<String, Integer> resultados = new HashMap<String, Integer>();

            for(Entry<TransferenciaDireccionDepartamental, List<TransferenciasACed>> item : sedesPorTDD.entrySet()){
               TransferenciaDireccionDepartamental tdd;
                if(item.getKey().getPk() == null){
                    tdd = (TransferenciaDireccionDepartamental) generalDAO.create(item.getKey());
                    transferenciasACedDAO.guardarAuditoriaTransferenciasDepartamental(tdd);
                    tddCreado ++;
                }else{
                    tdd = (TransferenciaDireccionDepartamental) generalDAO.update(item.getKey());
                    transferenciasACedDAO.guardarAuditoriaTransferenciasDepartamental(tdd);
                    tddActualizado ++;
                } 
                
                for (TransferenciasACed aced : item.getValue()) {
                    aced.setTransferenciaDireccionDep(tdd);
                    if (aced.getId() == null) {
                        aced = (TransferenciasACed) generalDAO.create(aced);
                        transferenciasACedDAO.guardarAuditoriaTransferenciasACed(aced);
                        acedCreado++;
                    } else {
                        aced = (TransferenciasACed) generalDAO.update(aced);
                        transferenciasACedDAO.guardarAuditoriaTransferenciasACed(aced);
                        acedActualizado++;
                    }
                }
            }
            resultados.put(ACED_CREADO, acedCreado);
            resultados.put(ACED_ACTUALIZADO, acedActualizado);
            resultados.put(TDD_CREADO, tddCreado);
            resultados.put(TDD_ACTUALIZADO, tddActualizado);
            return resultados;
        } catch (BusinessException be) {
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
     * Este método busca un registros de TransferenciaACed que se relacione con los filtros parametizados
     * 
     * @param idTransfComp
     * @param idSede
     * @param idTDD
     * @return 
     */
    public TransferenciasACed getTransferenciaACedByComponenteSedeTDD(Integer idTransfComp, Integer idSede, Integer idTDD) {
        try {
            return transferenciasACedDAO.getTransferenciaACedByComponenteSedeTDD(idTransfComp, idSede, idTDD);
        } catch(NoResultException re){
            return null;
        } catch (BusinessException be) {
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
     * Este método obtiene el total de las tranaferencias de una sede por año y departamental
     * @param idTc
     * @param idSede
     * @param idTDD
     * @return
     * @throws DAOGeneralException 
     */
    
    public BigDecimal getTotalMontoTransferenciaACedByComponenteSedeTDD(Integer componenteId,Integer subComponenteId, Integer unidadId, Integer lineaId, Integer fuenteRecursosId,Integer anioId, Integer idSede) throws DAOGeneralException {
        try {
            BigDecimal monto = transferenciasACedDAO.getTotalMontoTransferenciaACedByComponenteSedeTDD(componenteId, subComponenteId, unidadId,lineaId, fuenteRecursosId,anioId,idSede);
            if(monto == null) {
                monto = BigDecimal.ZERO;
            }
            return monto;
        } catch(NoResultException re){
            logger.log(Level.SEVERE, null, re);
            return BigDecimal.ZERO;
        } catch (BusinessException be) {
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
}
